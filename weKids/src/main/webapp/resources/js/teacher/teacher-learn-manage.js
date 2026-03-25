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

    const learnIdInput = document.getElementById('learnId');
    const classIdInput = document.getElementById('classId');

    const trashMode = window.learnTrashMode === true || window.learnTrashMode === 'true';
    const classId = classIdInput ? classIdInput.value : '';
	
	const contextPath = window.appContextPath || '';
	window.appContextPath = '${pageContext.request.contextPath}';

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
        if (!learnType) return;

        const value = learnType.value;
        if (learnLinkField) learnLinkField.style.display = (value === '영상' || value === '링크') ? '' : 'none';
        if (learnTextField) learnTextField.style.display = (value === '지문읽기') ? '' : 'none';
        if (learnFileField) learnFileField.style.display = (value === '파일') ? '' : 'none';
    }

    if (learnType) {
        learnType.addEventListener('change', updateTypeFields);
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

            updateTypeFields();
            openModal(learnFormModal);
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
            document.getElementById('learnStatus').value = row.dataset.status || '대기중';
            document.getElementById('learnDuration').value = row.dataset.duration || '';
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
            if (e) e.stopPropagation();

            const row = btn.closest('.teacher-learn-row');

            document.getElementById('learnDetailTitle').textContent = row.dataset.title || '';
            document.getElementById('detailRequiredBadge').textContent = row.dataset.required === 'true' ? '필수' : '선택';
            document.getElementById('detailRequiredBadge').className = 'mini-badge ' + (row.dataset.required === 'true' ? 'required' : 'optional');
            document.getElementById('detailTypeBadge').textContent = row.dataset.type || '';
            document.getElementById('detailStatusBadge').textContent = row.dataset.status || '';
            document.getElementById('detailPeriodText').textContent = (row.dataset.startDate || '') + ' ~ ' + (row.dataset.endDate || '');
            document.getElementById('detailDeadlineText').textContent = row.dataset.deadline || '';
            document.getElementById('detailDurationText').textContent = (row.dataset.duration || '0') + '분';
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
            if (titleEl) {
                titleEl.textContent = row.dataset.title || '';
            }

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
                const visible = title.indexOf(keyword) > -1 || content.indexOf(keyword) > -1;
                row.style.display = visible ? '' : 'none';
            });
        });
    }

    if (trashMode && openCreateBtn) {
        openCreateBtn.style.display = 'none';
    }
});