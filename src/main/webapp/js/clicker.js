document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('credit_btn');

    if (btn) {
        btn.addEventListener('click', (e) => {
            showClickGain(e.clientX, e.clientY);
            // petit rebond a chaque clic
            btn.animate([{ transform: 'scale(0.9)' }, { transform: 'scale(1.05)' }, { transform: 'scale(1)' }], { duration: 200 });
            fetch('click', { method: 'POST' })
                .then(res => res.text())
                .then(credit => setCredits(credit));
        });
    }

    affichage();

    if (typeof offlineGain !== 'undefined' && offlineGain > 0) {
        showToast("Pendant votre absence : +" + formatCredits(offlineGain) + " crédits", 'success');
    }
});

// icone Font Awesome de chaque bonus de la boutique (cookie par defaut)
const BONUS_ICONS = {
    'Autoclicker': 'fa-arrow-pointer',
    'Mineur': 'fa-person-digging',
    'Super Mineur': 'fa-helmet-safety',
    'Ferme': 'fa-wheat-awn',
    'Mine': 'fa-gem',
    'Usine': 'fa-industry',
    'Banque': 'fa-building-columns',
    'Temple': 'fa-place-of-worship',
    'Tour de Sorcier': 'fa-hat-wizard',
    'Vaisseau Spatial': 'fa-rocket',
    "Laboratoire d'Alchimie": 'fa-flask'
};

async function loadAllBonuses() {
    // Utilisation de l'IA pour l'affichage stylisé des bonus

    const container = document.getElementById('bonuses-container');
    const paletteGrid = document.getElementById('palette-grid');

    if (!container) return;

    try {
        const response = await fetch(contextPath + '/bonuses');

        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des bonus');
        }

        const data = await response.json();
        const bonuses = data.bonuses ? data.bonuses : [];
        currentCps = data.totalCps ? data.totalCps : 0;
        showCps();

        container.innerHTML = '';

        if (bonuses.length === 0) {
            container.innerHTML = '<p class="text-gray-500 italic text-center">Aucun bonus disponible pour le moment.</p>';
            return;
        }

        if (paletteGrid) {
            paletteGrid.querySelectorAll('[id^="palette-color-"]').forEach(el => el.remove());
        }

        bonuses.forEach(bonus => {
            const displayPrice = bonus.currentPrice ? bonus.currentPrice : bonus.cost;
            const quantity = bonus.userQuantityOwned || 0;

            if (bonus.type === 'PASSIVE') {
                const bonusDiv = document.createElement('div');
                bonusDiv.className = 'card p-4 flex justify-between items-center hover:shadow-lg transition-shadow';

                const quantityHtml = quantity > 0
                    ? `<div class="hidden sm:flex absolute right-36 top-1/2 transform -translate-y-1/2 select-none pointer-events-none">
                           <span class="text-5xl font-black text-gray-200">x${quantity}</span>
                       </div>`
                    : '';

                bonusDiv.classList.add('relative');

                bonusDiv.innerHTML = `
                    <div class="w-12 h-12 shrink-0 mr-4 rounded-xl bg-cookie-100 text-cookie-700 text-xl flex items-center justify-center relative z-10">
                        <i class="fas ${BONUS_ICONS[bonus.name] || 'fa-cookie'}"></i>
                    </div>
                    <div class="flex-1 pr-4 relative z-10">
                        <h3 class="font-bold text-lg text-choco">${bonus.name}</h3>
                        <p class="text-sm text-gray-600 line-clamp-2">${bonus.description}</p>
                        <p class="text-xs font-semibold text-green-600 mt-1">
                            <i class="fas fa-arrow-trend-up mr-1"></i>+${Number(bonus.creditsGeneratedPerSecond).toLocaleString('fr-FR')} crédits/sec
                        </p>
                    </div>
                
                    ${quantityHtml}
                
                    <div class="flex flex-col items-end gap-2 shrink-0 relative z-10">
                        <span class="font-bold text-yellow-600 bg-yellow-50 px-2 py-1 rounded">
                            <i class="fas fa-coins mr-1"></i>${formatCredits(displayPrice)}
                        </span>
                        <button onclick="buyBonus(${bonus.id})" data-price="${displayPrice}"
                                class="buy-btn bg-cookie-600 enabled:hover:bg-cookie-700 text-white px-4 py-2 rounded shadow transition-colors text-sm font-bold enabled:active:scale-95 disabled:bg-gray-400 disabled:cursor-not-allowed">
                            Acheter
                        </button>
                    </div>
                `;

                container.appendChild(bonusDiv);
            } else if (bonus.type === 'COLOR' && paletteGrid) {
                const isUnlocked = quantity > 0;
                const colorCode = bonus.unlockedColor;

                const paletteColorDiv = document.createElement('div');
                paletteColorDiv.id = `palette-color-${bonus.id}`;
                paletteColorDiv.dataset.color = colorCode;

                if (isUnlocked) {
                    paletteColorDiv.className = 'selected-color w-7 h-7 rounded cursor-pointer';
                    paletteColorDiv.style.backgroundColor = colorCode;

                    paletteColorDiv.onclick = () => {
                        if (typeof changeSelectedColor === 'function') {
                            changeSelectedColor(colorCode);
                        }
                    };
                } else {
                    // Utilisation de l'IA pour styliser les couleurs verrouillées avec un cadenas et une bulle de prix
                    paletteColorDiv.className = 'bonus-color-item w-7 h-7 rounded flex items-center justify-center transition-all cursor-pointer opacity-40 hover:opacity-70 group relative';
                    paletteColorDiv.style.backgroundColor = colorCode;

                    paletteColorDiv.innerHTML = `
                        <i class="fas fa-lock text-white text-[12px] drop-shadow-md"></i>
                        
                        <div class="pointer-events-none absolute bottom-full left-1/2 -translate-x-1/2 mb-1 w-max opacity-0 transition-opacity duration-200 group-hover:opacity-100 bg-gray-900 text-white text-xs font-bold rounded py-1 px-2 z-50 shadow-lg">
                            <i class="fas fa-coins text-yellow-400 mr-1"></i>${formatCredits(displayPrice)}
                        </div>
                    `;

                    paletteColorDiv.onclick = () => buyBonus(bonus.id);
                }

                paletteGrid.appendChild(paletteColorDiv);
            }
        });

        refreshAffordability();

    } catch (error) {
        console.error("Erreur de chargement des bonus:", error);
        container.innerHTML = '<p class="text-red-500 text-center text-sm"><i class="fas fa-triangle-exclamation mr-1"></i> Impossible de charger la boutique.</p>';
    }
}

