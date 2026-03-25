window.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('classSearchInput');
    const cards = document.querySelectorAll('.class-card');

    if (!input || !cards.length) return;

    input.addEventListener('input', function () {
        const keyword = input.value.trim().toLowerCase();

        cards.forEach(function (card) {
            const className = (card.getAttribute('data-class-name') || '').toLowerCase();

            if (!keyword || className.indexOf(keyword) > -1) {
                card.classList.remove('is-hidden');
            } else {
                card.classList.add('is-hidden');
            }
        });
    });
});