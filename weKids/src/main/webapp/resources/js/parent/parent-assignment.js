document.addEventListener('DOMContentLoaded', function () {
    const page = document.querySelector('.parent-assignment-page');
    if (!page) return;

    const contextPath = page.dataset.contextPath || '';
    const classId = page.dataset.classId;

    const childSelect = document.getElementById('parentAssignmentChildSelect');
    const modalOverlay = document.getElementById('parentAssignmentModalOverlay');
    const modalCloseBtn = document.getElementById('parentAssignmentModalCloseBtn');
    const modalFooterCloseBtn = document.getElementById('parentAssignmentModalCloseFooterBtn');

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
    const downloadBtn = document.getElementById('parentAssignmentDownloadBtn');
    const fileName = document.getElementById('parentAssignmentFileName');
    const fileSize = document.getElementById('parentAssignmentFileSize');

    function escapeHtml(value) {
        return (value || '').replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&').replace(/&#039;/g, "'").replace(/&quot;/g, '"');
    }

    function formatFileSize(bytes) {
        const size = Number(bytes || 0);
        if (!size) return '';
        if (size >= 1024 * 1024) return (size / (1024 * 1024)).toFixed(1) + ' MB';
        if (size >= 1024) return Math.round(size / 1024) + ' KB';
        return size + ' B';
    }

    function setStatusClass(el, status) {
        el.className = 'parent-assignment-modal-status';
        if (status === '진행') el.classList.add('progress');
        else if (status === '마감임박') el.classList.add('urgent');
        else if (status === '제출완료' || status === '확인완료') el.classList.add('done');
        else if (status === '늦은제출') el.classList.add('late');
        else if (status === '반려') el.classList.add('reject');
        else el.classList.add('default');
    }

    function openModal(card) {
        const data = card.dataset;
        const status = escapeHtml(data.status);
        const submitted = data.submitted === 'true';
        const attachedFile = escapeHtml(data.attachedFile);
        const type = escapeHtml(data.type);

        modalSubject.textContent = escapeHtml(data.subject);
        modalType.textContent = type;
        modalStatus.textContent = status;
        modalTitle.textContent = escapeHtml(data.title);
        modalContent.textContent = escapeHtml(data.content) || '과제 상세 내용이 없습니다.';
        modalDeadline.textContent = escapeHtml(data.deadline) || '마감일 없음';
        modalFeedback.textContent = escapeHtml(data.feedback);
        setStatusClass(modalStatus, status);

        const feedbackTitle = document.getElementById('parentAssignmentFeedbackTitle');
        rejectBox.style.display = modalFeedback.textContent.trim() ? 'block' : 'none';
        if (feedbackTitle) feedbackTitle.textContent = status === '반려' ? '반려 사유' : '선생님 피드백';
        submittedBox.style.display = submitted ? 'block' : 'none';
        submittedFileBox.style.display = 'none';
        submittedTextBox.style.display = 'none';

        if (submitted) {
            submittedAtText.textContent = (status === '확인완료' ? '확인 완료' : (status === '늦은제출' ? '늦은 제출 완료' : '제출 완료')) + (data.submittedAt ? ' (' + escapeHtml(data.submittedAt) + ')' : '');

            if (attachedFile) {
                submittedFileBox.style.display = 'flex';
                fileName.textContent = attachedFile;
                fileSize.textContent = formatFileSize(data.fileSize);
                downloadBtn.href = data.downloadUrl || '#';
            }

            if (escapeHtml(data.mySubmission)) {
                submittedTextBox.style.display = 'block';
                submittedText.textContent = escapeHtml(data.mySubmission);
            } else if (!attachedFile) {
                submittedTextBox.style.display = 'block';
                submittedText.textContent = '제출된 내용이 없습니다.';
            }
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
            const targetUrl = contextPath + '/parent/classes/' + classId + '/assignments?childId=' + encodeURIComponent(selectedId);
            window.location.href = targetUrl;
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
