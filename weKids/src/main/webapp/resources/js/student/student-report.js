(function () {
    const config = window.reportPageConfig || {};
    const contextPath = config.contextPath || '';
    const classId = config.classId || '';

    const modal = document.getElementById('reportDetailModal');
    const backdrop = document.getElementById('reportDetailBackdrop');
    const closeBtn = document.getElementById('closeReportDetailModal');

    const detailTitle = document.getElementById('detailTitle');
    const detailPeriodType = document.getElementById('detailPeriodType');
    const detailPeriod = document.getElementById('detailPeriod');
    const detailCreatedAt = document.getElementById('detailCreatedAt');
    const detailTeacherName = document.getElementById('detailTeacherName');

    const summaryLearningRate = document.getElementById('summaryLearningRate');
    const summaryAssignmentRate = document.getElementById('summaryAssignmentRate');
    const summaryLearningCount = document.getElementById('summaryLearningCount');
    const summaryAssignmentCount = document.getElementById('summaryAssignmentCount');

    const learningDonut = document.getElementById('learningDonut');
    const learningDonutLabel = document.getElementById('learningDonutLabel');
    const learningDonutText = document.getElementById('learningDonutText');
    const learningDonutCaption = document.getElementById('learningDonutCaption');
    const assignmentDonut = document.getElementById('assignmentDonut');
    const assignmentDonutLabel = document.getElementById('assignmentDonutLabel');
    const assignmentDonutText = document.getElementById('assignmentDonutText');
    const assignmentDonutCaption = document.getElementById('assignmentDonutCaption');
    const learningProgressBar = document.getElementById('learningProgressBar');
    const learningProgressText = document.getElementById('learningProgressText');
    const assignmentProgressBar = document.getElementById('assignmentProgressBar');
    const assignmentProgressText = document.getElementById('assignmentProgressText');

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

    function normalizeRate(value) {
        const rate = parseInt(value, 10);
        if (isNaN(rate)) return 0;
        if (rate < 0) return 0;
        if (rate > 100) return 100;
        return rate;
    }

    function setDonut(chartEl, labelEl, rate) {
        if (!chartEl) return;
        const safeRate = normalizeRate(rate);
        chartEl.style.setProperty('--rate', safeRate);
        if (labelEl) {
            labelEl.textContent = safeRate + '%';
        }
    }

    function setProgressBar(barEl, textEl, currentCount, totalCount, rate) {
        const safeRate = normalizeRate(rate);
        if (barEl) {
            barEl.style.width = safeRate + '%';
            barEl.setAttribute('aria-valuenow', String(safeRate));
        }
        if (textEl) {
            textEl.textContent = (currentCount || 0) + ' / ' + (totalCount || 0);
        }
    }

    function renderVisualSummary(summary) {
        const s = summary || {};
        const learningRate = normalizeRate(s.learningCompletionRate || 0);
        const assignmentRate = normalizeRate(s.assignmentSubmissionRate || 0);
        const completedLearningCount = s.completedLearningCount || 0;
        const totalLearningCount = s.totalLearningCount || 0;
        const submittedAssignmentCount = s.submittedAssignmentCount || 0;
        const totalAssignmentCount = s.totalAssignmentCount || 0;

        setDonut(learningDonut, learningDonutLabel, learningRate);
        setDonut(assignmentDonut, assignmentDonutLabel, assignmentRate);
        setProgressBar(learningProgressBar, learningProgressText, completedLearningCount, totalLearningCount, learningRate);
        setProgressBar(assignmentProgressBar, assignmentProgressText, submittedAssignmentCount, totalAssignmentCount, assignmentRate);

        if (learningDonutText) {
            learningDonutText.textContent = completedLearningCount + ' / ' + totalLearningCount + ' 완료';
        }
        if (assignmentDonutText) {
            assignmentDonutText.textContent = submittedAssignmentCount + ' / ' + totalAssignmentCount + ' 제출';
        }
        if (learningDonutCaption) {
            learningDonutCaption.textContent = learningRate >= 80 ? '학습 진행이 안정적입니다.' : (learningRate >= 50 ? '학습 진행을 조금 더 끌어올릴 수 있습니다.' : '미완료 학습을 우선 확인해 주세요.');
        }
        if (assignmentDonutCaption) {
            assignmentDonutCaption.textContent = assignmentRate >= 80 ? '과제 제출 흐름이 좋습니다.' : (assignmentRate >= 50 ? '남은 과제를 확인해 주세요.' : '미제출 과제 확인이 필요합니다.');
        }
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
 

        renderVisualSummary(s);
    }

    function fillDetail(detail) {
        if (!detail) return;

        if (detailTitle) {
            detailTitle.textContent = detail.title || '리포트 상세';
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

    function fetchReportDetail(reportId) {
        const url = contextPath + '/student/classes/' + classId + '/reports/' + reportId;

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

	function bindTypeFilters() {
	    const chips = document.querySelectorAll('.filter-chip[data-report-type]');
	    const cards = document.querySelectorAll('.student-report-card');

	    if (!chips.length || !cards.length) {
	        return;
	    }

	    chips.forEach(function (chip) {
	        chip.addEventListener('click', function () {
	            const selectedType = chip.getAttribute('data-report-type');

	            chips.forEach(function (item) {
	                item.classList.remove('is-active');
	            });
	            chip.classList.add('is-active');

	            cards.forEach(function (card) {
	                const reportType = card.getAttribute('data-report-type');

	                if (selectedType === 'ALL' || reportType === selectedType) {
	                    card.style.display = '';
	                } else {
	                    card.style.display = 'none';
	                }
	            });
	        });
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
})();