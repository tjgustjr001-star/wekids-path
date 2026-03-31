(function () {
    const config = window.reportPageConfig || {};
    const contextPath = config.contextPath || '';
    const classId = config.classId || '';

    const filterForm = document.getElementById('parentReportFilterForm');
    const studentSelect = document.getElementById('studentId');

    const modal = document.getElementById('reportDetailModal');
    const backdrop = document.getElementById('reportDetailBackdrop');
    const closeBtn = document.getElementById('closeReportDetailModal');

    const detailTitle = document.getElementById('detailTitle');
    const detailHeaderTitle = document.getElementById('detailHeaderTitle');
    const detailStudentName = document.getElementById('detailStudentName');
    const detailPeriodType = document.getElementById('detailPeriodType');
    const detailPeriod = document.getElementById('detailPeriod');
    const detailCreatedAt = document.getElementById('detailCreatedAt');
    const detailTeacherName = document.getElementById('detailTeacherName');

    const summaryLearningRate = document.getElementById('summaryLearningRate');
    const summaryAssignmentRate = document.getElementById('summaryAssignmentRate');
    const summaryLearningCount = document.getElementById('summaryLearningCount');
    const summaryAssignmentCount = document.getElementById('summaryAssignmentCount');

    const missingAssignmentList = document.getElementById('missingAssignmentList');
    const learningFeedbackList = document.getElementById('learningFeedbackList');
    const assignmentFeedbackList = document.getElementById('assignmentFeedbackList');
    const detailComent = document.getElementById('detailComent');

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

	function renderPendingLearnings(items) {
	    if (!learningFeedbackList) return;

	    if (!items || items.length === 0) {
	        setEmptyList(learningFeedbackList, '없음');
	        return;
	    }

	    let html = '';

	    items.forEach(function (item) {
	        html += ''
	            + '<div class="detail-list-item">'
	            + '  <div class="item-title">' + escapeHtml(item.title || '-') + '</div>'
	            + '  <div class="item-sub">상태: ' + escapeHtml(item.status || '미완료') + '</div>'
	            + '  <div class="item-sub">마감일: ' + escapeHtml(item.endDate || '-') + '</div>'
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

        if (detailHeaderTitle) {
            detailHeaderTitle.textContent = detail.title || '-';
        }

        if (detailStudentName) {
            detailStudentName.textContent = detail.reportType === 'CLASS'
                ? '학급 전체'
                : (detail.studentName || '-');
        }

        if (detailPeriodType) {
            detailPeriodType.textContent = detail.periodType === 'MONTHLY' ? '월간' : '주간';
        }

        if (detailPeriod) {
            detailPeriod.textContent = (detail.startDate || '-') + ' ~ ' + (detail.endDate || '-');
        }

        if (detailCreatedAt) {
            detailCreatedAt.textContent = detail.createdAt || '-';
        }

        if (detailTeacherName) {
            detailTeacherName.textContent = detail.teacherName || '-';
        }

        if (detailComent) {
            detailComent.textContent = detail.coment || '-';
        }

        const snapshot = safeJsonParse(detail.reportContent);

		fillSummary(snapshot.summary || {});
		renderMissingAssignments(snapshot.missingAssignments || []);
		renderPendingLearnings(snapshot.pendingLearnings || []);
		renderAssignmentFeedbacks(snapshot.assignmentFeedbacks || []);
    }

    function fetchReportDetail(reportId, studentId, reportType) {
        let url = contextPath
            + '/parent/classes/' + classId
            + '/reports/' + reportId;

        if (reportType !== 'CLASS' && studentId) {
            url += '?studentId=' + encodeURIComponent(studentId);
        }

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

	function applyListFilters() {
	    const activeChip = document.querySelector('.filter-chip[data-report-type].is-active');
	    const selectedType = activeChip ? activeChip.getAttribute('data-report-type') : 'ALL';
	    const selectedStudentId = studentSelect ? studentSelect.value : '';

	    const cards = document.querySelectorAll('.parent-report-card');

	    cards.forEach(function (card) {
	        const reportType = card.getAttribute('data-report-type') || '';
	        const studentId = card.getAttribute('data-student-id') || '';

	        const matchedType = (selectedType === 'ALL' || reportType === selectedType);
	        const matchedStudent = (!selectedStudentId || reportType === 'CLASS' || studentId === selectedStudentId);

	        if (matchedType && matchedStudent) {
	            card.style.display = '';
	        } else {
	            card.style.display = 'none';
	        }
	    });
	}

	function bindTypeFilters() {
	    const chips = document.querySelectorAll('.filter-chip[data-report-type]');

	    chips.forEach(function (chip) {
	        chip.addEventListener('click', function () {
	            chips.forEach(function (item) {
	                item.classList.remove('is-active');
	            });
	            chip.classList.add('is-active');
	            applyListFilters();
	        });
	    });

	    if (studentSelect) {
	        studentSelect.addEventListener('change', function () {
	            applyListFilters();
	        });
	    }
	}
	
    function bindDetailButtons() {
        const buttons = document.querySelectorAll('.detail-btn');

        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                const reportId = button.getAttribute('data-report-id');
                const studentId = button.getAttribute('data-student-id');
                const reportType = button.getAttribute('data-report-type');

                if (!reportId) {
                    return;
                }

                fetchReportDetail(reportId, studentId, reportType)
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
	    bindTypeFilters();
	    applyListFilters();
})();