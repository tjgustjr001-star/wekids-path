window.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('childDetailModal');
    const openBtn = document.getElementById('openChildDetailBtn');
    const closeBtn = document.getElementById('closeChildDetailBtn');
    const footerCloseBtn = document.getElementById('childDetailCloseFooterBtn');

    function openModal() {
        if (!modal) return;
        modal.classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        if (!modal) return;
        modal.classList.remove('open');
        document.body.style.overflow = '';
    }

    if (openBtn) openBtn.addEventListener('click', openModal);
    if (closeBtn) closeBtn.addEventListener('click', closeModal);
    if (footerCloseBtn) footerCloseBtn.addEventListener('click', closeModal);

    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) {
                closeModal();
            }
        });
    }
});