window.addEventListener('DOMContentLoaded', function () {
    const page = document.getElementById('teacherAssignmentPage');
    if (!page) return;

    const baseUrl = page.dataset.baseUrl;
    const trashMode = page.dataset.trashMode === 'true';
    const searchInput = document.getElementById('assignmentSearchInput');
    const trashToggleBtn = document.getElementById('assignmentTrashToggleBtn');
    const createBtn = document.getElementById('openAssignmentCreateModalBtn');
    const formModal = document.getElementById('assignmentFormModal');
    const detailModal = document.getElementById('assignmentDetailModal');
    const studentModal = document.getElementById('studentSubmissionModal');
    const feedbackModal = document.getElementById('teacherFeedbackModal');
    const rejectModal = document.getElementById('teacherRejectModal');
    const form = document.getElementById('assignmentForm');
    const actionForm = document.getElementById('assignmentActionForm');
    const searchEmptyBox = document.getElementById('assignmentSearchEmptyBox');
    const detailList = document.getElementById('studentSubmissionList');
    const filterSelect = document.getElementById('submissionFilterSelect');
    const notifyBtn = document.getElementById('notifyUnsubmittedBtn');

    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
    const csrfToken = csrfMeta ? csrfMeta.getAttribute('content') : '';
    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : '';

    let currentSubmissionList = [];
    let currentAssignmentDetail = null;
    let activeStudentSubmission = null;

    function qs(selector, root) {
        return (root || document).querySelector(selector);
    }

    function qsa(selector, root) {
        return Array.from((root || document).querySelectorAll(selector));
    }

    function openModal(modal) {
        if (modal) modal.classList.add('open');
    }

    function closeModal(modal) {
        if (modal) modal.classList.remove('open');
    }

    function escapeHtml(value) {
        return String(value == null ? '' : value)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    function fetchJson(url, options) {
        const opts = options || {};
        opts.headers = Object.assign({ 'Accept': 'application/json' }, opts.headers || {});

        if (csrfHeader && csrfToken) {
            opts.headers[csrfHeader] = csrfToken;
        }

        return fetch(url, opts).then(function (res) {
            return res.json().catch(function () {
                return {};
            }).then(function (data) {
                if (!res.ok) {
                    throw new Error(data.message || '요청을 처리하지 못했습니다.');
                }
                return data;
            });
        });
    }

    function formatSize(size) {
        const value = Number(size || 0);
        if (!value) return '0 KB';
        if (value >= 1024 * 1024) return (value / (1024 * 1024)).toFixed(1) + ' MB';
        return (value / 1024).toFixed(1) + ' KB';
    }

    function extLabel(fileName) {
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

    function closeAllMenus() {
        qsa('.row-menu-dropdown.open').forEach(function (menu) {
            menu.classList.remove('open');
        });
    }

    function fillForm(row) {
        qs('#assignmentTitle').value = row ? (row.dataset.title || '') : '';
        qs('#assignmentSubject').value = row ? (row.dataset.subject || '국어') : '국어';
        qs('#assignmentStatus').value = row ? (row.dataset.status || '진행중') : '진행중';
        qs('#assignmentDeadline').value = row ? (row.dataset.deadlineValue || '') : '';
        qs('#assignmentFormat').value = row ? (row.dataset.submitFormat || '파일') : '파일';
        qs('#assignmentMaxEditCount').value = row ? (row.dataset.maxEditCount || 3) : 3;
        qs('#assignmentContent').value = row ? (row.dataset.content || '') : '';
    }

    function openCreateModal() {
        if (!form) return;

        fillForm(null);
        form.action = baseUrl;
        qs('#assignmentFormModalTitle').textContent = '새 과제 등록';
        qs('#assignmentFormSubmitBtn').textContent = '등록 완료';
        openModal(formModal);
    }

    function openEditModal(row) {
        if (!row || !form) return;

        fillForm(row);
        form.action = baseUrl + '/' + row.dataset.id + '/update';
        qs('#assignmentFormModalTitle').textContent = '과제 수정';
        qs('#assignmentFormSubmitBtn').textContent = '수정 완료';
        openModal(formModal);
    }

    function submitAction(url) {
        if (!actionForm) return;
        actionForm.action = url;
        actionForm.submit();
    }

    function applySearchFilter() {
        const keyword = (searchInput.value || '').trim().toLowerCase();
        const rows = qsa('.teacher-assignment-row');
        let visibleCount = 0;

        rows.forEach(function (row) {
            const title = (row.dataset.title || '').toLowerCase();
            const visible = !keyword || title.indexOf(keyword) > -1;
            row.style.display = visible ? '' : 'none';
            if (visible) visibleCount += 1;
        });

        if (searchEmptyBox) {
            searchEmptyBox.style.display = visibleCount === 0 && rows.length > 0 ? 'block' : 'none';
        }
    }

    function statusClass(status) {
        if (status === '진행중') return 'primary';
        if (status === '마감임박') return 'warning';
        return 'gray';
    }

    function submissionStatusClass(status) {
        if (status === '제출완료') return 'green';
        if (status === '재제출') return 'blue';
        if (status === '늦은제출') return 'orange';
        if (status === '확인완료') return 'green';
        if (status === '반려') return 'reject';
        return 'red';
    }

    function renderSubmissionList(filterValue) {
        if (!detailList) return;

        const list = currentSubmissionList.filter(function (item) {
            return filterValue === 'all' ? true : item.submitStatus === filterValue;
        });

        if (!list.length) {
            detailList.innerHTML = '<div class="teacher-empty-box">표시할 제출 내역이 없습니다.</div>';
            return;
        }

        detailList.innerHTML = list.map(function (item) {
            const feedbackClass = item.feedbackDone ? 'done' : 'need';
            const feedbackText = item.feedbackDone ? '피드백 완료' : '피드백 필요';
            const statusClassName = submissionStatusClass(item.submitStatus);
            const submitText = item.submitAt ? escapeHtml(item.submitAt) : '제출 기록 없음';
            const revise = Number(item.reviseCount || 0);

            return ''
                + '<button type="button" class="submission-student-row submission-student-open-btn" data-student-id="' + escapeHtml(item.studentId) + '">'
                +     '<div class="submission-student-main">'
                +         '<div class="submission-avatar ' + statusClassName + '">' + escapeHtml(item.nameFirst || '?') + '</div>'
                +         '<div class="submission-student-info">'
                +             '<div class="submission-name-row"><strong>' + escapeHtml(item.studentName) + '</strong><span class="feedback-mini-badge ' + feedbackClass + '">' + feedbackText + '</span></div>'
                +             '<div class="submission-meta-row"><span>🕒 ' + submitText + '</span><span>수정 ' + revise + '회</span></div>'
                +         '</div>'
                +     '</div>'
                +     '<span class="submission-status-pill ' + statusClassName + '">' + escapeHtml(item.submitStatus) + '</span>'
                + '</button>';
        }).join('');
    }

    function applyAssignmentDetail(detail) {
        currentAssignmentDetail = detail;
        currentSubmissionList = Array.isArray(detail.submissionList) ? detail.submissionList : [];

        qs('#assignmentDetailTitle').textContent = detail.title || '-';
        qs('#detailSubjectBadge').textContent = detail.subject || '-';

        const statusBadge = qs('#detailStatusBadge');
        statusBadge.textContent = detail.status || '-';
        statusBadge.className = 'status-badge ' + statusClass(detail.status || '');

        qs('#detailAssignmentDeadline').textContent = detail.deadline || '-';
        qs('#detailAssignmentContent').textContent = detail.content || '등록된 내용이 없습니다.';
        qs('#detailNeedFeedback').textContent = (detail.needFeedback || 0) + '명 피드백 필요';
        qs('#detailSubmissionRate').textContent = (detail.progressPercent || 0) + '%';
        qs('#detailSubmissionBar').style.width = (detail.progressPercent || 0) + '%';
        qs('#submittedCountText').textContent = (detail.submittedCount || 0) + '명';
        qs('#resubmittedCountText').textContent = (detail.resubmittedCount || 0) + '명';
        qs('#lateCountText').textContent = (detail.lateCount || 0) + '명';
        qs('#notSubmittedCountText').textContent = (detail.notSubmittedCount || 0) + '명';
        qs('#submissionStudentCount').textContent = (detail.totalCount || currentSubmissionList.length || 0);

        if (filterSelect) {
            filterSelect.value = 'all';
        }

        renderSubmissionList('all');
    }

    function openDetail(id) {
        fetchJson(baseUrl + '/' + id + '/detail')
            .then(function (detail) {
                applyAssignmentDetail(detail);
                openModal(detailModal);
            })
            .catch(function (error) {
                alert(error.message || '상세 정보를 불러오지 못했습니다.');
            });
    }

    function fillStudentModal(submission) {
        activeStudentSubmission = submission;

        const assignmentSubject = currentAssignmentDetail ? (currentAssignmentDetail.subject || '-') : '-';
        const assignmentFormat = currentAssignmentDetail ? currentAssignmentDetail.submitFormat : '';
        const assignmentTitle = currentAssignmentDetail ? (currentAssignmentDetail.title || '-') : '-';
        const assignmentContent = currentAssignmentDetail ? (currentAssignmentDetail.content || '등록된 내용이 없습니다.') : '등록된 내용이 없습니다.';
        const assignmentDeadline = currentAssignmentDetail ? (currentAssignmentDetail.deadline || '-') : '-';

        qs('#studentWorkSubjectBadge').textContent = assignmentSubject;

        let workTypeText = '파일 제출';
        if (assignmentFormat === '텍스트') {
            workTypeText = '텍스트 제출';
        } else if (assignmentFormat === '이미지') {
            workTypeText = '이미지 제출';
        }
        qs('#studentWorkTypeBadge').textContent = workTypeText;

        const statusBadge = qs('#studentWorkStatusBadge');
        statusBadge.textContent = submission.submitStatus || '미제출';
        statusBadge.className = 'mini-badge blue ' + submissionStatusClass(submission.submitStatus);

        qs('#studentWorkTitle').textContent = assignmentTitle;
        qs('#studentWorkAssignmentContent').textContent = assignmentContent;
        qs('#studentWorkDeadline').textContent = assignmentDeadline;
        const remainCountEl = qs('#studentWorkRemainCount');
        if (remainCountEl) remainCountEl.textContent = (submission.remainingEditCount || 0) + '회';

        const fileBox = qs('#teacherStudentFileBox');
        const textBox = qs('#teacherStudentTextBox');
        const submittedState = qs('#teacherStudentSubmittedState');
        const downloadBtn = qs('#teacherStudentDownloadBtn');
        const currentReasonLabel = qs('#teacherRejectCurrentReasonLabel');
        const reasonInput = qs('#teacherStudentRejectReason');
        const completeBtn = qs('#teacherStudentCompleteBtn');
        const feedbackBtn = qs('#teacherStudentFeedbackBtn');
        const feedbackTextarea = qs('#teacherStudentFeedbackContent');
        const feedbackView = qs('#teacherStudentFeedbackView');
        const feedbackTitle = qs('#teacherStudentFeedbackTitle');
        const feedbackText = qs('#teacherStudentFeedbackText');

        fileBox.style.display = submission.attachedFileName ? 'flex' : 'none';
        if (submission.attachedFileName) {
            qs('#teacherStudentFileExt').textContent = extLabel(submission.attachedFileName);
            qs('#teacherStudentFileName').textContent = submission.attachedFileName;
            qs('#teacherStudentFileSize').textContent = formatSize(submission.fileSize);
        }

        textBox.style.display = submission.content ? 'block' : 'none';
        qs('#teacherStudentSubmissionText').textContent = submission.content || '';

        submittedState.textContent = submission.submitted
            ? '제출이 완료되었습니다.' + (submission.submitAt ? ' (' + submission.submitAt + ')' : '')
            : '아직 제출하지 않았습니다.';

        const hasExistingReturnReason =
            (submission.submitStatus || '').trim() === '반려' &&
            typeof submission.returnReason === 'string' &&
            submission.returnReason.trim() !== '';

        if (hasExistingReturnReason) {
            currentReasonLabel.style.display = 'inline-flex';
            reasonInput.value = submission.returnReason.trim();
        } else {
            currentReasonLabel.style.display = 'none';
            reasonInput.value = '';
        }

        downloadBtn.style.display = submission.canDownload ? 'inline-flex' : 'none';

        qs('#teacherStudentRejectBtn').disabled = !submission.submitted;

        if (completeBtn) {
            completeBtn.style.display =
                submission.submitted && (submission.submitStatus || '').trim() !== '제출완료' && (submission.submitStatus || '').trim() !== '확인완료'
                    ? 'inline-flex'
                    : 'none';
            completeBtn.disabled = !submission.submitted;
        }

        if (feedbackBtn) {
            const hasTeacherFeedback = typeof submission.feedbackContent === 'string' && submission.feedbackContent.trim() !== '';
            const currentStatus = (submission.submitStatus || '').trim();

            feedbackBtn.style.display =
                submission.submitted && currentStatus !== '반려'
                    ? 'inline-flex'
                    : 'none';
            feedbackBtn.disabled = !submission.submitted;
            feedbackBtn.textContent = hasTeacherFeedback ? '피드백 수정' : '피드백';
        }

        if (feedbackView && feedbackTitle && feedbackText) {
            const isRejected = (submission.submitStatus || '').trim() === '반려';
            const visibleFeedback = isRejected
                ? (submission.returnReason || '').trim()
                : (submission.feedbackContent || '').trim();

            feedbackView.style.display = visibleFeedback ? 'block' : 'none';
            feedbackView.classList.toggle('is-confirm', !isRejected && !!visibleFeedback);
            feedbackView.classList.toggle('is-reject', isRejected && !!visibleFeedback);
            feedbackTitle.textContent = isRejected ? '반려 사유' : '선생님 피드백';
            feedbackText.textContent = visibleFeedback;
        }

        if (feedbackTextarea) {
            feedbackTextarea.value = submission.feedbackContent || '';
        }
    }

    function openStudentSubmission(studentId) {
        if (!currentAssignmentDetail) return;

        fetchJson(baseUrl + '/' + currentAssignmentDetail.id + '/students/' + studentId)
            .then(function (submission) {
                fillStudentModal(submission);
                openModal(studentModal);
            })
            .catch(function (error) {
                alert(error.message || '학생 제출 내역을 불러오지 못했습니다.');
            });
    }

    function completeStudentSubmission() {
        if (!currentAssignmentDetail || !activeStudentSubmission) return;
        if (!confirm('이 제출을 제출완료 상태로 변경할까요?')) return;

        fetchJson(baseUrl + '/' + currentAssignmentDetail.id + '/students/' + activeStudentSubmission.studentId + '/complete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: ''
        }).then(function (result) {
            alert(result.message || '제출완료 상태로 변경했습니다.');

            if (result.submission) {
                fillStudentModal(result.submission);
            }

            if (result.assignmentDetail) {
                applyAssignmentDetail(result.assignmentDetail);
            } else {
                openDetail(currentAssignmentDetail.id);
            }
        }).catch(function (error) {
            alert(error.message || '상태 변경에 실패했습니다.');
        });
    }

    function openRejectModal() {
        if (!activeStudentSubmission || !activeStudentSubmission.submitted) return;

        const currentReasonLabel = qs('#teacherRejectCurrentReasonLabel');
        const reasonInput = qs('#teacherStudentRejectReason');
        const hasExistingReturnReason =
            (activeStudentSubmission.submitStatus || '').trim() === '반려' &&
            typeof activeStudentSubmission.returnReason === 'string' &&
            activeStudentSubmission.returnReason.trim() !== '';

        if (currentReasonLabel) {
            currentReasonLabel.style.display = hasExistingReturnReason ? 'inline-flex' : 'none';
        }
        if (reasonInput) {
            reasonInput.value = hasExistingReturnReason ? activeStudentSubmission.returnReason.trim() : '';
        }
        openModal(rejectModal);
    }

    function rejectStudentSubmission() {
        if (!currentAssignmentDetail || !activeStudentSubmission) return;

        const reason = (qs('#teacherStudentRejectReason').value || '').trim();
        if (!reason) {
            alert('반려 사유를 입력해주세요.');
            qs('#teacherStudentRejectReason').focus();
            return;
        }

        const formData = new URLSearchParams();
        formData.append('returnReason', reason);

        fetchJson(baseUrl + '/' + currentAssignmentDetail.id + '/students/' + activeStudentSubmission.studentId + '/reject', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: formData.toString()
        }).then(function (result) {
            alert(result.message || '과제를 반려했습니다.');
            closeModal(rejectModal);

            if (result.submission) {
                fillStudentModal(result.submission);
            }

            if (result.assignmentDetail) {
                applyAssignmentDetail(result.assignmentDetail);
            } else {
                openDetail(currentAssignmentDetail.id);
            }
        }).catch(function (error) {
            alert(error.message || '과제 반려 처리에 실패했습니다.');
        });
    }


    function openFeedbackModal() {
        if (!activeStudentSubmission || !activeStudentSubmission.submitted) return;
        const feedbackTextarea = qs('#teacherStudentFeedbackContent');
        if (feedbackTextarea) {
            feedbackTextarea.value = activeStudentSubmission.feedbackContent || '';
        }
        openModal(feedbackModal);
    }

    function confirmFeedbackComplete() {
        if (!currentAssignmentDetail || !activeStudentSubmission) return;

        const feedbackContent = (qs('#teacherStudentFeedbackContent').value || '').trim();
        if (!feedbackContent) {
            alert('피드백 내용을 입력해주세요.');
            qs('#teacherStudentFeedbackContent').focus();
            return;
        }

        const formData = new URLSearchParams();
        formData.append('feedbackContent', feedbackContent);

        fetchJson(baseUrl + '/' + currentAssignmentDetail.id + '/students/' + activeStudentSubmission.studentId + '/feedback-complete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: formData.toString()
        }).then(function (result) {
            alert(result.message || '피드백을 등록하고 확인완료로 변경했습니다.');
            closeModal(feedbackModal);

            if (result.submission) {
                fillStudentModal(result.submission);
            }

            if (result.assignmentDetail) {
                applyAssignmentDetail(result.assignmentDetail);
            } else {
                openDetail(currentAssignmentDetail.id);
            }
        }).catch(function (error) {
            alert(error.message || '피드백 저장에 실패했습니다.');
        });
    }

    document.addEventListener('click', function (event) {
        const closeBtn = event.target.closest('[data-close-modal]');
        if (closeBtn) {
            closeModal(document.getElementById(closeBtn.dataset.closeModal));
            return;
        }

        const toggleBtn = event.target.closest('.row-menu-toggle-btn');
        if (toggleBtn) {
            event.stopPropagation();

            const menu = qs('.row-menu-dropdown', toggleBtn.closest('.row-menu-wrap'));
            const isOpen = menu.classList.contains('open');

            closeAllMenus();
            if (!isOpen) {
                menu.classList.add('open');
            }
            return;
        }

        const row = event.target.closest('.teacher-assignment-row');

        if (event.target.closest('.detail-open-btn') || event.target.closest('.detail-open-text')) {
            closeAllMenus();
            if (row && row.dataset.deleted !== 'true') {
                openDetail(row.dataset.id);
            }
            return;
        }

        if (event.target.closest('.edit-open-btn')) {
            closeAllMenus();
            openEditModal(row);
            return;
        }

        if (event.target.closest('.row-delete-btn')) {
            closeAllMenus();
            if (confirm('이 과제를 휴지통으로 이동할까요?')) {
                submitAction(baseUrl + '/' + row.dataset.id + '/delete');
            }
            return;
        }

        if (event.target.closest('.row-recover-btn')) {
            if (confirm('이 과제를 복구할까요?')) {
                submitAction(baseUrl + '/' + row.dataset.id + '/restore');
            }
            return;
        }

        if (event.target.closest('.row-remove-btn')) {
            if (confirm('영구삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) {
                submitAction(baseUrl + '/' + row.dataset.id + '/remove');
            }
            return;
        }

        if (event.target.closest('.status-toggle-btn')) {
            closeAllMenus();
            submitAction(baseUrl + '/' + row.dataset.id + '/toggle-status');
            return;
        }

        const studentRow = event.target.closest('.submission-student-open-btn');
        if (studentRow) {
            openStudentSubmission(studentRow.dataset.studentId);
            return;
        }

        if (!event.target.closest('.row-menu-wrap')) {
            closeAllMenus();
        }

        if (event.target === formModal) {
            closeModal(formModal);
        }
        if (event.target === detailModal) {
            closeModal(detailModal);
        }
        if (event.target === studentModal) {
            closeModal(studentModal);
        }
        if (event.target === feedbackModal) {
            closeModal(feedbackModal);
        }
        if (event.target === rejectModal) {
            closeModal(rejectModal);
        }
    });

    if (createBtn) {
        createBtn.addEventListener('click', openCreateModal);
    }

    if (trashToggleBtn) {
        trashToggleBtn.addEventListener('click', function () {
            window.location.href = baseUrl + (trashMode ? '' : '?trash=1');
        });
    }

    if (searchInput) {
        searchInput.addEventListener('input', applySearchFilter);
    }

    if (filterSelect) {
        filterSelect.addEventListener('change', function () {
            renderSubmissionList(filterSelect.value);
        });
    }

    if (notifyBtn) {
        notifyBtn.addEventListener('click', function () {
            alert('알림 발송 기능은 다음 단계에서 연결할 수 있습니다.');
        });
    }

    const rejectBtn = document.getElementById('teacherStudentRejectBtn');
    if (rejectBtn) {
        rejectBtn.addEventListener('click', openRejectModal);
    }

    const completeBtn = document.getElementById('teacherStudentCompleteBtn');
    if (completeBtn) {
        completeBtn.addEventListener('click', completeStudentSubmission);
    }

    const feedbackBtn = document.getElementById('teacherStudentFeedbackBtn');
    if (feedbackBtn) {
        feedbackBtn.addEventListener('click', openFeedbackModal);
    }

    const rejectConfirmBtn = document.getElementById('teacherRejectConfirmBtn');
    if (rejectConfirmBtn) {
        rejectConfirmBtn.addEventListener('click', rejectStudentSubmission);
    }

    const feedbackConfirmBtn = document.getElementById('teacherFeedbackConfirmBtn');
    if (feedbackConfirmBtn) {
        feedbackConfirmBtn.addEventListener('click', confirmFeedbackComplete);
    }

    const downloadBtn = document.getElementById('teacherStudentDownloadBtn');
    if (downloadBtn) {
        downloadBtn.addEventListener('click', function () {
            if (!currentAssignmentDetail || !activeStudentSubmission || !activeStudentSubmission.canDownload) return;
            window.location.href = baseUrl + '/' + currentAssignmentDetail.id + '/students/' + activeStudentSubmission.studentId + '/download';
        });
    }
});