window.addEventListener('DOMContentLoaded', function () {
    const page = document.querySelector('.student-assignment-page');
    if (!page) return;

    const cards = document.querySelectorAll('.assignment-card');
    const modalOverlay = document.getElementById('assignmentModalOverlay');
    const closeBtn = document.getElementById('assignmentModalCloseBtn');
    const closeDetailBtn = document.getElementById('assignmentCloseBtn');
    const goSubmitBtn = document.getElementById('assignmentGoSubmitBtn');
    const backBtn = document.getElementById('assignmentBackBtn');
    const submitBtn = document.getElementById('assignmentSubmitBtn');
    const submitBtnText = document.getElementById('assignmentSubmitBtnText');
    const downloadBtn = document.getElementById('assignmentDownloadBtn');

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
    const modalRemainCount = document.getElementById('assignmentModalRemainCount');

    const submittedBox = document.getElementById('assignmentSubmittedBox');
    const submittedFileBox = document.getElementById('assignmentSubmittedFileBox');
    const submittedFileName = document.getElementById('assignmentSubmittedFileName');
    const submittedFileSize = document.getElementById('assignmentSubmittedFileSize');
    const submittedTextBox = document.getElementById('assignmentSubmittedTextBox');
    const submittedText = document.getElementById('assignmentSubmittedText');
    const submittedAtText = document.getElementById('assignmentSubmittedAtText');

    const fileInput = document.getElementById('assignmentFileInput');
    const fileUploadWrap = document.getElementById('assignmentFileUploadWrap');
    const fileDropzone = document.getElementById('assignmentFileDropzone');
    const dropzoneDesc = document.getElementById('assignmentDropzoneDesc');
    const selectedFileBox = document.getElementById('assignmentSelectedFileBox');
    const selectedFileName = document.getElementById('assignmentSelectedFileName');
    const selectedFileSize = document.getElementById('assignmentSelectedFileSize');
    const removeFileBtn = document.getElementById('assignmentRemoveFileBtn');
    const textareaWrap = document.getElementById('assignmentTextareaWrap');
    const submissionTextarea = document.getElementById('assignmentSubmissionTextarea');

    const toastContainer = document.getElementById('assignmentToastContainer');
    const csrfName = document.getElementById('assignmentCsrfName').value;
    const csrfToken = document.getElementById('assignmentCsrfToken').value;

    let selectedCard = null;
    let selectedFile = null;
    let isSubmitting = false;
    let activeDetail = null;

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

    function formatSize(size) {
        const value = Number(size || 0);
        if (!value) return '0 KB';
        if (value >= 1024 * 1024) return (value / (1024 * 1024)).toFixed(1) + ' MB';
        return (value / 1024).toFixed(1) + ' KB';
    }

    function setStatusClass(el, status) {
        el.className = el.className.split(' ')[0];
        if (status === '진행') el.classList.add('progress');
        else if (status === '마감임박') el.classList.add('urgent');
        else if (status === '제출완료' || status === '확인완료') el.classList.add('done');
        else if (status === '늦은제출') el.classList.add('late');
        else if (status === '반려') el.classList.add('reject');
        else el.classList.add('default');
    }

    function resetModal() {
        activeDetail = null;
        selectedFile = null;
        isSubmitting = false;
        fileInput.value = '';
        submissionTextarea.value = '';
        selectedFileBox.classList.remove('show');
        fileDropzone.style.display = 'block';
        fileUploadWrap.style.display = 'block';
        textareaWrap.style.display = 'block';
        downloadBtn.style.display = 'none';

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
        goSubmitBtn.style.display = 'inline-flex';
    }

    function showDetailView() {
        detailView.classList.remove('hide');
        submitView.classList.remove('show');
        detailActions.classList.remove('hide');
        submitActions.classList.remove('show');
    }

    function showSubmitView() {
        if (!activeDetail || activeDetail.canSubmit !== true) {
            showToast('현재는 제출 또는 수정이 불가능합니다.', 'error');
            return;
        }
        detailView.classList.add('hide');
        submitView.classList.add('show');
        detailActions.classList.add('hide');
        submitActions.classList.add('show');
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

    function applySubmitFormat(format, attachedFile) {
        const normalized = String(format || '').toUpperCase();
        const isText = normalized.indexOf('TEXT') > -1 || normalized.indexOf('텍스트') > -1;
        const isImage = normalized.indexOf('IMAGE') > -1 || normalized.indexOf('이미지') > -1;
        const isFile = normalized.indexOf('FILE') > -1 || normalized.indexOf('파일') > -1 || isImage;

        fileUploadWrap.style.display = isFile ? 'block' : 'none';
        textareaWrap.style.display = 'block';

        if (isText) {
            submissionTextarea.placeholder = '과제에 대한 설명이나 내용을 입력해주세요...';
        } else if (isImage) {
            submissionTextarea.placeholder = '사진과 함께 추가 설명을 입력해주세요...';
        } else if (isFile) {
            submissionTextarea.placeholder = '첨부파일과 함께 추가 설명을 입력해주세요...';
        } else {
            submissionTextarea.placeholder = '과제에 대한 설명이나 내용을 입력해주세요...';
        }

        if (isImage) {
            fileInput.setAttribute('accept', '.jpg,.jpeg,.png,.webp');
            dropzoneDesc.textContent = '지원 형식: JPG, PNG, WEBP (최대 50MB)';
        } else {
            fileInput.setAttribute('accept', '.hwp,.pdf,.jpg,.jpeg,.png,.webp,.doc,.docx');
            dropzoneDesc.textContent = '지원 형식: HWP, PDF, JPG, PNG, DOCX (최대 50MB)';
        }

        if (isFile && attachedFile) {
            selectedFileName.textContent = attachedFile;
        }
    }

    function normalizeStatus(assignment) {
        if (!assignment) return '';
        const feedback = String(assignment.feedback || '').trim();
        return assignment.status || '';
    }

    function syncCardDataset(card, assignment) {
        const normalizedStatus = normalizeStatus(assignment);
        card.dataset.status = normalizedStatus;
        card.dataset.deadline = assignment.deadline || '';
        card.dataset.deadlineValue = assignment.deadlineValue || '';
        card.dataset.type = assignment.submitFormatLabel || '';
        card.dataset.submitFormat = assignment.submitFormat || '';
        card.dataset.submitted = String(assignment.submitted === true);
        card.dataset.submittedAt = assignment.submittedAt || '';
        card.dataset.feedback = assignment.feedback || '';
        card.dataset.content = assignment.content || '';
        card.dataset.mySubmission = assignment.mySubmission || '';
        card.dataset.attachedFile = assignment.attachedFile || '';
        card.dataset.fileSize = assignment.fileSize || 0;
        card.dataset.canSubmit = String(assignment.canSubmit === true);
        card.dataset.canResubmit = String(assignment.canResubmit === true);
        card.dataset.canDownload = String(assignment.canDownload === true);
        card.dataset.expired = String(assignment.expired === true);
        card.dataset.submitCount = assignment.submitCount || 0;
        card.dataset.maxEditCount = assignment.maxEditCount || 0;
        card.dataset.remainingEditCount = assignment.remainingEditCount || 0;
    }

    function syncCardUI(card, assignment) {
        syncCardDataset(card, assignment);

        const normalizedStatus = normalizeStatus(assignment);
        assignment.status = normalizedStatus;

        const statusBadge = card.querySelector('.assignment-status-badge');
        const typeBadge = card.querySelector('.assignment-type-badge');
        const feedbackPreview = card.querySelector('.assignment-feedback-preview');
        const submitState = card.querySelector('.assignment-submit-state');
        const openBtn = card.querySelector('.assignment-open-btn');

        statusBadge.textContent = assignment.status;
        setStatusClass(statusBadge, assignment.status);
        typeBadge.textContent = assignment.submitFormatLabel || '';

        if (assignment.feedback) {
            if (feedbackPreview) {
                feedbackPreview.querySelector('span:last-child').textContent = assignment.feedback;
            } else {
                const preview = document.createElement('div');
                preview.className = 'assignment-feedback-preview';
                preview.innerHTML = '<span class="assignment-alert-icon"></span><span>' + assignment.feedback + '</span>';
                card.querySelector('.assignment-card-top').appendChild(preview);
            }
        } else if (feedbackPreview) {
            feedbackPreview.remove();
        }

        if (assignment.status === '반려') {
            submitState.className = 'assignment-submit-state reject';
            submitState.innerHTML = '<span class="assignment-alert-icon"></span><span>반려' +
                (assignment.submittedAt ? ' (' + assignment.submittedAt + ')' : '') + '</span>';
        } else if (assignment.submitted) {
            submitState.className = 'assignment-submit-state done';
            submitState.innerHTML = '<span class="assignment-check-icon"></span><span>' + (assignment.status === '확인완료' ? '확인 완료' : '제출 완료') +
                (assignment.submittedAt ? ' (' + assignment.submittedAt + ')' : '') + '</span>';
        } else {
            submitState.className = 'assignment-submit-state';
            submitState.innerHTML = '<span class="assignment-fileplus-icon"></span><span>미제출</span>';
        }

        if (assignment.status === '반려') {
            openBtn.textContent = '수정하여 재제출';
            openBtn.className = 'assignment-open-btn primary';
            openBtn.disabled = false;
        } else if (!assignment.submitted) {
            openBtn.textContent = assignment.expired ? '늦은 제출하기' : '제출하기';
            openBtn.className = 'assignment-open-btn primary';
            openBtn.disabled = false;
        } else if (!assignment.canSubmit && assignment.status === '확인완료') {
            openBtn.textContent = '제출 내용 보기';
            openBtn.className = 'assignment-open-btn secondary';
            openBtn.disabled = false;
        } else {
            openBtn.textContent = '제출 내용 보기';
            openBtn.className = 'assignment-open-btn ' + (assignment.canSubmit ? 'primary' : 'secondary');
            openBtn.disabled = false;
        }
    }

    function fillModal(detail) {
        detail.status = normalizeStatus(detail);
        activeDetail = detail;
        modalSubject.textContent = detail.subject || '';
        modalType.textContent = detail.submitFormatLabel || '';
        modalStatus.textContent = detail.status || '';
        modalTitle.textContent = detail.title || '';
        modalContent.textContent = detail.content || '';
        modalDeadline.textContent = detail.deadline || '';
        modalFeedback.textContent = detail.feedback || '';
        modalRemainCount.textContent = (detail.remainingEditCount || 0) + '회';

        setStatusClass(modalStatus, detail.status || '');

        const feedbackTitle = document.getElementById('assignmentFeedbackTitle');
        if (detail.feedback) {
            modalRejectBox.classList.add('show');
            if (feedbackTitle) feedbackTitle.textContent = detail.status === '반려' ? '반려 사유' : '선생님 피드백';
        }

        if (detail.submitted) {
            submittedBox.classList.add('show');
            if (detail.attachedFile) {
                submittedFileBox.classList.add('show');
                submittedFileName.textContent = detail.attachedFile;
                submittedFileSize.textContent = formatSize(detail.fileSize);
                if (detail.canDownload) {
                    downloadBtn.style.display = 'inline-flex';
                }
            }
            if (detail.mySubmission) {
                submittedTextBox.classList.add('show');
                submittedText.textContent = detail.mySubmission;
            }
            submittedAtText.textContent = (detail.status === '반려' ? '반려된 제출입니다.' : (detail.status === '확인완료' ? '확인 완료되었습니다.' : (detail.status === '늦은제출' ? '늦은 제출로 접수되었습니다.' : '제출이 완료되었습니다.'))) + (detail.submittedAt ? ' (' + detail.submittedAt + ')' : '');
        }

        submissionTextarea.value = detail.mySubmission || '';
        applySubmitFormat(detail.submitFormat, detail.attachedFile || '');

        goSubmitBtn.style.display = detail.canSubmit && detail.status !== '확인완료' ? 'inline-flex' : 'none';
    }

    function openModal(card, openSubmit) {
        resetModal();
        selectedCard = card;

        fetch(card.dataset.detailUrl, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(function (response) {
                if (!response.ok) {
                    throw new Error('과제 정보를 불러오지 못했습니다.');
                }
                return response.json();
            })
            .then(function (detail) {
                fillModal(detail);
                modalOverlay.classList.add('open');
                document.body.style.overflow = 'hidden';
                if (openSubmit && detail.canSubmit) {
                    showSubmitView();
                } else {
                    showDetailView();
                }
            })
            .catch(function (error) {
                showToast(error.message || '과제 정보를 불러오지 못했습니다.', 'error');
            });
    }

    function closeModal() {
        modalOverlay.classList.remove('open');
        document.body.style.overflow = '';
        selectedCard = null;
        resetModal();
    }

    cards.forEach(function (card) {
        card.addEventListener('click', function () {
            openModal(card, false);
        });

        const btn = card.querySelector('.assignment-open-btn');
        if (btn) {
            btn.addEventListener('click', function (e) {
                e.stopPropagation();
                if (btn.disabled) return;

                const submitted = card.dataset.submitted === 'true';
                const status = card.dataset.status || '';
                const canSubmit = card.dataset.canSubmit === 'true';
                const expired = card.dataset.expired === 'true';

                if (!submitted && canSubmit) {
                    openModal(card, true);
                } else if (status === '반려' && canSubmit) {
                    openModal(card, true);
                } else {
                    openModal(card, false);
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

    downloadBtn.addEventListener('click', function () {
        if (selectedCard) {
            window.location.href = selectedCard.dataset.downloadUrl;
        }
    });

    submitBtn.addEventListener('click', function () {
        if (!selectedCard || !activeDetail || isSubmitting) return;

        const type = String(activeDetail.submitFormat || '').toUpperCase();
        const needsText = type.indexOf('TEXT') > -1 || type.indexOf('텍스트') > -1;
        const needsFile = type.indexOf('FILE') > -1 || type.indexOf('파일') > -1 || type.indexOf('IMAGE') > -1 || type.indexOf('이미지') > -1;
        const hasOldFile = !!(activeDetail.attachedFile);

        if (needsText && !submissionTextarea.value.trim()) {
            showToast('내용을 입력해주세요.', 'error');
            return;
        }

        if (needsFile && !selectedFile && !hasOldFile) {
            showToast('파일을 첨부해주세요.', 'error');
            return;
        }

        isSubmitting = true;
        submitBtn.disabled = true;
        submitBtnText.textContent = '제출 중...';

        const formData = new FormData();
        formData.append('content', submissionTextarea.value || '');
        if (selectedFile) {
            formData.append('uploadFile', selectedFile);
        }
        formData.append(csrfName, csrfToken);

        fetch(selectedCard.dataset.submitUrl, {
            method: 'POST',
            body: formData,
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(function (response) {
                return response.json().then(function (result) {
                    if (!response.ok || result.success === false) {
                        throw new Error(result.message || '제출에 실패했습니다.');
                    }
                    return result;
                });
            })
            .then(function (result) {
                syncCardUI(selectedCard, result.assignment);
                showToast('과제가 성공적으로 제출되었습니다.', 'success');
                closeModal();
            })
            .catch(function (error) {
                showToast(error.message || '제출에 실패했습니다.', 'error');
                submitBtn.disabled = false;
                submitBtnText.textContent = '과제 제출하기';
                isSubmitting = false;
            });
    });
});
