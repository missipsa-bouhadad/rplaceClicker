function togglePlayer(pseudo) {
    const detail = document.getElementById("detail-" + pseudo);
    const icon = document.getElementById("icon-" + pseudo);

    detail.classList.toggle("hidden");
    detail.classList.toggle("flex");
    icon.classList.toggle("fa-chevron-down");
    icon.classList.toggle("fa-chevron-up");
}

// passer par style evite d'injecter du css avec une couleur invalide
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-color]').forEach(el => el.style.backgroundColor = el.dataset.color);
    document.querySelectorAll('[data-width]').forEach(el => el.style.width = el.dataset.width + '%');
});
