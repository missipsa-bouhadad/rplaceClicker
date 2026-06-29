// notification temporaire en haut au centre (remplace les alert)
function showToast(message, type = 'error') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'fixed top-20 left-1/2 -translate-x-1/2 z-[70] flex flex-col gap-2 items-center pointer-events-none';
        document.body.appendChild(container);
    }

    const colors = type === 'success' ? 'bg-green-600' : 'bg-red-600';
    const icon = type === 'success' ? 'fa-circle-check' : 'fa-triangle-exclamation';

    const toast = document.createElement('div');
    toast.className = `${colors} text-white text-sm font-semibold px-4 py-3 rounded-lg shadow-lg flex items-center gap-2 transition-opacity duration-300`;
    toast.innerHTML = `<i class="fas ${icon}"></i>`;
    // le message peut venir du serveur
    toast.appendChild(document.createTextNode(message));
    container.appendChild(toast);

    setTimeout(() => {
        toast.classList.add('opacity-0');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}
