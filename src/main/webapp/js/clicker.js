document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('credit_btn');

    if (btn) {
        btn.addEventListener('click', () => {
            fetch('click', { method: 'POST' })
                .then(res => res.text())
                .then(credit => setCredits(credit));
        });
    }

    affichage();
});

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
                bonusDiv.className = 'bg-white p-4 rounded-lg shadow-sm border border-gray-200 flex justify-between items-center hover:shadow-md transition-shadow';

                const quantityHtml = quantity > 0
                    ? `<div class="hidden sm:flex absolute right-36 top-1/2 transform -translate-y-1/2 select-none pointer-events-none">
                           <span class="text-5xl font-black text-gray-200">x${quantity}</span>
                       </div>`
                    : '';

                bonusDiv.classList.add('relative');

                bonusDiv.innerHTML = `
                    <div class="flex-1 pr-4 relative z-10">
                        <h3 class="font-bold text-lg text-gray-800">${bonus.name}</h3>
                        <p class="text-sm text-gray-600 line-clamp-2">${bonus.description}</p>
                        <p class="text-xs font-semibold text-green-600 mt-1">
                            <i class="fas fa-arrow-trend-up mr-1"></i>+${bonus.creditsGeneratedPerSecond} crédits/sec
                        </p>
                    </div>
                
                    ${quantityHtml}
                
                    <div class="flex flex-col items-end gap-2 shrink-0 relative z-10">
                        <span class="font-bold text-yellow-600 bg-yellow-50 px-2 py-1 rounded">
                            <i class="fas fa-coins mr-1"></i>${displayPrice}
                        </span>
                        <button onclick="buyBonus(${bonus.id})" 
                                class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded shadow transition-colors text-sm font-bold active:scale-95">
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
                            <i class="fas fa-coins text-yellow-400 mr-1"></i>${displayPrice}
                        </div>
                    `;

                    paletteColorDiv.onclick = () => buyBonus(bonus.id);
                }

                paletteGrid.appendChild(paletteColorDiv);
            }
        });

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

    console.log("Sauvegarde effectuée");

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

// entier avec separateur de milliers : 1234567.8 -> "1 234 567"
function formatCredits(value) {
    return Math.floor(value).toLocaleString('fr-FR');
}

// seul point d'ecriture du compteur, la valeur serveur arrive en texte ("12.5")
function setCredits(value) {
    currentCredits = Number.parseFloat(value) || 0;
    const label = document.getElementById('nb_click');
    if (label) label.innerText = formatCredits(currentCredits);
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
        requestAnimationFrame(updateCounter);
    }
    requestAnimationFrame(updateCounter);
}
