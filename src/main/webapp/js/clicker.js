document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('credit_btn');
    const label = document.getElementById('nb_click');

    btn.addEventListener('click', () => {
        fetch('click', { method: 'POST' })
            .then(res => res.text())
            .then(credit => label.innerText = credit);
    });
});

async function loadAllBonuses() {
    // Utilisation de l'IA pour l'affichage stylisé des bonus

    const container = document.getElementById('bonuses-container');

    if (!container) return;

    try {
        const response = await fetch(contextPath + '/bonuses');

        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des bonus');
        }

        const bonuses = await response.json();

        container.innerHTML = '';

        if (bonuses.length === 0) {
            container.innerHTML = '<p class="text-gray-500 italic text-center">Aucun bonus disponible pour le moment.</p>';
            return;
        }

        bonuses.forEach(bonus => {
            const bonusDiv = document.createElement('div');
            bonusDiv.className = 'bg-white p-4 rounded-lg shadow-sm border border-gray-200 flex justify-between items-center hover:shadow-md transition-shadow';

            const displayPrice = bonus.currentPrice ? bonus.currentPrice : bonus.cost;

            bonusDiv.innerHTML = `
                <div class="flex-1 pr-4">
                    <h3 class="font-bold text-lg text-gray-800">${bonus.name}</h3>
                    <p class="text-sm text-gray-600 line-clamp-2">${bonus.description}</p>
                    <p class="text-xs font-semibold text-green-600 mt-1">
                        <i class="fas fa-arrow-trend-up mr-1"></i>+${bonus.creditsGeneratedPerSecond} crédits/sec
                    </p>
                </div>
                <div class="flex flex-col items-end gap-2 shrink-0">
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
        });

    } catch (error) {
        console.error("Erreur de chargement des bonus:", error);
        container.innerHTML = '<p class="text-red-500 text-center text-sm"><i class="fas fa-triangle-exclamation mr-1"></i> Impossible de charger la boutique.</p>';
    }
}

async function buyBonus(bonusId) {
    if (!isConnected) {
        alert("Vous devez être connecté pour acheter un bonus !");
        return;
    }

    const saveResponse = await fetch('save', { method: 'POST' });

    if (!saveResponse.ok) {
        alert("Erreur lors de la sauvegarde avant l'achat. Veuillez réessayer.");
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
            const newCredits = await response.text();
            const scoreElement = document.getElementById('nb_click');
            if (scoreElement) {
                scoreElement.innerText = newCredits;
            }
            await loadAllBonuses();
        } else {
            const errorMsg = await response.text();
            alert("Achat impossible : " + errorMsg);
        }
    } catch (error) {
        console.error("Erreur lors de l'achat :", error);
    }
}