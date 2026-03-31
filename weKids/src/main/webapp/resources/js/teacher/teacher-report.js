(function () {
    const config = window.reportPageConfig || {};
    const contextPath = config.contextPath || '';
    const classId = config.classId || '';

    const reportTypeSelect = document.getElementById('reportType');
    const studentSelectGroup = document.getElementById('studentSelectGroup');
    const reportGenerateForm = document.getElementById('reportGenerateForm');

    const modal = document.getElementById('reportDetailModal');
    const backdrop = document.getElementById('reportDetailBackdrop');
    const closeBtn = document.getElementById('closeReportDetailModal');

    const detailTitle = document.getElementById('detailTitle');
    const detailReportType = document.getElementById('detailReportType');
    const detailPeriod = document.getElementById('detailPeriod');
    const detailStudentName = document.getElementById('detailStudentName');
    const detailCreatedAt = document.getElementById('detailCreatedAt');

    const summaryLearningRate = document.getElementById('summaryLearningRate');
    const summaryAssignmentRate = document.getElementById('summaryAssignmentRate');
    const summaryLearningCount = document.getElementById('summaryLearningCount');
    const summaryAssignmentCount = document.getElementById('summaryAssignmentCount');

    const missingAssignmentList = document.getElementById('missingAssignmentList');
    const learningFeedbackList = document.getElementById('learningFeedbackList');
    const assignmentFeedbackList = document.getElementById('assignmentFeedbackList');
    const detailComent = document.getElementById('detailComent');


    function formatDate(date) {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const d = String(date.getDate()).padStart(2, '0');
        return y + '-' + m + '-' + d;
    }

    function applyPeriodPreset() {
        const periodTypeSelect = document.getElementById('periodType');
        const startDateInput = document.getElementById('startDate');
        const endDateInput = document.getElementById('endDate');

        if (!periodTypeSelect || !startDateInput || !endDateInput) {
            return;
        }

        const today = new Date();
        const endDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        const startDate = new Date(endDate);

        if (periodTypeSelect.value === 'MONTHLY') {
            startDate.setDate(startDate.getDate() - 29);
        } else {
            startDate.setDate(startDate.getDate() - 6);
        }

        startDateInput.value = formatDate(startDate);
        endDateInput.value = formatDate(endDate);
    }

    function toggleStudentSelect() {
        if (!reportTypeSelect || !studentSelectGroup) {
            return;
        }

        const isClassReport = reportTypeSelect.value === 'CLASS';
        studentSelectGroup.style.display = isClassReport ? 'none' : '';
    }

    function openModal() {
        if (!modal) return;
        modal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        if (!modal) return;
        modal.style.display = 'none';
        document.body.style.overflow = '';
    }

    function escapeHtml(value) {
        if (value == null) return '';
        return String(value)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    function safeJsonParse(text) {
        if (!text) {
            return {};
        }

        try {
            return JSON.parse(text);
        } catch (e) {
            console.error('reportContent JSON parse error:', e);
            return {};
        }
    }

    function setEmptyList(container, message) {
        if (!container) return;
        container.innerHTML = '<div class="empty-inline-text">' + escapeHtml(message || '없음') + '</div>';
    }

    function renderMissingAssignments(items) {
        if (!missingAssignmentList) return;

        if (!items || items.length === 0) {
            setEmptyList(missingAssignmentList, '없음');
            return;
        }

        let html = '';
        items.forEach(function (item) {
            html += ''
                + '<div class="detail-list-item">'
                + '  <div class="item-title">' + escapeHtml(item.title) + '</div>'
                + '  <div class="item-sub">마감일: ' + escapeHtml(item.dueDate || '-') + '</div>'
                + '</div>';
        });

        missingAssignmentList.innerHTML = html;
    }

    function renderLearningFeedbacks(items) {
        if (!learningFeedbackList) return;

        if (!items || items.length === 0) {
            setEmptyList(learningFeedbackList, '없음');
            return;
        }

        let html = '';
        items.forEach(function (item) {
            html += ''
                + '<div class="detail-list-item">'
                + '  <div class="item-title">' + escapeHtml(item.title) + '</div>'
                + '  <div class="item-text">' + escapeHtml(item.feedback || '-') + '</div>'
                + '</div>';
        });

        learningFeedbackList.innerHTML = html;
    }

    function renderAssignmentFeedbacks(items) {
        if (!assignmentFeedbackList) return;

        if (!items || items.length === 0) {
            setEmptyList(assignmentFeedbackList, '없음');
            return;
        }

        let html = '';
        items.forEach(function (item) {
            html += ''
                + '<div class="detail-list-item">'
                + '  <div class="item-title">' + escapeHtml(item.title) + '</div>'
                + '  <div class="item-text">' + escapeHtml(item.feedback || '-') + '</div>'
                + '</div>';
        });

        assignmentFeedbackList.innerHTML = html;
    }

    function fillSummary(summary) {
        const s = summary || {};

        if (summaryLearningRate) {
            summaryLearningRate.textContent = (s.learningCompletionRate || 0) + '%';
        }
        if (summaryAssignmentRate) {
            summaryAssignmentRate.textContent = (s.assignmentSubmissionRate || 0) + '%';
        }
        if (summaryLearningCount) {
            summaryLearningCount.textContent = (s.completedLearningCount || 0) + ' / ' + (s.totalLearningCount || 0);
        }
        if (summaryAssignmentCount) {
            summaryAssignmentCount.textContent = (s.submittedAssignmentCount || 0) + ' / ' + (s.totalAssignmentCount || 0);
        }
    }

    function fillDetail(detail) {
        if (!detail) return;

        if (detailTitle) {
            detailTitle.textContent = detail.title || '리포트 상세';
        }

        if (detailReportType) {
            detailReportType.textContent = detail.reportType === 'CLASS' ? '학급 요약 리포트' : '개인 리포트';
        }

        if (detailPeriod) {
            const typeLabel = detail.periodType === 'MONTHLY' ? '월간' : '주간';
            detailPeriod.textContent = (detail.startDate || '-') + ' ~ ' + (detail.endDate || '-') + ' / ' + typeLabel;
        }

        if (detailStudentName) {
            detailStudentName.textContent = detail.reportType === 'CLASS'
                ? '학급 전체'
                : (detail.studentName || '-');
        }

        if (detailCreatedAt) {
            detailCreatedAt.textContent = detail.createdAt || '-';
        }

        if (detailComent) {
            detailComent.textContent = detail.coment || '-';
        }

        const snapshot = safeJsonParse(detail.reportContent);

        fillSummary(snapshot.summary || {});
        renderMissingAssignments(snapshot.missingAssignments || []);
        renderLearningFeedbacks(snapshot.learningFeedbacks || []);
        renderAssignmentFeedbacks(snapshot.assignmentFeedbacks || []);
    }

    function fetchReportDetail(reportId) {
        const url = contextPath + '/teacher/classes/' + classId + '/reports/' + reportId;

        return fetch(url, {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        }).then(function (response) {
            if (!response.ok) {
                throw new Error('리포트 상세 조회 실패');
            }
            return response.json();
        });
    }

    function bindDetailButtons() {
        const buttons = document.querySelectorAll('.detail-btn');

        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                const reportId = button.getAttribute('data-report-id');
                if (!reportId) {
                    return;
                }

                fetchReportDetail(reportId)
                    .then(function (detail) {
                        fillDetail(detail);
                        openModal();
                    })
                    .catch(function (error) {
                        console.error(error);
                        alert('리포트 상세를 불러오지 못했습니다.');
                    });
            });
        });
    }

    function bindFormValidation() {
        if (!reportGenerateForm) return;

        reportGenerateForm.addEventListener('submit', function (e) {
            const reportType = reportTypeSelect ? reportTypeSelect.value : '';
            if (reportType === 'PERSONAL') {
                const checkedStudents = reportGenerateForm.querySelectorAll('input[name="studentIds"]:checked');
                if (!checkedStudents || checkedStudents.length === 0) {
                    e.preventDefault();
                    alert('개인 리포트는 대상 학생을 한 명 이상 선택해야 합니다.');
                    return;
                }
            }

            const startDateInput = document.getElementById('startDate');
            const endDateInput = document.getElementById('endDate');

            if (startDateInput && endDateInput && startDateInput.value && endDateInput.value) {
                if (startDateInput.value > endDateInput.value) {
                    e.preventDefault();
                    alert('시작일은 종료일보다 늦을 수 없습니다.');
                }
            }
        });
    }

    if (reportTypeSelect) {
        reportTypeSelect.addEventListener('change', toggleStudentSelect);
        toggleStudentSelect();
    }

    const periodTypeSelect = document.getElementById('periodType');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    if (periodTypeSelect) {
        periodTypeSelect.addEventListener('change', applyPeriodPreset);
    }
    if (startDateInput && !startDateInput.value && endDateInput && !endDateInput.value) {
        applyPeriodPreset();
    }

    if (closeBtn) {
        closeBtn.addEventListener('click', closeModal);
    }

    if (backdrop) {
        backdrop.addEventListener('click', closeModal);
    }

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            closeModal();
        }
    });

    bindDetailButtons();
    bindFormValidation();
})();