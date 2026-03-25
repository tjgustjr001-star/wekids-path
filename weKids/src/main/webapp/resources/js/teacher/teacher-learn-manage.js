window.addEventListener('DOMContentLoaded', function () {
    const rows = document.querySelectorAll('.teacher-learn-row');
    const searchInput = document.getElementById('learnSearchInput');
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
});