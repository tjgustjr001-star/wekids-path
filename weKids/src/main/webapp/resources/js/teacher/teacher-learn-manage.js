window.addEventListener('DOMContentLoaded', function () {
    const rows = document.querySelectorAll('.teacher-learn-row');
    const searchInput = document.getElementById('learnSearchInput');
<<<<<<< HEAD
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

    document.querySelectorAll('.teacher-modal-backdrop').forEach(function (backdrop) {
        backdrop.addEventListener('click', function (e) {
            if (e.target === backdrop) {
                closeModal(backdrop);
            }
        });
    });

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
            document.getElementById('learnStatus').value = '운영중';
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
            document.getElementById('learnStatus').value = row.dataset.status || '운영중';
            document.getElementById('learnDuration').value = row.dataset.duration && row.dataset.duration !== '0' ? row.dataset.duration : '';
            document.getElementById('learnStartDate').value = row.dataset.startDate || '';
            document.getElementById('learnDeadline').value = (row.dataset.deadline || '').replace(' ', 'T');
            document.getElementById('learnLinkUrl').value = row.dataset.linkUrl || '';
            document.getElementById('learnTextContent').value = row.dataset.textContent || '';
            document.getElementById('learnContent').value = row.dataset.content || '';

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
            document.getElementById('detailContentText').textContent = row.dataset.content || '';

            const detailLinkBox = document.getElementById('detailLinkBox');
            const detailLinkUrl = document.getElementById('detailLinkUrl');

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
=======
    const trashToggleBtn = document.getElementById('trashToggleBtn');
    const pageTitle = document.getElementById('learnPageTitle');
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

    function updateTypeFields() {
        const value = learnType.value;
        learnLinkField.style.display = (value === '영상' || value === '링크') ? '' : 'none';
        learnTextField.style.display = (value === '지문읽기') ? '' : 'none';
        learnFileField.style.display = (value === '파일') ? '' : 'none';
    }

    if (learnType) {
        learnType.addEventListener('change', updateTypeFields);
        updateTypeFields();
    }

    if (openCreateBtn) {
        openCreateBtn.addEventListener('click', function () {
            learnForm.reset();
            learnFormModalTitle.textContent = '학습 자료 등록';
            document.querySelector('#learnForm .teacher-primary-btn').textContent = '등록 완료';
            updateTypeFields();
            openModal(learnFormModal);
        });
    }

    document.querySelectorAll('.edit-open-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const row = btn.closest('.teacher-learn-row');

            document.getElementById('learnTitle').value = row.dataset.title || '';
            document.getElementById('learnType').value = row.dataset.type || '영상';
            document.getElementById('learnRequired').value = row.dataset.required === 'true' ? 'true' : 'false';
            document.getElementById('learnStatus').value = row.dataset.status || '대기중';
            document.getElementById('learnDuration').value = row.dataset.duration || '10';
            document.getElementById('learnStartDate').value = row.dataset.startDate || '';
            document.getElementById('learnDeadline').value = (row.dataset.deadline || '').replace(' ', 'T');
            document.getElementById('learnLinkUrl').value = row.dataset.linkUrl || '';
            document.getElementById('learnContent').value = row.dataset.content || '';

            learnFormModalTitle.textContent = '학습 자료 수정';
            document.querySelector('#learnForm .teacher-primary-btn').textContent = '수정 완료';
            updateTypeFields();

            closeAllMenus();
            openModal(learnFormModal);
        });
    });

    if (learnForm) {
        learnForm.addEventListener('submit', function (e) {
            e.preventDefault();
            alert('학습 자료가 저장되었습니다.');
            closeModal(learnFormModal);
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
            pageTitle.textContent = '휴지통 (학습 관리)';
            trashToggleBtn.classList.add('active');
            openCreateBtn.style.display = 'none';
        } else {
            pageTitle.textContent = '학습 목록 관리';
            trashToggleBtn.classList.remove('active');
            openCreateBtn.style.display = '';
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

    document.querySelectorAll('.row-recover-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            alert('복구 기능은 아직 연결 전입니다.');
        });
    });

    document.querySelectorAll('.row-delete-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            if (confirm('정말 삭제하시겠습니까? 삭제된 항목은 휴지통으로 이동합니다.')) {
                alert('삭제 기능은 아직 연결 전입니다.');
            }
        });
    });

    document.querySelectorAll('.status-toggle-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            alert('상태 변경 기능은 아직 연결 전입니다.');
        });
    });

    document.querySelectorAll('.difficult-open-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const row = btn.closest('.teacher-learn-row');
            const title = row.dataset.title || '';
            const difficultCount = Number(row.dataset.difficultCount || '0');
            document.getElementById('difficultLearnTitle').textContent = title;

            const list = document.getElementById('difficultStudentList');
            list.innerHTML = '';

            for (let i = 1; i <= difficultCount; i++) {
                const item = document.createElement('div');
                item.className = 'difficult-student-item';
                item.innerHTML =
                    '<div class="difficult-student-left">' +
                        '<div class="difficult-avatar">학</div>' +
                        '<div>' +
                            '<strong>김학생' + i + '</strong>' +
                            '<p>"어려웠어요" 태그 남김</p>' +
                        '</div>' +
                    '</div>' +
                    '<button type="button" class="mini-line-blue-btn">1:1 메시지</button>';
                list.appendChild(item);
            }

            openModal(learnDifficultModal);
        });
    });

    function makeStudentProgressData() {
        return [
            { name: '김민수', progress: 100, status: '완료', lastAccessed: '2026-03-11 14:30' },
            { name: '이영희', progress: 85, status: '진행중', lastAccessed: '2026-03-11 16:45' },
            { name: '박철수', progress: 100, status: '완료', lastAccessed: '2026-03-10 20:15' },
            { name: '최지혜', progress: 45, status: '진행중', lastAccessed: '2026-03-11 10:20' },
            { name: '정준호', progress: 0, status: '미시작', lastAccessed: '' },
            { name: '강서연', progress: 100, status: '완료', lastAccessed: '2026-03-11 18:00' },
            { name: '윤동현', progress: 0, status: '미시작', lastAccessed: '' },
            { name: '임수진', progress: 60, status: '진행중', lastAccessed: '2026-03-11 12:30' }
        ];
    }

    function renderStudentProgressList(data) {
        const list = document.getElementById('studentProgressList');
        list.innerHTML = '';

        data.forEach(function (student) {
            const fillClass = student.status === '완료' ? 'complete' :
                              student.status === '진행중' ? 'progress' : 'not-started';

            const badgeClass = student.status === '완료' ? 'complete' :
                               student.status === '진행중' ? 'progress' : 'not-started';

            const item = document.createElement('div');
            item.className = 'student-progress-item';
            item.innerHTML =
                '<div class="student-progress-left">' +
                    '<div class="progress-avatar">' + student.name.charAt(0) + '</div>' +
                    '<div class="progress-meta">' +
                        '<strong>' + student.name + '</strong>' +
                        '<p>' + (student.lastAccessed ? '마지막 접근: ' + student.lastAccessed : '아직 시작하지 않음') + '</p>' +
                        '<div class="progress-bar-row">' +
                            '<div class="small-progress-bar">' +
                                '<div class="small-progress-fill ' + fillClass + '" style="width:' + student.progress + '%;"></div>' +
                            '</div>' +
                            '<span class="progress-rate-text">' + student.progress + '%</span>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
                '<span class="status-badge ' + badgeClass + '">' + student.status + '</span>';
            list.appendChild(item);
        });
    }

    document.querySelectorAll('.detail-open-text').forEach(function (titleEl) {
        titleEl.addEventListener('click', function () {
            const row = titleEl.closest('.teacher-learn-row');
            const title = row.dataset.title || '';
            const type = row.dataset.type || '';
            const required = row.dataset.required === 'true';
            const status = row.dataset.status || '';
            const startDate = row.dataset.startDate || '';
            const endDate = row.dataset.endDate || '';
            const deadline = row.dataset.deadline || '';
            const duration = row.dataset.duration || '';
            const linkUrl = row.dataset.linkUrl || '';
            const content = row.dataset.content || '';
            const target = row.dataset.target || '';

            document.getElementById('learnDetailTitle').textContent = title;
            document.getElementById('detailRequiredBadge').textContent = required ? '필수' : '선택';
            document.getElementById('detailRequiredBadge').className = 'mini-badge ' + (required ? 'required' : 'optional');
            document.getElementById('detailTypeBadge').textContent = type;
            document.getElementById('detailStatusBadge').textContent = status;
            document.getElementById('detailStatusBadge').className = 'mini-badge ' + (status === '운영중' ? 'primary' : 'gray');
            document.getElementById('detailPeriodText').textContent = startDate + ' ~ ' + endDate;
            document.getElementById('detailDeadlineText').textContent = deadline;
            document.getElementById('detailDurationText').textContent = duration + '분';
            document.getElementById('detailTargetText').textContent = target;
            document.getElementById('detailContentText').textContent = content || '등록된 설명이 없습니다.';

            const detailLinkBox = document.getElementById('detailLinkBox');
            const detailLinkUrl = document.getElementById('detailLinkUrl');
            if (linkUrl) {
                detailLinkBox.style.display = '';
                detailLinkUrl.href = linkUrl;
                detailLinkUrl.textContent = linkUrl;
            } else {
                detailLinkBox.style.display = 'none';
            }

            const data = makeStudentProgressData();
            const completed = data.filter(function (d) { return d.status === '완료'; }).length;
            const inProgress = data.filter(function (d) { return d.status === '진행중'; }).length;
            const notStarted = data.filter(function (d) { return d.status === '미시작'; }).length;
            const rate = Math.round((completed / data.length) * 100);

            document.getElementById('detailCompletionRate').textContent = rate + '%';
            document.getElementById('detailCompletionBar').style.width = rate + '%';
            document.getElementById('completedCountText').textContent = completed + '명';
            document.getElementById('inProgressCountText').textContent = inProgress + '명';
            document.getElementById('notStartedCountText').textContent = notStarted + '명';
            document.getElementById('studentProgressCount').textContent = data.length;

            renderStudentProgressList(data);
            openModal(learnDetailModal);
        });
    });
>>>>>>> refs/remotes/origin/brunch1
});