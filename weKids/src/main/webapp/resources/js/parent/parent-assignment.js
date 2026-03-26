window.addEventListener('DOMContentLoaded', function () {
    const childSelect = document.getElementById('parentAssignmentChildSelect');
    const panels = document.querySelectorAll('.assignment-child-panel');

    const modalOverlay = document.getElementById('parentAssignmentModalOverlay');
    const modalCloseBtn = document.getElementById('parentAssignmentModalCloseBtn');
    const modalFooterCloseBtn = document.getElementById('parentAssignmentCloseFooterBtn');

    const modalSubject = document.getElementById('parentAssignmentModalSubject');
    const modalType = document.getElementById('parentAssignmentModalType');
    const modalStatus = document.getElementById('parentAssignmentModalStatus');
    const modalTitle = document.getElementById('parentAssignmentModalTitle');
    const modalContent = document.getElementById('parentAssignmentModalContent');
    const modalDeadline = document.getElementById('parentAssignmentModalDeadline');
    const modalFeedback = document.getElementById('parentAssignmentModalFeedback');

    const rejectBox = document.getElementById('parentAssignmentRejectBox');
    const submittedBox = document.getElementById('parentAssignmentSubmittedBox');
    const submittedFileBox = document.getElementById('parentAssignmentSubmittedFileBox');
    const submittedTextBox = document.getElementById('parentAssignmentSubmittedTextBox');
    const submittedText = document.getElementById('parentAssignmentSubmittedText');
    const submittedAtText = document.getElementById('parentAssignmentSubmittedAtText');

    function setStatusClass(el, status) {
        el.className = el.className.split(' ')[0];
        if (status === '진행') el.classList.add('progress');
        else if (status === '마감임박') el.classList.add('urgent');
        else if (status === '제출완료') el.classList.add('done');
        else if (status === '반려') el.classList.add('reject');
        else el.classList.add('default');
    }

    function openModal(card) {
        const data = card.dataset;
        const submitted = data.submitted === 'true';

        modalSubject.textContent = data.subject || '';
        modalType.textContent = data.type || '';
        modalStatus.textContent = data.status || '';
        modalTitle.textContent = data.title || '';
        modalContent.textContent = data.content || '과제 상세 내용입니다.';
        modalDeadline.textContent = data.deadline || '';
        modalFeedback.textContent = data.feedback || '';

        modalStatus.className = 'parent-assignment-modal-status';
        if (data.status === '진행') modalStatus.classList.add('progress');
        else if (data.status === '마감임박') modalStatus.classList.add('urgent');
        else if (data.status === '제출완료') modalStatus.classList.add('done');
        else if (data.status === '반려') modalStatus.classList.add('reject');

        rejectBox.classList.remove('show');
        submittedBox.classList.remove('show');
        submittedFileBox.classList.remove('show');
        submittedTextBox.classList.remove('show');

        if (data.status === '반려' && data.feedback) {
            rejectBox.classList.add('show');
        }

        if (submitted) {
            submittedBox.classList.add('show');

            if ((data.type || '').indexOf('파일') > -1) {
                submittedFileBox.classList.add('show');
            } else {
                submittedTextBox.classList.add('show');
                submittedText.textContent = data.mySubmission || '제출된 텍스트 내용입니다.';
            }

            submittedAtText.textContent = '제출 완료' + (data.submittedAt ? ' (' + data.submittedAt + ')' : '');
        }

        modalOverlay.classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        modalOverlay.classList.remove('open');
        document.body.style.overflow = '';
    }

    if (childSelect) {
        childSelect.addEventListener('change', function () {
            const selectedId = childSelect.value;

            panels.forEach(function (panel) {
                panel.classList.toggle('active', panel.dataset.childId === selectedId);
            });
        });
    }

    document.querySelectorAll('.parent-assignment-card').forEach(function (card) {
        card.addEventListener('click', function () {
            openModal(card);
        });

        const detailBtn = card.querySelector('.parent-assignment-detail-btn');
        if (detailBtn) {
            detailBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                openModal(card);
            });
        }
    });

    if (modalCloseBtn) modalCloseBtn.addEventListener('click', closeModal);
    if (modalFooterCloseBtn) modalFooterCloseBtn.addEventListener('click', closeModal);

    if (modalOverlay) {
        modalOverlay.addEventListener('click', function (e) {
            if (e.target === modalOverlay) {
                closeModal();
            }
        });
    }
});