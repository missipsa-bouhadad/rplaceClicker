document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('credit_btn');
    const label = document.getElementById('nb_click');

    btn.addEventListener('click', () => {
        fetch('click', { method: 'POST' })
            .then(res => res.text())
            .then(credit => label.innerText = credit);
    });
});