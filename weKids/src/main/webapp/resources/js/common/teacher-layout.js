window.addEventListener('DOMContentLoaded', function () {
    const notiToggleBtn = document.getElementById('notiToggleBtn');
    const notificationDropdown = document.getElementById('notificationDropdown');

    if (notiToggleBtn && notificationDropdown) {
        notiToggleBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            notificationDropdown.classList.toggle('open');
        });

        document.addEventListener('click', function (e) {
            if (!notificationDropdown.contains(e.target) && !notiToggleBtn.contains(e.target)) {
                notificationDropdown.classList.remove('open');
            }
        });
    }
});
