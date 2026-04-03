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
    const feedbackPreview = document.getElementById('parentAssignmentFeedbackPreview');

    const rejectBox = document.getElementById('parentAssignmentRejectBox');
    const submittedBox = document.getElementById('parentAssignmentSubmittedBox');
    const submittedFileBox = document.getElementById('parentAssignmentSubmittedFileBox');
    const submittedTextBox = document.getElementById('parentAssignmentSubmittedTextBox');
    const submittedText = document.getElementById('parentAssignmentSubmittedText');
    const submittedAtText = document.getElementById('parentAssignmentSubmittedAtText');
    const downloadBtn = document.getElementById('parentAssignmentDownloadBtn');
    const fileIcon = document.getElementById('parentAssignmentFileIcon');
    const fileName = document.getElementById('parentAssignmentFileName');
    const fileSize = document.getElementById('parentAssignmentFileSize');

    function decodeHtml(value) {
        if (value == null) return '';
        const textarea = document.createElement('textarea');
        textarea.innerHTML = String(value);
        return textarea.value;
    }

    function formatFileSize(bytes) {
        const size = Number(bytes || 0);
        if (!size) return '';
        if (size >= 1024 * 1024) return (size / (1024 * 1024)).toFixed(1) + ' MB';
        if (size >= 1024) return Math.round(size / 1024) + ' KB';
        return size + ' B';
    }

    function fileExtLabel(fileName) {
        const normalized = String(fileName || '').trim().toLowerCase();
        const ext = normalized.indexOf('.') > -1 ? normalized.split('.').pop() : '';
        const map = {
            jpeg: 'JPG',
            jpg: 'JPG',
            png: 'PNG',
            webp: 'WEB',
            pdf: 'PDF',
            hwp: 'HWP',
            hwpx: 'HWP',
            doc: 'DOC',
            docx: 'DOC',
            xls: 'XLS',
            xlsx: 'XLS',
            ppt: 'PPT',
            pptx: 'PPT',
            zip: 'ZIP'
        };
        return map[ext] || (ext ? ext.substring(0, 3).toUpperCase() : 'F');
    }

    function applyFileIcon(fileNameValue) {
        if (!fileIcon) return;
        fileIcon.textContent = String(fileNameValue || '').trim() ? fileExtLabel(fileNameValue) : 'F';
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

    function normalizeCardData(card) {
        const data = card.dataset;
        return {
            subject: decodeHtml(data.subject),
            submitFormatLabel: decodeHtml(data.type),
            status: decodeHtml(data.status),
            title: decodeHtml(data.title),
            content: decodeHtml(data.content),
            deadline: decodeHtml(data.deadline),
            feedback: decodeHtml(data.feedback),
            submitted: data.submitted === 'true',
            submittedAt: decodeHtml(data.submittedAt),
            attachedFile: decodeHtml(data.attachedFile),
            fileSize: Number(data.fileSize || 0),
            mySubmission: decodeHtml(data.mySubmission),
            downloadUrl: data.downloadUrl || '#'
        };
    }

    function applyDetail(detail) {
        const status = detail.status || '';
        const submitted = !!detail.submitted;
        const attachedFile = detail.attachedFile || '';
        const feedback = detail.feedback || '';

        modalSubject.textContent = detail.subject || '';
        modalType.textContent = detail.submitFormatLabel || '';
        modalStatus.textContent = status;
        modalTitle.textContent = detail.title || '';
        modalContent.textContent = detail.content || '과제 상세 내용이 없습니다.';
        modalDeadline.textContent = detail.deadline || '마감일 없음';
        modalFeedback.textContent = feedback;
        setStatusClass(modalStatus, status);

        const feedbackTitle = document.getElementById('parentAssignmentFeedbackTitle');
        rejectBox.style.display = feedback.trim() ? 'block' : 'none';
        if (feedbackTitle) {
            feedbackTitle.textContent = status === '반려' ? '반려 사유' : '선생님 피드백';
        }
        if (feedbackPreview) {
            feedbackPreview.classList.toggle('confirm', status !== '반려' && feedback.trim().length > 0);
        }

        submittedBox.style.display = submitted ? 'block' : 'none';
        submittedFileBox.style.display = 'none';
        submittedTextBox.style.display = 'none';
        downloadBtn.removeAttribute('href');
        applyFileIcon('');

        if (submitted) {
            let submitLabel = '제출 완료';
            if (status === '확인완료') submitLabel = '확인 완료되었습니다.';
            else if (status === '늦은제출') submitLabel = '늦은 제출이 완료되었습니다.';
            else if (status === '반려') submitLabel = '제출 후 반려되었습니다.';

            submittedAtText.textContent = submitLabel + (detail.submittedAt ? ' (' + detail.submittedAt + ')' : '');

            if (attachedFile) {
                submittedFileBox.style.display = 'flex';
                fileName.textContent = attachedFile;
                fileSize.textContent = formatFileSize(detail.fileSize);
                applyFileIcon(attachedFile);
                if (detail.downloadUrl) {
                    downloadBtn.href = detail.downloadUrl;
                }
            }

            if (detail.mySubmission) {
                submittedTextBox.style.display = 'block';
                submittedText.textContent = detail.mySubmission;
            } else if (!attachedFile) {
                submittedTextBox.style.display = 'block';
                submittedText.textContent = '제출된 내용이 없습니다.';
            }
        }
    }

    function openModal() {
        modalOverlay.classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        modalOverlay.classList.remove('open');
        document.body.style.overflow = '';
    }

    function fetchDetail(card) {
        const detailUrl = card.dataset.detailUrl;
        if (!detailUrl) {
            return Promise.resolve(normalizeCardData(card));
        }

        return fetch(detailUrl, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            credentials: 'same-origin'
        }).then(function (response) {
            if (!response.ok) {
                throw new Error('상세 정보를 불러오지 못했습니다.');
            }
            return response.json();
        }).then(function (detail) {
            return Object.assign({}, detail, {
                downloadUrl: card.dataset.downloadUrl || '#'
            });
        }).catch(function () {
            return normalizeCardData(card);
        });
    }

    if (childSelect) {
        childSelect.addEventListener('change', function () {
            const selectedId = childSelect.value;
            const targetUrl = contextPath + '/parent/classes/' + classId + '/assignments?childId=' + encodeURIComponent(selectedId);
            window.location.href = targetUrl;
        });
    }

    document.querySelectorAll('.parent-assignment-card').forEach(function (card) {
        const handler = function () {
            fetchDetail(card).then(function (detail) {
                applyDetail(detail);
                openModal();
            });
        };

        card.addEventListener('click', handler);

        const detailBtn = card.querySelector('.parent-assignment-detail-btn');
        if (detailBtn) {
            detailBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                handler();
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
