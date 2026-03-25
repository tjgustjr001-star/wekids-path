window.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.assignment-card');
    const modalOverlay = document.getElementById('assignmentModalOverlay');
    const closeBtn = document.getElementById('assignmentModalCloseBtn');
    const closeDetailBtn = document.getElementById('assignmentCloseBtn');
    const goSubmitBtn = document.getElementById('assignmentGoSubmitBtn');
    const backBtn = document.getElementById('assignmentBackBtn');
    const submitBtn = document.getElementById('assignmentSubmitBtn');
    const submitBtnText = document.getElementById('assignmentSubmitBtnText');

    const detailView = document.getElementById('assignmentDetailView');
    const submitView = document.getElementById('assignmentSubmitView');
    const detailActions = document.getElementById('assignmentDetailActions');
    const submitActions = document.getElementById('assignmentSubmitActions');

    const modalSubject = document.getElementById('assignmentModalSubject');
    const modalType = document.getElementById('assignmentModalType');
    const modalStatus = document.getElementById('assignmentModalStatus');
    const modalTitle = document.getElementById('assignmentModalTitle');
    const modalContent = document.getElementById('assignmentModalContent');
    const modalDeadline = document.getElementById('assignmentModalDeadline');
    const modalFeedback = document.getElementById('assignmentModalFeedback');
    const modalRejectBox = document.getElementById('assignmentRejectBox');

    const submittedBox = document.getElementById('assignmentSubmittedBox');
    const submittedFileBox = document.getElementById('assignmentSubmittedFileBox');
    const submittedFileName = document.getElementById('assignmentSubmittedFileName');
    const submittedTextBox = document.getElementById('assignmentSubmittedTextBox');
    const submittedText = document.getElementById('assignmentSubmittedText');
    const submittedAtText = document.getElementById('assignmentSubmittedAtText');

    const fileInput = document.getElementById('assignmentFileInput');
    const fileDropzone = document.getElementById('assignmentFileDropzone');
    const selectedFileBox = document.getElementById('assignmentSelectedFileBox');
    const selectedFileName = document.getElementById('assignmentSelectedFileName');
    const selectedFileSize = document.getElementById('assignmentSelectedFileSize');
    const removeFileBtn = document.getElementById('assignmentRemoveFileBtn');
    const submissionTextarea = document.getElementById('assignmentSubmissionTextarea');

    const toastContainer = document.getElementById('assignmentToastContainer');

    let selectedCard = null;
    let selectedFile = null;
    let isSubmitting = false;

    function showToast(message, type) {
        const toast = document.createElement('div');
        toast.className = 'assignment-toast ' + type;
        toast.textContent = message;
        toastContainer.appendChild(toast);

        setTimeout(function () {
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(10px)';
        }, 2400);

        setTimeout(function () {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 2800);
    }

    function resetModal() {
        selectedFile = null;
        isSubmitting = false;
        submissionTextarea.value = '';
        selectedFileBox.classList.remove('show');
        fileDropzone.style.display = 'block';

        modalRejectBox.classList.remove('show');
        submittedBox.classList.remove('show');
        submittedFileBox.classList.remove('show');
        submittedTextBox.classList.remove('show');

        detailView.classList.remove('hide');
        submitView.classList.remove('show');
        detailActions.classList.remove('hide');
        submitActions.classList.remove('show');

        submitBtn.disabled = false;
        submitBtnText.textContent = '과제 제출하기';
    }

    function setStatusClass(el, status) {
        el.className = el.className.split(' ')[0];
        if (status === '진행') el.classList.add('progress');
        else if (status === '마감임박') el.classList.add('urgent');
        else if (status === '제출완료') el.classList.add('done');
        else if (status === '반려') el.classList.add('reject');
        else el.classList.add('default');
    }

    function openDetail(card, openSubmit) {
        resetModal();
        selectedCard = card;

        const data = card.dataset;
        const submitted = data.submitted === 'true';

        modalSubject.textContent = data.subject || '';
        modalType.textContent = data.type || '';
        modalTitle.textContent = data.title || '';
        modalContent.textContent = data.content || '';
        modalDeadline.textContent = data.deadline || '';
        modalFeedback.textContent = data.feedback || '';

        modalStatus.textContent = data.status || '';
        modalStatus.className = 'assignment-modal-status';
        if (data.status === '진행') modalStatus.classList.add('progress');
        else if (data.status === '마감임박') modalStatus.classList.add('urgent');
        else if (data.status === '제출완료') modalStatus.classList.add('done');
        else if (data.status === '반려') modalStatus.classList.add('reject');

        if (data.status === '반려' && data.feedback) {
            modalRejectBox.classList.add('show');
        }

        if (submitted) {
            submittedBox.classList.add('show');

            if (data.attachedFile) {
                submittedFileBox.classList.add('show');
                submittedFileName.textContent = data.attachedFile;
            }

            if (data.mySubmission) {
                submittedTextBox.classList.add('show');
                submittedText.textContent = data.mySubmission;
            }

            submittedAtText.textContent = '제출이 완료되었습니다.' + (data.submittedAt ? ' (' + data.submittedAt + ')' : '');
        }

        if (!submitted || data.status === '반려') {
            goSubmitBtn.style.display = 'inline-flex';
            submissionTextarea.value = data.mySubmission || '';
        } else {
            goSubmitBtn.style.display = 'none';
        }

        if (openSubmit) {
            showSubmitView();
        } else {
            showDetailView();
        }

        modalOverlay.classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        modalOverlay.classList.remove('open');
        document.body.style.overflow = '';
        selectedCard = null;
        resetModal();
    }

    function showDetailView() {
        detailView.classList.remove('hide');
        submitView.classList.remove('show');
        detailActions.classList.remove('hide');
        submitActions.classList.remove('show');
    }

    function showSubmitView() {
        detailView.classList.add('hide');
        submitView.classList.add('show');
        detailActions.classList.add('hide');
        submitActions.classList.add('show');
    }

    function formatSize(size) {
        return (size / 1024).toFixed(1) + ' KB';
    }

    function updateSelectedFileUI(file) {
        if (!file) {
            selectedFileBox.classList.remove('show');
            fileDropzone.style.display = 'block';
            return;
        }

        selectedFileName.textContent = file.name;
        selectedFileSize.textContent = formatSize(file.size);
        selectedFileBox.classList.add('show');
        fileDropzone.style.display = 'none';
    }

    function syncCardAfterSubmit(card) {
        const statusBadge = card.querySelector('.assignment-status-badge');
        const submitState = card.querySelector('.assignment-submit-state');
        const openBtn = card.querySelector('.assignment-open-btn');
        const feedbackPreview = card.querySelector('.assignment-feedback-preview');

        card.dataset.status = '제출완료';
        card.dataset.submitted = 'true';
        card.dataset.submittedAt = '방금 전';
        card.dataset.mySubmission = submissionTextarea.value || '';
        if (selectedFile) {
            card.dataset.attachedFile = selectedFile.name;
        }

        statusBadge.textContent = '제출완료';
        statusBadge.className = 'assignment-status-badge done';

        submitState.className = 'assignment-submit-state done';
        submitState.innerHTML = '<span class="assignment-check-icon"></span><span>제출 완료 (방금 전)</span>';

        openBtn.className = 'assignment-open-btn secondary';
        openBtn.textContent = '제출 내용 보기';

        if (feedbackPreview) {
            feedbackPreview.remove();
        }
    }

    cards.forEach(function (card) {
        card.addEventListener('click', function () {
            openDetail(card, false);
        });

        const btn = card.querySelector('.assignment-open-btn');
        if (btn) {
            btn.addEventListener('click', function (e) {
                e.stopPropagation();

                const submitted = card.dataset.submitted === 'true';
                const status = card.dataset.status || '';

                if (!submitted || status === '반려') {
                    openDetail(card, true);
                } else {
                    openDetail(card, false);
                }
            });
        }
    });

    closeBtn.addEventListener('click', closeModal);
    closeDetailBtn.addEventListener('click', closeModal);

    modalOverlay.addEventListener('click', function (e) {
        if (e.target === modalOverlay) {
            closeModal();
        }
    });

    goSubmitBtn.addEventListener('click', showSubmitView);
    backBtn.addEventListener('click', showDetailView);

    fileInput.addEventListener('change', function () {
        if (fileInput.files && fileInput.files.length > 0) {
            selectedFile = fileInput.files[0];
            updateSelectedFileUI(selectedFile);
        }
    });

    removeFileBtn.addEventListener('click', function () {
        selectedFile = null;
        fileInput.value = '';
        updateSelectedFileUI(null);
    });

    submitBtn.addEventListener('click', function () {
        if (!selectedCard || isSubmitting) return;

        const type = selectedCard.dataset.type || '';
        const attachedFile = selectedCard.dataset.attachedFile || '';

        if (type.indexOf('텍스트') > -1 && !submissionTextarea.value.trim()) {
            showToast('내용을 입력해주세요.', 'error');
            return;
        }

        if (type.indexOf('파일') > -1 && !selectedFile && !attachedFile) {
            showToast('파일을 첨부해주세요.', 'error');
            return;
        }

        isSubmitting = true;
        submitBtn.disabled = true;
        submitBtnText.textContent = '제출 중...';

        setTimeout(function () {
            syncCardAfterSubmit(selectedCard);
            isSubmitting = false;
            showToast('과제가 성공적으로 제출되었습니다.', 'success');
            closeModal();
        }, 800);
    });
});