async function buyBonus(bonusId) {
    if (!isConnected) {
        showToast("Vous devez être connecté pour acheter un bonus !");
        return;
    }

    const saveResponse = await fetch('save', { method: 'POST' });

    if (!saveResponse.ok) {
        showToast("Erreur lors de la sauvegarde avant l'achat. Veuillez réessayer.");
        return;
    }

    try {
        const response = await fetch(contextPath + '/bonuses/buy/' + bonusId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            setCredits(await response.text());
            showToast("Bonus acheté !", 'success');
            await loadAllBonuses();
        } else {
            const errorMsg = await response.text();
            showToast("Achat impossible : " + errorMsg);
        }
    } catch (error) {
        console.error("Erreur lors de l'achat :", error);
        showToast("Erreur lors de l'achat. Veuillez réessayer.");
    }
}

// Variables pour la gestion du compteur de crédits fluide
let currentCredits = 0;
let currentCps = 0;
let lastFrameTime = performance.now();

const UNITS = [[1e12, 'Bn'], [1e9, 'Md'], [1e6, 'M']];

// 12345.6 -> "12 345", 1234567 -> "1,23 M", 75000000000 -> "75 Md"
function formatCredits(value) {
    const n = Math.floor(value);
    for (const [size, unit] of UNITS) {
        if (n >= size) {
            // tronque pour ne jamais afficher plus que la valeur reelle
            const short = Math.floor(n / size * 100) / 100;
            return short.toLocaleString('fr-FR') + ' ' + unit;
        }
    }
    return n.toLocaleString('fr-FR');
}

// seul point d'ecriture du compteur, la valeur serveur arrive en texte ("12.5")
function setCredits(value) {
    currentCredits = Number.parseFloat(value) || 0;
    const label = document.getElementById('nb_click');
    if (label) label.innerText = formatCredits(currentCredits);
    refreshAffordability();
}

// "+1" qui monte et disparait a l'endroit du clic, sans attendre le serveur
function showClickGain(x, y) {
    const gain = document.createElement('span');
    gain.textContent = '+1';
    gain.className = 'fixed pointer-events-none select-none text-2xl font-black text-yellow-600 drop-shadow z-40';
    gain.style.left = (x - 12) + 'px';
    gain.style.top = (y - 20) + 'px';
    document.body.appendChild(gain);

    gain.animate(
        [{ transform: 'translateY(0)', opacity: 1 }, { transform: 'translateY(-60px)', opacity: 0 }],
        { duration: 800, easing: 'ease-out' }
    ).onfinish = () => gain.remove();
}

// cps sous le compteur, cache si 0 (visiteur non connecte)
function showCps() {
    const label = document.getElementById('cps_label');
    if (!label) return;
    label.classList.toggle('hidden', currentCps <= 0);
    const value = currentCps >= 1e6
        ? formatCredits(currentCps)
        : currentCps.toLocaleString('fr-FR', { maximumFractionDigits: 1 });
    label.textContent = '+' + value + ' /s';
}

// grise les bonus trop chers, non connecte on laisse cliquer pour afficher le message de connexion
function refreshAffordability() {
    if (!isConnected) return;
    document.querySelectorAll('.buy-btn').forEach(btn => {
        btn.disabled = currentCredits < Number(btn.dataset.price);
    });
}

/**
 * Fonction affichage : gère l'animation fluide du compteur de crédits avec ia
 */
function affichage() {
    const label = document.getElementById('nb_click');
    if (!label) return;

    // Initialisation au premier lancement
    setCredits(label.innerText);

    function updateCounter(now) {
        const deltaTime = (now - lastFrameTime) / 1000;
        lastFrameTime = now;

        if (currentCps > 0) {
            currentCredits += currentCps * deltaTime;
        }
        label.innerText = formatCredits(currentCredits);
        refreshAffordability();
        requestAnimationFrame(updateCounter);
    }
    requestAnimationFrame(updateCounter);
}
