let currentPage = 1;
const cardsPerPage = 6;
let currentFilter = 'all';

window.addEventListener('DOMContentLoaded', function () {
    initFilter();
    initPagination();
    initCards();
    initPopups();
    initWriteButton();
    updateFilterCounts();
    updateUI();
    autoOpenRequiredPopup();
});

function initFilter() {
    const buttons = document.querySelectorAll('.filter_btn');
    buttons.forEach(btn => {
        btn.addEventListener('click', function () {
            buttons.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            currentFilter = this.dataset.filter;
            currentPage = 1;
            updateUI();
        });
    });
}

function initPagination() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    if (prevBtn) {
        prevBtn.addEventListener('click', function () {
            if (currentPage > 1) {
                currentPage--;
                updateUI();
            }
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function () {
            currentPage++;
            updateUI();
        });
    }
}

function initCards() {
    document.querySelectorAll('.notice_card').forEach(card => {
        card.addEventListener('click', function () {
            openDetailModal(this.dataset.noticeId, this.dataset.classId || window.noticeClassId || '');
        });
    });
}

function initPopups() {
    const popupOverlay = document.getElementById('requiredPopupOverlay');
    const closePopupBtn = document.getElementById('closeRequiredPopupBtn');
    const goRequiredNoticeBtn = document.getElementById('goRequiredNoticeBtn');

    if (closePopupBtn) {
        closePopupBtn.addEventListener('click', closeRequiredPopup);
    }

    if (popupOverlay) {
        popupOverlay.addEventListener('click', function (e) {
            if (e.target === popupOverlay) {
                closeRequiredPopup();
            }
        });
    }

    document.querySelectorAll('.required_item').forEach(item => {
        item.addEventListener('click', function () {
            closeRequiredPopup();
            openDetailModal(this.dataset.noticeId, this.dataset.classId || window.noticeClassId || '');
        });
    });

    if (goRequiredNoticeBtn) {
        goRequiredNoticeBtn.addEventListener('click', function () {
            const firstItem = document.querySelector('.required_item');
            if (!firstItem) {
                closeRequiredPopup();
                return;
            }
            closeRequiredPopup();
            openDetailModal(firstItem.dataset.noticeId, firstItem.dataset.classId || window.noticeClassId || '');
        });
    }

    const modalOverlay = document.getElementById('modalOverlay');
    if (modalOverlay) {
        modalOverlay.addEventListener('click', function (e) {
            if (e.target === modalOverlay || e.target.dataset.closeModal === 'true') {
                closeModal();
            }
        });
    }
}

function initWriteButton() {
    const btn = document.getElementById('openWriteBtn');
    if (!btn) return;

    btn.addEventListener('click', function () {
        const classId = this.dataset.classId || window.noticeClassId || '';
        const returnUrl = encodeURIComponent(this.dataset.returnUrl || window.noticeReturnUrl || '');

        fetch(window.noticeContextPath + '/notice/write-modal?classId=' + classId + '&returnUrl=' + returnUrl)
            .then(res => res.text())
            .then(html => showModal(html));
    });
}

function autoOpenRequiredPopup() {
    const popupOverlay = document.getElementById('requiredPopupOverlay');
    const unreadRequiredCount = Number(window.requiredUnreadCount || 0);
    const isStudentOrParent = String(window.isStudentOrParent) === 'true';

    if (popupOverlay && isStudentOrParent && unreadRequiredCount > 0) {
        popupOverlay.classList.add('show');
    }
}

function closeRequiredPopup() {
    const popupOverlay = document.getElementById('requiredPopupOverlay');
    if (popupOverlay) {
        popupOverlay.classList.remove('show');
    }
}

function openDetailModal(noticeId, classId) {
    const returnUrl = encodeURIComponent(window.noticeReturnUrl || '');
    const resolvedClassId = classId || window.noticeClassId || '';

    fetch(
        window.noticeContextPath
        + '/notice/detail-modal?classId=' + resolvedClassId
        + '&noticeId=' + noticeId
        + '&returnUrl=' + returnUrl
    )
        .then(res => res.text())
        .then(html => {
            showModal(html);
            bindDetailModalEvents(resolvedClassId);
        });
}

function bindDetailModalEvents(resolvedClassId) {
    const editBtn = document.getElementById('openEditBtn');
    if (editBtn) {
        editBtn.addEventListener('click', function () {
            const editReturnUrl = encodeURIComponent(this.dataset.returnUrl || window.noticeReturnUrl || '');
            const editClassId = this.dataset.classId || window.noticeClassId || resolvedClassId;

            fetch(
                window.noticeContextPath
                + '/notice/edit-modal?classId=' + editClassId
                + '&noticeId=' + this.dataset.noticeId
                + '&returnUrl=' + editReturnUrl
            )
                .then(res => res.text())
                .then(editHtml => showModal(editHtml));
        });
    }
}

function showModal(html) {
    const overlay = document.getElementById('modalOverlay');
    const modalContent = document.getElementById('modalContent');

    if (!overlay || !modalContent) return;

    modalContent.innerHTML = html;
    overlay.classList.add('show');
}

function closeModal() {
    const overlay = document.getElementById('modalOverlay');
    const modalContent = document.getElementById('modalContent');

    if (!overlay || !modalContent) return;

    overlay.classList.remove('show');
    modalContent.innerHTML = '';
}

function updateUI() {
    const allCards = Array.from(document.querySelectorAll('.notice_card'));
    const filtered = allCards.filter(card => currentFilter === 'all' || card.dataset.category === currentFilter);
    const totalPages = Math.max(1, Math.ceil(filtered.length / cardsPerPage));

    if (currentPage > totalPages) currentPage = totalPages;

    allCards.forEach(card => card.classList.add('hide'));

    const start = (currentPage - 1) * cardsPerPage;
    const end = start + cardsPerPage;
    filtered.slice(start, end).forEach(card => card.classList.remove('hide'));

    renderPageNumbers(totalPages);

    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    if (prevBtn) prevBtn.disabled = currentPage === 1;
    if (nextBtn) nextBtn.disabled = currentPage === totalPages;
}

function renderPageNumbers(totalPages) {
    const pagination = document.querySelector('.pagination');
    if (!pagination) return;

    pagination.querySelectorAll('.page_num').forEach(el => el.remove());

    const nextBtn = document.getElementById('nextBtn');
    if (!nextBtn) return;

    for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'page_num' + (i === currentPage ? ' active' : '');
        btn.textContent = i;

        btn.addEventListener('click', function () {
            currentPage = i;
            updateUI();
        });

        pagination.insertBefore(btn, nextBtn);
    }
}

function updateFilterCounts() {
    const cards = Array.from(document.querySelectorAll('.notice_card'));

    const allBtn = document.querySelector('.filter_btn[data-filter="all"] span');
    const studentBtn = document.querySelector('.filter_btn[data-filter="STUDENT"] span');
    const parentBtn = document.querySelector('.filter_btn[data-filter="PARENT"] span');

    if (allBtn) allBtn.textContent = cards.length;
    if (studentBtn) studentBtn.textContent = cards.filter(c => c.dataset.category === 'STUDENT').length;
    if (parentBtn) parentBtn.textContent = cards.filter(c => c.dataset.category === 'PARENT').length;
}
