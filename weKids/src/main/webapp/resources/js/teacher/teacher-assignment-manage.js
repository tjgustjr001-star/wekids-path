window.addEventListener('DOMContentLoaded', function () {
    const rows = document.querySelectorAll('.teacher-assignment-row');
    const searchInput = document.getElementById('assignmentSearchInput');
    const trashToggleBtn = document.getElementById('assignmentTrashToggleBtn');
    const pageTitle = document.getElementById('assignmentPageTitle');
    const openCreateBtn = document.getElementById('openAssignmentCreateModalBtn');

    const assignmentFormModal = document.getElementById('assignmentFormModal');
    const assignmentDetailModal = document.getElementById('assignmentDetailModal');

    const assignmentFormModalTitle = document.getElementById('assignmentFormModalTitle');
    const assignmentForm = document.getElementById('assignmentForm');

    let trashMode = false;

    function openModal(modal) {
        if (modal) modal.classList.add('open');
    }

    function closeModal(modal) {
        if (modal) modal.classList.remove('open');
    }

    document.querySelectorAll('[data-close-modal]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            closeModal(document.getElementById(btn.getAttribute('data-close-modal')));
        });
    });

    document.querySelectorAll('.teacher-modal-backdrop').forEach(function (backdrop) {
        backdrop.addEventListener('click', function (e) {
            if (e.target === backdrop) closeModal(backdrop);
        });
    });

    function closeAllMenus() {
        document.querySelectorAll('.row-menu-dropdown').forEach(function (menu) {
            menu.classList.remove('open');
        });
    }

    document.querySelectorAll('.row-menu-toggle-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const dropdown = btn.parentElement.querySelector('.row-menu-dropdown');
            const opened = dropdown.classList.contains('open');

            closeAllMenus();
            if (!opened) dropdown.classList.add('open');
        });
    });

    document.addEventListener('click', function () {
        closeAllMenus();
    });

    function applyTrashMode() {
        rows.forEach(function (row) {
            const deleted = row.dataset.deleted === 'true';
            row.style.display = trashMode ? (deleted ? '' : 'none') : (deleted ? 'none' : '');
        });

        if (trashMode) {
            if (pageTitle) pageTitle.textContent = '휴지통 (과제 관리)';
            if (trashToggleBtn) trashToggleBtn.classList.add('active');
            if (openCreateBtn) openCreateBtn.style.display = 'none';
        } else {
            if (pageTitle) pageTitle.textContent = '과제 관리';
            if (trashToggleBtn) trashToggleBtn.classList.remove('active');
            if (openCreateBtn) openCreateBtn.style.display = '';
        }
    }

    if (trashToggleBtn) {
        trashToggleBtn.addEventListener('click', function () {
            trashMode = !trashMode;
            applyTrashMode();
        });
    }

    applyTrashMode();

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const keyword = searchInput.value.trim().toLowerCase();

            rows.forEach(function (row) {
                const title = (row.dataset.title || '').toLowerCase();
                const deleted = row.dataset.deleted === 'true';
                const visibleByTrash = trashMode ? deleted : !deleted;
                const visibleBySearch = title.indexOf(keyword) > -1;

                row.style.display = visibleByTrash && visibleBySearch ? '' : 'none';
            });
        });
    }

    if (openCreateBtn) {
        openCreateBtn.addEventListener('click', function () {
            if (assignmentForm) assignmentForm.reset();
            if (assignmentFormModalTitle) assignmentFormModalTitle.textContent = '새 과제 등록';

            const submitBtn = assignmentFormModal
                ? assignmentFormModal.querySelector('.teacher-primary-btn')
                : null;
            if (submitBtn) submitBtn.textContent = '등록 완료';

            openModal(assignmentFormModal);
        });
    }

    document.querySelectorAll('.edit-open-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();

            const row = btn.closest('.teacher-assignment-row');
            if (!row) return;

            const title = row.dataset.title || '';
            const subject = row.dataset.subject || '국어';
            const status = row.dataset.status || '진행중';
            const deadline = row.dataset.deadline || '';
            const content = row.dataset.content || '';

            const titleInput = document.getElementById('assignmentTitle');
            const subjectSelect = document.getElementById('assignmentSubject');
            const statusSelect = document.getElementById('assignmentStatus');
            const deadlineInput = document.getElementById('assignmentDeadline');
            const contentInput = document.getElementById('assignmentContent');

            if (titleInput) titleInput.value = title;
            if (subjectSelect) subjectSelect.value = subject;
            if (statusSelect) statusSelect.value = status;
            if (deadlineInput) deadlineInput.value = deadline;
            if (contentInput) contentInput.value = content;

            if (assignmentFormModalTitle) assignmentFormModalTitle.textContent = '과제 수정';

            const submitBtn = assignmentFormModal
                ? assignmentFormModal.querySelector('.teacher-primary-btn')
                : null;
            if (submitBtn) submitBtn.textContent = '수정 완료';

            closeAllMenus();
            openModal(assignmentFormModal);
        });
    });

    if (assignmentForm) {
        assignmentForm.addEventListener('submit', function (e) {
            e.preventDefault();
            alert('과제가 저장되었습니다.');
            closeModal(assignmentFormModal);
        });
    }

    document.querySelectorAll('.row-delete-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            if (confirm('정말 삭제하시겠습니까? 삭제된 항목은 휴지통으로 이동합니다.')) {
                alert('삭제 기능은 아직 연결 전입니다.');
            }
        });
    });

    document.querySelectorAll('.row-recover-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            alert('복구 기능은 아직 연결 전입니다.');
        });
    });

    document.querySelectorAll('.status-toggle-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            alert('상태 변경 기능은 아직 연결 전입니다.');
        });
    });

    function makeStudentSubmissionData() {
        return [
            { name: '김민수', status: '제출완료', submittedAt: '2026-03-14 14:30', editCount: 1, feedbackDone: true },
            { name: '이영희', status: '제출완료', submittedAt: '2026-03-14 16:45', editCount: 0, feedbackDone: false },
            { name: '박철수', status: '재제출', submittedAt: '2026-03-15 09:20', editCount: 2, feedbackDone: false },
            { name: '최지혜', status: '제출완료', submittedAt: '2026-03-14 10:20', editCount: 0, feedbackDone: true },
            { name: '정준호', status: '미제출', submittedAt: '', editCount: 0, feedbackDone: false },
            { name: '강서연', status: '제출완료', submittedAt: '2026-03-14 18:00', editCount: 1, feedbackDone: true },
            { name: '윤동현', status: '미제출', submittedAt: '', editCount: 0, feedbackDone: false },
            { name: '임수진', status: '늦은제출', submittedAt: '2026-03-15 02:30', editCount: 0, feedbackDone: false }
        ];
    }

    function renderSubmissionList(data) {
        const list = document.getElementById('studentSubmissionList');
        if (!list) return;

        list.innerHTML = '';

        data.forEach(function (student) {
            let avatarClass = 'none';
            let badgeClass = 'none';

            if (student.status === '제출완료') {
                avatarClass = 'complete';
                badgeClass = 'complete';
            } else if (student.status === '재제출') {
                avatarClass = 'resubmit';
                badgeClass = 'resubmit';
            } else if (student.status === '늦은제출') {
                avatarClass = 'late';
                badgeClass = 'late';
            }

            const feedbackHtml = student.status !== '미제출'
                ? (student.feedbackDone
                    ? '<span class="feedback-done-chip">피드백 완료</span>'
                    : '<span class="feedback-needed-chip inline">피드백 필요</span>')
                : '';

            const metaText = student.submittedAt
                ? '제출: ' + student.submittedAt + (student.editCount > 0 ? ' · 수정 ' + student.editCount + '회' : '')
                : '아직 제출하지 않았습니다';

            const item = document.createElement('div');
            item.className = 'student-submission-item';
            item.innerHTML =
                '<div class="student-submission-left">' +
                    '<div class="submission-avatar ' + avatarClass + '">' + student.name.charAt(0) + '</div>' +
                    '<div class="submission-meta">' +
                        '<strong>' + student.name + '</strong>' +
                        '<p>' + metaText + '</p>' +
                    '</div>' +
                '</div>' +
                '<div class="submission-status-wrap">' +
                    feedbackHtml +
                    '<span class="status-badge ' + badgeClass + '">' + student.status + '</span>' +
                '</div>';

            list.appendChild(item);
        });
    }

    document.querySelectorAll('.detail-open-text').forEach(function (titleEl) {
        titleEl.addEventListener('click', function () {
            const row = titleEl.closest('.teacher-assignment-row');
            if (!row) return;

            const title = row.dataset.title || '';
            const subject = row.dataset.subject || '';
            const status = row.dataset.status || '';
            const deadline = row.dataset.deadline || '';
            const content = row.dataset.content || '';
            const submitCount = Number(row.dataset.submitCount || '0');
            const totalCount = Number(row.dataset.totalCount || '0');

            const detailTitle = document.getElementById('assignmentDetailTitle');
            const detailSubjectBadge = document.getElementById('detailSubjectBadge');
            const detailStatusBadge = document.getElementById('detailStatusBadge');
            const detailDeadlineText = document.getElementById('detailDeadlineText');
            const detailContentText = document.getElementById('detailContentText');

            if (detailTitle) detailTitle.textContent = title;
            if (detailSubjectBadge) detailSubjectBadge.textContent = subject;
            if (detailStatusBadge) {
                detailStatusBadge.textContent = status;
                detailStatusBadge.className =
                    'mini-badge ' +
                    (status === '진행중' ? 'primary' :
                     status === '마감임박' ? 'warning' : 'gray');
            }
            if (detailDeadlineText) detailDeadlineText.textContent = deadline;
            if (detailContentText) detailContentText.textContent = content || '등록된 내용이 없습니다.';

            const data = makeStudentSubmissionData();
            const submitted = data.filter(function (d) { return d.status === '제출완료'; }).length;
            const resubmit = data.filter(function (d) { return d.status === '재제출'; }).length;
            const late = data.filter(function (d) { return d.status === '늦은제출'; }).length;
            const notSubmitted = data.filter(function (d) { return d.status === '미제출'; }).length;
            const rate = data.length === 0 ? 0 : Math.round(((submitted + resubmit + late) / data.length) * 100);

            const needFeedback = data.filter(function (d) {
                return d.status !== '미제출' && !d.feedbackDone;
            }).length;

            const completionRate = document.getElementById('detailSubmissionRate');
            const completionBar = document.getElementById('detailSubmissionBar');
            const submittedCountText = document.getElementById('submittedCountText');
            const resubmitCountText = document.getElementById('resubmitCountText');
            const lateCountText = document.getElementById('lateCountText');
            const notSubmittedCountText = document.getElementById('notSubmittedCountText');
            const needFeedbackText = document.getElementById('needFeedbackText');
            const studentSubmissionCount = document.getElementById('studentSubmissionCount');

            if (completionRate) completionRate.textContent = rate + '%';
            if (completionBar) completionBar.style.width = rate + '%';
            if (submittedCountText) submittedCountText.textContent = submitted + '명';
            if (resubmitCountText) resubmitCountText.textContent = resubmit + '명';
            if (lateCountText) lateCountText.textContent = late + '명';
            if (notSubmittedCountText) notSubmittedCountText.textContent = notSubmitted + '명';
            if (needFeedbackText) needFeedbackText.textContent = needFeedback + '명 피드백 필요';
            if (studentSubmissionCount) studentSubmissionCount.textContent = data.length;

            renderSubmissionList(data);
            openModal(assignmentDetailModal);
        });
    });

    const notifyUnsubmittedBtn = document.getElementById('notifyUnsubmittedBtn');
    if (notifyUnsubmittedBtn) {
        notifyUnsubmittedBtn.addEventListener('click', function () {
            alert('미제출 학생 알림 발송 기능은 아직 연결 전입니다.');
        });
    }
});