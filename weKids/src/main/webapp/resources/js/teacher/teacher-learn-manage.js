window.addEventListener('DOMContentLoaded', function () {
    const rows = document.querySelectorAll('.teacher-learn-row');
    const searchInput = document.getElementById('learnSearchInput');

    const openCreateBtn = document.getElementById('openLearnCreateModalBtn');

    const learnFormModal = document.getElementById('learnFormModal');
    const learnDifficultModal = document.getElementById('learnDifficultModal');
    const learnDetailModal = document.getElementById('learnDetailModal');

    const learnFormModalTitle = document.getElementById('learnFormModalTitle');
    const learnForm = document.getElementById('learnForm');

    const learnType = document.getElementById('learnType');
    const learnLinkField = document.getElementById('learnLinkField');
    const learnTextField = document.getElementById('learnTextField');
    const learnFileField = document.getElementById('learnFileField');

    const learnLinkLabel = document.querySelector('label[for="learnLinkUrl"]');
    const learnLinkUrl = document.getElementById('learnLinkUrl');

    const learnIdInput = document.getElementById('learnId');
    const classIdInput = document.getElementById('classId');

    const trashMode = window.learnTrashMode === true || window.learnTrashMode === 'true';
    const classId = classIdInput ? classIdInput.value : '';
    const contextPath = window.appContextPath || '';

	
	
	function escapeHtml(text) {
	    return String(text || '')
	        .replace(/&/g, '&amp;')
	        .replace(/</g, '&lt;')
	        .replace(/>/g, '&gt;')
	        .replace(/"/g, '&quot;')
	        .replace(/'/g, '&#039;');
	}
	
    function openModal(modal) {
        if (modal) {
            modal.classList.add('open');
        }
    }

    function closeModal(modal) {
        if (modal) {
            modal.classList.remove('open');
        }
    }

    function formatDate(date) {
        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, '0');
        const dd = String(date.getDate()).padStart(2, '0');
        return `${yyyy}-${mm}-${dd}`;
    }

    function formatDateTimeLocal(date) {
        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, '0');
        const dd = String(date.getDate()).padStart(2, '0');
        const hh = String(date.getHours()).padStart(2, '0');
        const mi = String(date.getMinutes()).padStart(2, '0');
        return `${yyyy}-${mm}-${dd}T${hh}:${mi}`;
    }

    function setDefaultLearnDates() {
        const startDateInput = document.getElementById('learnStartDate');
        const deadlineInput = document.getElementById('learnDeadline');

        if (!startDateInput || !deadlineInput) {
            return;
        }

        const today = new Date();
        const nextWeek = new Date();
        nextWeek.setDate(today.getDate() + 7);
        nextWeek.setHours(23, 59, 0, 0);

        startDateInput.value = formatDate(today);
        deadlineInput.value = formatDateTimeLocal(nextWeek);
    }

    function clearTypeSpecificValues(type) {
        const textContentInput = document.getElementById('learnTextContent');

        if (type === '영상' || type === '링크' || type === '파일') {
            if (textContentInput) {
                textContentInput.value = '';
            }
        }

        if (type === '지문읽기') {
            if (learnLinkUrl) {
                learnLinkUrl.value = '';
            }
        }
    }

    function updateTypeFields() {
        if (!learnType) {
            return;
        }

        const value = learnType.value;

        if (learnLinkField) {
            learnLinkField.style.display = (value === '영상' || value === '링크' || value === '파일') ? '' : 'none';
        }

        if (learnTextField) {
            learnTextField.style.display = (value === '지문읽기') ? '' : 'none';
        }

        if (learnFileField) {
            learnFileField.style.display = (value === '파일') ? '' : 'none';
        }

        if (learnLinkLabel && learnLinkUrl) {
			if (value === '영상') {
			    learnLinkLabel.textContent = '유튜브 주소';
			    learnLinkUrl.placeholder = 'https://www.youtube.com/watch?v=... 또는 https://youtu.be/...';
			} else if (value === '링크') {
                learnLinkLabel.textContent = '링크 주소';
                learnLinkUrl.placeholder = 'https://...';
            } else if (value === '파일') {
                learnLinkLabel.textContent = '파일 주소';
                learnLinkUrl.placeholder = '/resources/upload/... 또는 https://...';
            }
        }
    }

	function isYoutubeUrl(url) {
	    if (!url) return false;
	    const value = url.trim().toLowerCase();
	    return value.includes('youtube.com/watch?v=') || value.includes('youtu.be/');
	}
	
    function validateLearnForm() {
        const title = document.getElementById('learnTitle');
        const duration = document.getElementById('learnDuration');
        const startDate = document.getElementById('learnStartDate');
        const deadline = document.getElementById('learnDeadline');
        const textContentInput = document.getElementById('learnTextContent');
        const content = document.getElementById('learnContent');

        if (!title || !learnType || !startDate || !deadline) {
            return true;
        }

        if (!title.value.trim()) {
            alert('학습 제목을 입력해주세요.');
            title.focus();
            return false;
        }

        if (!startDate.value) {
            alert('시작일을 입력해주세요.');
            startDate.focus();
            return false;
        }

        if (!deadline.value) {
            alert('마감일을 입력해주세요.');
            deadline.focus();
            return false;
        }

        if (duration && duration.value && Number(duration.value) <= 0) {
            alert('예상 소요시간은 1분 이상으로 입력해주세요.');
            duration.focus();
            return false;
        }

		if (learnType.value === '영상') {
		    if (!learnLinkUrl || !learnLinkUrl.value.trim()) {
		        alert('영상 유형은 유튜브 주소가 필요합니다.');
		        learnLinkUrl.focus();
		        return false;
		    }

		    if (!isYoutubeUrl(learnLinkUrl.value.trim())) {
		        alert('영상 유형은 유튜브 주소만 등록할 수 있습니다.');
		        learnLinkUrl.focus();
		        return false;
		    }
		}

        if (learnType.value === '링크') {
            if (!learnLinkUrl || !learnLinkUrl.value.trim()) {
                alert('링크 유형은 링크 주소가 필요합니다.');
                learnLinkUrl.focus();
                return false;
            }
        }

        if (learnType.value === '파일') {
            if (!learnLinkUrl || !learnLinkUrl.value.trim()) {
                alert('파일 유형은 파일 주소가 필요합니다.');
                learnLinkUrl.focus();
                return false;
            }
        }

        if (learnType.value === '지문읽기') {
            if (!textContentInput || !textContentInput.value.trim()) {
                alert('지문읽기 유형은 지문 내용을 입력해주세요.');
                textContentInput.focus();
                return false;
            }
        }

        if (content && content.value && content.value.length > 4000) {
            alert('설명은 너무 길게 입력하지 말아라, 허접. 4000자 이하로 줄여.');
            content.focus();
            return false;
        }

        return true;
    }
	
	
	
    document.querySelectorAll('[data-close-modal]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            closeModal(document.getElementById(btn.getAttribute('data-close-modal')));
        });
    });

	/*배경클릭하면 모달창닫히는거*/
