function togglePlayer(pseudo) {
    const detail = document.getElementById("detail-" + pseudo);
    const icon = document.getElementById("icon-" + pseudo);

    detail.classList.toggle("hidden");
    icon.classList.toggle("fa-chevron-down");
    icon.classList.toggle("fa-chevron-up");
}