/*    document.querySelectorAll('.teacher-modal-backdrop').forEach(function (backdrop) {
        backdrop.addEventListener('click', function (e) {
            if (e.target === backdrop) {
                closeModal(backdrop);
            }
        });
    });*/

    if (learnType) {
        learnType.addEventListener('change', function () {
            updateTypeFields();
        });
        updateTypeFields();
    }

    if (openCreateBtn && learnForm) {
        openCreateBtn.addEventListener('click', function () {
            learnForm.reset();

            if (learnIdInput) {
                learnIdInput.value = '';
            }

            learnForm.action = contextPath + '/teacher/classes/' + classId + '/learns/new';
            learnFormModalTitle.textContent = '학습 자료 등록';

            const submitBtn = learnForm.querySelector('.teacher-primary-btn');
            if (submitBtn) {
                submitBtn.textContent = '등록 완료';
            }

            document.getElementById('learnType').value = '영상';
            document.getElementById('learnRequired').value = 'true';
            document.getElementById('learnStatus').value = 'AUTO';
            setDefaultLearnDates();
            updateTypeFields();
            openModal(learnFormModal);
        });
    }

    if (learnForm) {
        learnForm.addEventListener('submit', function (e) {
            if (!validateLearnForm()) {
                e.preventDefault();
                return;
            }

            clearTypeSpecificValues(learnType ? learnType.value : '');
        });
    }

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
            if (!opened) {
                dropdown.classList.add('open');
            }
        });
    });

    document.addEventListener('click', function () {
        closeAllMenus();
    });

    document.querySelectorAll('.edit-open-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();

            const row = btn.closest('.teacher-learn-row');
            const learnId = row.dataset.learningId || '';

            if (learnIdInput) {
                learnIdInput.value = learnId;
            }

            document.getElementById('learnTitle').value = row.dataset.title || '';
            document.getElementById('learnType').value = row.dataset.type || '영상';
            document.getElementById('learnRequired').value = row.dataset.required === 'true' ? 'true' : 'false';
            document.getElementById('learnStatus').value = row.dataset.manualStatus || 'AUTO';
            document.getElementById('learnDuration').value = row.dataset.duration && row.dataset.duration !== '0' ? row.dataset.duration : '';
            document.getElementById('learnStartDate').value = row.dataset.startDate || '';
            document.getElementById('learnDeadline').value = (row.dataset.deadline || '').replace(' ', 'T');
            document.getElementById('learnLinkUrl').value = row.dataset.linkUrl || '';
			document.getElementById('learnTextContent').value = row.dataset.content || '';
			document.getElementById('learnContent').value = row.dataset.description || '';

            learnForm.action = contextPath + '/teacher/classes/' + classId + '/learns/' + learnId + '/edit';
            learnFormModalTitle.textContent = '학습 자료 수정';

            const submitBtn = learnForm.querySelector('.teacher-primary-btn');
            if (submitBtn) {
                submitBtn.textContent = '수정 완료';
            }

            updateTypeFields();
            closeAllMenus();
            openModal(learnFormModal);
        });
    });

    document.querySelectorAll('.detail-open-btn, .detail-open-text').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            if (e) {
                e.stopPropagation();
            }

            const row = btn.closest('.teacher-learn-row');

            document.getElementById('learnDetailTitle').textContent = row.dataset.title || '';
            document.getElementById('detailRequiredBadge').textContent = row.dataset.required === 'true' ? '필수' : '선택';
            document.getElementById('detailRequiredBadge').className = 'mini-badge ' + (row.dataset.required === 'true' ? 'required' : 'optional');
            document.getElementById('detailTypeBadge').textContent = row.dataset.type || '';
            document.getElementById('detailStatusBadge').textContent = row.dataset.status || '';
            document.getElementById('detailPeriodText').textContent = (row.dataset.startDate || '') + ' ~ ' + (row.dataset.endDate || '');
            document.getElementById('detailDeadlineText').textContent = row.dataset.deadline || '';
            document.getElementById('detailDurationText').textContent = row.dataset.duration && row.dataset.duration !== '0' ? row.dataset.duration + '분' : '-';
            document.getElementById('detailTargetText').textContent = row.dataset.target || '';
            document.getElementById('detailContentText').textContent = row.dataset.description || '';
			
			const detailTextContentBox = document.getElementById('detailTextContentBox');
			const detailTextContentText = document.getElementById('detailTextContentText');

			if (row.dataset.type === '지문읽기') {
			    detailTextContentBox.style.display = '';
			    detailTextContentText.textContent = row.dataset.textContent || row.dataset.content || '';
			} else {
			    detailTextContentBox.style.display = 'none';
			    detailTextContentText.textContent = '';
			}
			
            const detailLinkBox = document.getElementById('detailLinkBox');
            const detailLinkUrl = document.getElementById('detailLinkUrl');

			document.getElementById('detailCompletedCount').textContent =
			    (row.dataset.completedCount || '0') + '명';
			document.getElementById('detailInProgressCount').textContent =
			    (row.dataset.inProgressCount || '0') + '명';
			document.getElementById('detailNotStartedCount').textContent =
			    (row.dataset.notStartedCount || '0') + '명';

			const completionRate = Number(row.dataset.completionRate || 0);
			document.getElementById('detailCompletionRateText').textContent = completionRate + '%';
			document.getElementById('detailCompletionBar').style.width = completionRate + '%';
			document.getElementById('detailStudentCount').textContent = row.dataset.totalStudentCount || '0';

			loadStudentProgress(row.dataset.learningId || '');
			
            if (row.dataset.linkUrl) {
                detailLinkBox.style.display = '';
                detailLinkUrl.href = row.dataset.linkUrl;
                detailLinkUrl.textContent = row.dataset.linkUrl;
            } else {
                detailLinkBox.style.display = 'none';
                detailLinkUrl.href = '#';
                detailLinkUrl.textContent = '';
            }

            closeAllMenus();
            openModal(learnDetailModal);
        });
    });

	document.querySelectorAll('.difficult-open-btn').forEach(function (btn) {
	    btn.addEventListener('click', function (e) {
	        e.stopPropagation();

	        const row = btn.closest('.teacher-learn-row');
	        const titleEl = document.getElementById('difficultLearnTitle');
	        const learnId = row.dataset.learningId || '';

	        if (titleEl) {
	            titleEl.textContent = row.dataset.title || '';
	        }

	        loadDifficultyStudents(learnId);
	        openModal(learnDifficultModal);
	    });
	});

    document.querySelectorAll('.row-delete-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();

            const row = btn.closest('.teacher-learn-row');
            const learnId = row.dataset.learningId || '';

            if (!confirm('삭제하시겠습니까?\n삭제된 항목은 휴지통으로 이동합니다.')) {
                return;
            }

            submitPost(contextPath + '/teacher/classes/' + classId + '/learns/' + learnId + '/delete');
        });
    });

    document.querySelectorAll('.row-recover-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            const row = btn.closest('.teacher-learn-row');
            const learnId = row.dataset.learningId || '';

            submitPost(contextPath + '/teacher/classes/' + classId + '/learns/' + learnId + '/restore');
        });
    });

    document.querySelectorAll('.row-remove-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            const row = btn.closest('.teacher-learn-row');
            const learnId = row.dataset.learningId || '';

            if (!confirm('영구삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) {
                return;
            }

            submitPost(contextPath + '/teacher/classes/' + classId + '/learns/' + learnId + '/remove');
        });
    });

	function renderDifficultyStudents(list) {
	    const listBox = document.getElementById('difficultStudentList');
	    if (!listBox) {
	        return;
	    }

	    if (!list || list.length === 0) {
	        listBox.innerHTML = '<div class="teacher-empty-box">어려움을 남긴 학생이 없습니다.</div>';
	        return;
	    }

	    let html = '';

	    list.forEach(function (item) {
	        const profileImage = item.profileImage || '';
	        const studentName = item.studentName || '학생';
	        const feedbackContent = item.feedbackContent || '';
	        const status = item.status || '미시작';
	        const progressRate = Number(item.progressRate || 0);
	        const updatedAt = item.updatedAt || item.createdAt || '';

	        let statusClass = 'not-started';
	        if (status === 'COMPLETED' || status === '완료') {
	            statusClass = 'complete';
	        } else if (status === 'IN_PROGRESS' || status === '진행중') {
	            statusClass = 'progress';
	        }

	        const avatarHtml = profileImage
	            ? '<img src="' + escapeHtml(profileImage) + '" alt="' + escapeHtml(studentName) + '" class="difficult-avatar-image" />'
	            : '<span class="difficult-avatar-text">' + escapeHtml(studentName.charAt(0)) + '</span>';

	        html +=
	            '<div class="difficult-student-item">' +
	                '<div class="difficult-student-left">' +
	                    '<div class="difficult-avatar">' + avatarHtml + '</div>' +
	                    '<div class="difficult-student-meta">' +
	                        '<strong>' + escapeHtml(studentName) + '</strong>' +
	                        '<p>' + escapeHtml(updatedAt) + '</p>' +
	                    '</div>' +
	                '</div>' +
	                '<div class="difficult-student-right">' +
	                    '<span class="status-badge ' + statusClass + '">' + escapeHtml(status === 'IN_PROGRESS' ? '진행중' : status === 'COMPLETED' ? '완료' : status) + '</span>' +
	                    '<span class="difficulty-progress-text">' + progressRate + '%</span>' +
	                '</div>' +
	                '<div class="difficult-feedback-box">' +
	                    escapeHtml(feedbackContent).replace(/\n/g, '<br>') +
	                '</div>' +
	            '</div>';
	    });

	    listBox.innerHTML = html;
	}
	
	function loadDifficultyStudents(learnId) {
	    const listBox = document.getElementById('difficultStudentList');
	    if (!listBox) {
	        return;
	    }

	    listBox.innerHTML = '<div class="teacher-empty-box">학생 목록을 불러오는 중입니다.</div>';

	    fetch(contextPath + '/teacher/classes/' + classId + '/learns/' + learnId + '/difficulties', {
	        method: 'POST',
	        headers: {
	            'X-Requested-With': 'XMLHttpRequest'
	        }
	    })
	    .then(function (response) {
	        if (!response.ok) {
	            throw new Error('load failed');
	        }
	        return response.json();
	    })
	    .then(function (data) {
	        renderDifficultyStudents(data);
	    })
	    .catch(function () {
	        listBox.innerHTML = '<div class="teacher-empty-box">어려움 학생 목록을 불러오지 못했습니다.</div>';
	    });
	}
	
    function submitPost(action) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = action;
        document.body.appendChild(form);
        form.submit();
    }

    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const keyword = searchInput.value.trim().toLowerCase();

            rows.forEach(function (row) {
                const title = (row.dataset.title || '').toLowerCase();
                const content = (row.dataset.content || '').toLowerCase();
                const type = (row.dataset.type || '').toLowerCase();
                const visible =
                    title.indexOf(keyword) > -1 ||
                    content.indexOf(keyword) > -1 ||
                    type.indexOf(keyword) > -1;

                row.style.display = visible ? '' : 'none';
            });
        });
    }

    if (trashMode && openCreateBtn) {
        openCreateBtn.style.display = 'none';
    }
	function renderStudentProgressList(list) {
	    const listBox = document.getElementById('detailStudentProgressList');
	    const countBox = document.getElementById('detailStudentCount');

	    if (!listBox) return;

	    if (!list || list.length === 0) {
	        if (countBox) countBox.textContent = '0';
	        listBox.innerHTML = '<div class="teacher-empty-box">학생 진행 데이터가 없습니다.</div>';
	        return;
	    }

	    if (countBox) countBox.textContent = String(list.length);

	    let html = '';

	    list.forEach(function (item) {
	        const studentName = item.studentName || '학생';
	        const profileImage = item.profileImage || '';
	        const status = item.status || '미시작';
	        const progressRate = Number(item.progressRate || 0);
	        const lastAccessedAt = item.lastAccessedAt || '';

	        let avatarHtml = '';
	        if (profileImage) {
	            avatarHtml = '<img src="' + escapeHtml(profileImage) + '" alt="' + escapeHtml(studentName) + '" class="detail-student-avatar-image">';
	        } else {
	            avatarHtml = '<span class="detail-student-avatar-text">' + escapeHtml(studentName.charAt(0)) + '</span>';
	        }

	        let statusClass = 'not-started';
	        let progressClass = 'not-started';
	        if (status === '완료') {
	            statusClass = 'complete';
	            progressClass = 'complete';
	        } else if (status === '진행중') {
	            statusClass = 'progress';
	            progressClass = 'progress';
	        }

	        html +=
	            '<div class="detail-student-item">' +
	                '<div class="detail-student-left">' +
	                    '<div class="detail-student-avatar">' + avatarHtml + '</div>' +
	                    '<div class="detail-student-meta">' +
	                        '<strong>' + escapeHtml(studentName) + '</strong>' +
	                        '<div class="detail-student-progress-row">' +
	                            '<div class="detail-student-progress-bar">' +
	                                '<div class="detail-student-progress-fill ' + progressClass + '" style="width:' + progressRate + '%;"></div>' +
	                            '</div>' +
	                            '<span class="detail-student-progress-rate">' + progressRate + '%</span>' +
	                        '</div>' +
	                        '<p>' + (lastAccessedAt ? '마지막 접근: ' + escapeHtml(lastAccessedAt) : '기록 없음') + '</p>' +
	                    '</div>' +
	                '</div>' +
	                '<div class="detail-student-right">' +
	                    '<span class="status-badge ' + statusClass + '">' + escapeHtml(status) + '</span>' +
	                '</div>' +
	            '</div>';
	    });

	    listBox.innerHTML = html;
	}
	function loadStudentProgress(learnId) {
	    const listBox = document.getElementById('detailStudentProgressList');
	    if (!listBox) return;

	    listBox.innerHTML = '<div class="teacher-empty-box">학생 진행 상황을 불러오는 중입니다.</div>';

	    fetch(contextPath + '/teacher/classes/' + classId + '/learns/' + learnId + '/progress', {
	        method: 'POST',
	        headers: {
	            'X-Requested-With': 'XMLHttpRequest'
	        }
	    })
	    .then(function (response) {
	        if (!response.ok) {
	            throw new Error('load failed');
	        }
	        return response.json();
	    })
	    .then(function (data) {
	        renderStudentProgressList(data);
	    })
	    .catch(function () {
	        listBox.innerHTML = '<div class="teacher-empty-box">학생 진행 상황을 불러오지 못했습니다.</div>';
	    });
	}
});