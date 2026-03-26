window.addEventListener('DOMContentLoaded', function () {
    function getEl(id) {
        return document.getElementById(id);
    }

    function getValueNumber(id) {
        const el = getEl(id);
        return Number(el ? el.value : 0);
    }

    function getTrimmedValue(id) {
        const el = getEl(id);
        if (!el || el.value == null) return '';
        return String(el.value).trim();
    }

    function destroyChartIfExists(canvas) {
        if (!canvas || typeof Chart === 'undefined') return;
        const oldChart = Chart.getChart(canvas);
        if (oldChart) {
            oldChart.destroy();
        }
    }

    function createChart(canvas, config) {
        if (!canvas || typeof Chart === 'undefined') return null;
        destroyChartIfExists(canvas);
        return new Chart(canvas, config);
    }

    function closeAllMenus(selector) {
        document.querySelectorAll(selector).forEach(function (menu) {
            menu.classList.remove('open');
        });
    }

    function setupToggleMenu(buttonSelector, menuSelector) {
        const buttons = document.querySelectorAll(buttonSelector);

        buttons.forEach(function (button) {
            button.addEventListener('click', function (e) {
                e.stopPropagation();

                const menu = button.nextElementSibling;
                if (!menu || !menu.matches(menuSelector)) return;

                document.querySelectorAll(menuSelector).forEach(function (item) {
                    if (item !== menu) {
                        item.classList.remove('open');
                    }
                });

                menu.classList.toggle('open');
            });
        });
    }

    function setupOutsideClose(menuSelector, modalSelectorList) {
        document.addEventListener('click', function (e) {
            const clickedInsideExcludedModal = modalSelectorList.some(function (selector) {
                const modal = document.querySelector(selector);
                return modal && modal.contains(e.target);
            });

            if (!clickedInsideExcludedModal) {
                closeAllMenus(menuSelector);
            }
        });
    }

    function openModal(modal) {
        if (modal) {
            modal.classList.add('show');
        }
    }

    function closeModal(modal) {
        if (modal) {
            modal.classList.remove('show');
        }
    }

    function bindModalClose(modal, closeBtn, cancelBtn) {
        if (closeBtn) {
            closeBtn.addEventListener('click', function () {
                closeModal(modal);
            });
        }

        if (cancelBtn) {
            cancelBtn.addEventListener('click', function () {
                closeModal(modal);
            });
        }

        if (modal) {
            modal.addEventListener('click', function (e) {
                if (e.target === modal) {
                    closeModal(modal);
                }
            });
        }
    }

    function setBadgeClass(target, baseClass, status) {
        if (!target) return;

        target.className = baseClass;

        if (status === 'ACTIVE') {
            target.classList.add('active');
        } else if (status === 'ARCHIVED') {
            target.classList.add('archive');
        } else if (status === 'BLINDED') {
            target.classList.add('blind');
        } else if (status === 'DORMANT') {
            target.classList.add('dormant');
        } else if (status === 'SUSPENDED') {
            target.classList.add('suspended');
        } else if (status === 'DELETED') {
            target.classList.add('deleted');
        }
    }

    function getStatusText(status) {
        if (status === 'ACTIVE') return '활성';
        if (status === 'ARCHIVED') return '아카이브';
        if (status === 'BLINDED') return '블라인드';
        if (status === 'DORMANT') return '휴면';
        if (status === 'SUSPENDED') return '정지';
        if (status === 'DELETED') return '탈퇴';
        return status || '';
    }

    function filterElements(rows, options) {
        let visibleCount = 0;

        rows.forEach(function (row) {
            const isVisible = options.matcher(row);

            row.style.display = isVisible ? '' : 'none';

            if (isVisible) {
                visibleCount++;
            }
        });

        if (options.countTarget) {
            options.countTarget.textContent = String(visibleCount);
        }
    }

    /* =========================
       대시보드 차트
    ========================= */
    createChart(getEl('weeklyTrendChart'), {
        type: 'line',
        data: {
            labels: ['월', '화', '수', '목', '금', '토', '일'],
            datasets: [{
                label: '접속자 수',
                data: [4920, 8610, 5535, 11070, 7995, 3690, 2460],
                borderColor: '#4379F2',
                backgroundColor: '#4379F2',
                borderWidth: 3,
                tension: 0.35,
                pointRadius: 5,
                pointHoverRadius: 6,
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55, 65, 81, 0.5)' }
                },
                y: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55, 65, 81, 0.5)' }
                }
            }
        }
    });

    /* =========================
       클래스 관리
    ========================= */
    createChart(getEl('classStatusChart'), {
        type: 'doughnut',
        data: {
            labels: ['활성 클래스', '아카이브', '블라인드'],
            datasets: [{
                data: [
                    getValueNumber('activeClassCount'),
                    getValueNumber('archiveClassCount'),
                    getValueNumber('blindedClassCount')
                ],
                backgroundColor: ['#00d37a', '#9DB2BF', '#FF7777'],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '65%',
            plugins: {
                legend: { display: false }
            }
        }
    });

    const classTabButtons = document.querySelectorAll('.tab-btn');
    const classCards = document.querySelectorAll('.class-card');
    const classSearchInput = getEl('classSearchInput');
    const classDetailModal = getEl('classDetailModal');
    const closeClassModalBtn = getEl('closeClassModalBtn');
    const detailButtons = document.querySelectorAll('.open-detail-btn');
    const modalActivateBtn = getEl('modalActivateBtn');
    const modalBlindBtn = getEl('modalBlindBtn');

    const modalClassName = getEl('modalClassName');
    const modalClassStatus = getEl('modalClassStatus');
    const modalClassId = getEl('modalClassId');
    const modalTeacher = getEl('modalTeacher');
    const modalSchool = getEl('modalSchool');
    const modalStudents = getEl('modalStudents');
    const modalLastActive = getEl('modalLastActive');
    const modalDescription = getEl('modalDescription');

    let currentClassFilter = '전체';
    let currentOpenedCard = null;

    function filterClassCards() {
        const keyword = classSearchInput ? classSearchInput.value.trim().toLowerCase() : '';

        filterElements(classCards, {
            matcher: function (card) {
                const status = card.getAttribute('data-status') || '';
                const name = (card.getAttribute('data-name') || '').toLowerCase();
                const teacher = (card.getAttribute('data-teacher') || '').toLowerCase();

                const matchFilter = currentClassFilter === '전체' || status === currentClassFilter;
                const matchSearch = name.includes(keyword) || teacher.includes(keyword);

                return matchFilter && matchSearch;
            }
        });
    }

    function updateClassCardStatus(card, status) {
        if (!card) return;

        card.setAttribute('data-status', status);

        const badge = card.querySelector('.badge');
        if (badge) {
            badge.textContent = getStatusText(status);
            setBadgeClass(badge, 'badge', status);
        }

        const detailBtn = card.querySelector('.open-detail-btn');
        if (detailBtn) {
            detailBtn.setAttribute('data-status', status);
        }
    }

    classTabButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            classTabButtons.forEach(function (item) {
                item.classList.remove('active');
            });

            btn.classList.add('active');
            currentClassFilter = btn.getAttribute('data-filter') || '전체';
            filterClassCards();
        });
    });

    if (classSearchInput) {
        classSearchInput.addEventListener('input', filterClassCards);
    }

    detailButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            currentOpenedCard = btn.closest('.class-card');

            const name = btn.getAttribute('data-name') || '';
            const status = btn.getAttribute('data-status') || '';
            const id = btn.getAttribute('data-id') || '';
            const teacher = btn.getAttribute('data-teacher') || '';
            const school = btn.getAttribute('data-school') || '';
            const students = btn.getAttribute('data-students') || '';
            const lastActive = btn.getAttribute('data-last-active') || '';
            const description = btn.getAttribute('data-description') || '';

            if (modalClassName) modalClassName.textContent = name;
            if (modalClassStatus) {
                modalClassStatus.textContent = getStatusText(status);
                setBadgeClass(modalClassStatus, 'badge', status);
            }
            if (modalClassId) modalClassId.textContent = '코드: ' + id;
            if (modalTeacher) modalTeacher.textContent = teacher;
            if (modalSchool) modalSchool.textContent = school;
            if (modalStudents) modalStudents.textContent = students + '명';
            if (modalLastActive) modalLastActive.textContent = lastActive;
            if (modalDescription) modalDescription.textContent = description;

            openModal(classDetailModal);
        });
    });

    if (modalActivateBtn) {
        modalActivateBtn.addEventListener('click', function () {
            updateClassCardStatus(currentOpenedCard, 'ACTIVE');

            if (modalClassStatus) {
                modalClassStatus.textContent = '활성';
                setBadgeClass(modalClassStatus, 'badge', 'ACTIVE');
            }

            closeModal(classDetailModal);
            filterClassCards();
        });
    }

    if (modalBlindBtn) {
        modalBlindBtn.addEventListener('click', function () {
            updateClassCardStatus(currentOpenedCard, 'BLINDED');

            if (modalClassStatus) {
                modalClassStatus.textContent = '블라인드';
                setBadgeClass(modalClassStatus, 'badge', 'BLINDED');
            }

            closeModal(classDetailModal);
            filterClassCards();
        });
    }

    bindModalClose(classDetailModal, closeClassModalBtn, null);

    /* =========================
       교사 관리
    ========================= */
    createChart(getEl('teacherActivityChart'), {
        type: 'bar',
        data: {
            labels: ['1주', '2주', '3주', '4주'],
            datasets: [
                {
                    label: '과제등록',
                    data: [4, 5, 3, 6],
                    backgroundColor: '#4379F2',
                    borderRadius: 4,
                    barThickness: 20
                },
                {
                    label: '피드백',
                    data: [12, 15, 8, 20],
                    backgroundColor: '#00d37a',
                    borderRadius: 4,
                    barThickness: 20
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55,65,81,0.15)' }
                },
                y: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55,65,81,0.35)' }
                }
            }
        }
    });

    const teacherTrendCanvas = getEl('teacherTrendChart');
    if (teacherTrendCanvas && typeof Chart !== 'undefined') {
        destroyChartIfExists(teacherTrendCanvas);

        const ctx = teacherTrendCanvas.getContext('2d');
        const gradient = ctx.createLinearGradient(0, 0, 0, 220);
        gradient.addColorStop(0, 'rgba(67, 121, 242, 0.30)');
        gradient.addColorStop(1, 'rgba(67, 121, 242, 0)');

        new Chart(teacherTrendCanvas, {
            type: 'line',
            data: {
                labels: ['9월', '10월', '11월', '12월', '1월', '2월', '3월'],
                datasets: [{
                    label: '신규 교사',
                    data: [12, 19, 15, 8, 24, 38, 45],
                    borderColor: '#4379F2',
                    backgroundColor: gradient,
                    fill: true,
                    tension: 0.35,
                    borderWidth: 3,
                    pointRadius: 4,
                    pointHoverRadius: 5
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    x: {
                        ticks: { color: '#9CA3AF' },
                        grid: { color: 'rgba(55, 65, 81, 0.2)', drawBorder: false }
                    },
                    y: {
                        ticks: { color: '#9CA3AF' },
                        grid: { color: 'rgba(55, 65, 81, 0.4)', drawBorder: false }
                    }
                }
            }
        });
    }

    const teacherRows = document.querySelectorAll('.teacher-row');
    const teacherSearchInput = getEl('teacherSearchInput');
    const teacherStatusFilter = getEl('teacherStatusFilter');
    const teacherSchoolFilter = getEl('teacherSchoolFilter');
    const teacherTotalCount = getEl('teacherTotalCount');

    function filterTeacherRows() {
        const keyword = teacherSearchInput ? teacherSearchInput.value.trim().toLowerCase() : '';
        const statusValue = teacherStatusFilter ? teacherStatusFilter.value : '전체';
        const schoolValue = teacherSchoolFilter ? teacherSchoolFilter.value : '전체';

        filterElements(teacherRows, {
            countTarget: teacherTotalCount,
            matcher: function (row) {
                const name = (row.getAttribute('data-name') || '').toLowerCase();
                const email = (row.getAttribute('data-email') || '').toLowerCase();
                const school = row.getAttribute('data-school') || '';
                const status = row.getAttribute('data-status') || '';

                const matchKeyword =
                    name.includes(keyword) ||
                    email.includes(keyword) ||
                    school.toLowerCase().includes(keyword);

                const matchStatus = statusValue === '전체' || status === statusValue;
                const matchSchool = schoolValue === '전체' || school === schoolValue;

                return matchKeyword && matchStatus && matchSchool;
            }
        });
    }

    if (teacherSearchInput) {
        teacherSearchInput.addEventListener('input', filterTeacherRows);
    }
    if (teacherStatusFilter) {
        teacherStatusFilter.addEventListener('change', filterTeacherRows);
    }
    if (teacherSchoolFilter) {
        teacherSchoolFilter.addEventListener('change', filterTeacherRows);
    }

    const addTeacherModal = getEl('addTeacherModal');
    const openAddTeacherModalBtn = getEl('openAddTeacherModalBtn');
    const closeAddTeacherModalBtn = getEl('closeAddTeacherModalBtn');
    const cancelAddTeacherBtn = getEl('cancelAddTeacherBtn');

    if (openAddTeacherModalBtn) {
        openAddTeacherModalBtn.addEventListener('click', function () {
            openModal(addTeacherModal);
        });
    }

    bindModalClose(addTeacherModal, closeAddTeacherModalBtn, cancelAddTeacherBtn);

    function bindTeacherFormValidation() {
        const addTeacherForm = getEl('addTeacherForm');
        if (!addTeacherForm) return;

        addTeacherForm.addEventListener('submit', function (e) {
            const teacherName = getTrimmedValue('teacherNameInput');
            const loginId = getTrimmedValue('teacherLoginIdInput');
            const email = getTrimmedValue('teacherEmailInput');
            const password = getTrimmedValue('teacherPasswordInput');

            if (!teacherName || !loginId || !email || !password) {
                e.preventDefault();
                alert('교사 계정 생성 필수 정보를 모두 입력해주세요.');
                return;
            }
        });
    }

    const downloadTeacherListBtn = getEl('downloadTeacherListBtn');
    if (downloadTeacherListBtn) {
        downloadTeacherListBtn.addEventListener('click', function () {
            alert('목록이 다운로드 되었습니다.');
        });
    }

    /* =========================
       사용자 관리
    ========================= */
    createChart(getEl('userRoleChart'), {
        type: 'bar',
        data: {
            labels: ['학생', '학부모'],
            datasets: [{
                data: [
                    getValueNumber('studentUserCount'),
                    getValueNumber('parentUserCount')
                ],
                backgroundColor: ['#4379F2', '#9DB2BF'],
                borderRadius: 6,
                barThickness: 28
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55, 65, 81, 0.15)' }
                },
                y: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55, 65, 81, 0.35)' }
                }
            }
        }
    });

    createChart(getEl('userWeeklyActivityChart'), {
        type: 'line',
        data: {
            labels: ['월', '화', '수', '목', '금', '토', '일'],
            datasets: [{
                label: '활동량',
                data: [2, 4, 3, 5, 4, 1, 2],
                borderColor: '#4379F2',
                backgroundColor: '#4379F2',
                borderWidth: 3,
                tension: 0.35,
                pointRadius: 4,
                fill: false
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55, 65, 81, 0.2)' }
                },
                y: {
                    ticks: { color: '#9CA3AF' },
                    grid: { color: 'rgba(55, 65, 81, 0.35)' }
                }
            }
        }
    });

    const userRows = document.querySelectorAll('.user-row');
    const userTabButtons = document.querySelectorAll('.user-tab-btn');
    const userSearchInput = getEl('userSearchInput');
    const userCountText = getEl('userCountText');

    let currentUserRoleFilter = '전체';

    function filterUserRows() {
        const keyword = userSearchInput ? userSearchInput.value.trim().toLowerCase() : '';

        filterElements(userRows, {
            countTarget: userCountText,
            matcher: function (row) {
                const role = row.getAttribute('data-role') || '';
                const name = (row.getAttribute('data-name') || '').toLowerCase();
                const loginId = (row.getAttribute('data-login-id') || '').toLowerCase();
                const email = (row.getAttribute('data-email') || '').toLowerCase();

                const matchRole = currentUserRoleFilter === '전체' || role === currentUserRoleFilter;
                const matchKeyword =
                    name.includes(keyword) ||
                    loginId.includes(keyword) ||
                    email.includes(keyword);

                return matchRole && matchKeyword;
            }
        });
    }

    userTabButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            userTabButtons.forEach(function (item) {
                item.classList.remove('active');
            });

            btn.classList.add('active');
            currentUserRoleFilter = btn.getAttribute('data-filter') || '전체';
            filterUserRows();
        });
    });

    if (userSearchInput) {
        userSearchInput.addEventListener('input', filterUserRows);
    }

    const createStudentModal = getEl('createStudentModal');
    const openCreateStudentModalBtn = getEl('openCreateStudentModalBtn');
    const closeCreateStudentModalBtn = getEl('closeCreateStudentModalBtn');
    const cancelCreateStudentBtn = getEl('cancelCreateStudentBtn');

    if (openCreateStudentModalBtn) {
        openCreateStudentModalBtn.addEventListener('click', function () {
            openModal(createStudentModal);
        });
    }

    bindModalClose(createStudentModal, closeCreateStudentModalBtn, cancelCreateStudentBtn);

    function bindStudentFormValidation() {
        const createStudentForm = getEl('createStudentForm');
        if (!createStudentForm) return;

        createStudentForm.addEventListener('submit', function (e) {
            const studentName = getTrimmedValue('studentNameInput');
            const loginId = getTrimmedValue('studentLoginIdInput');
            const email = getTrimmedValue('studentEmailInput');
            const password = getTrimmedValue('studentPasswordInput');

            if (!studentName || !loginId || !email || !password) {
                e.preventDefault();
                alert('학생 계정 생성 필수 정보를 모두 입력해주세요.');
                return;
            }
        });
    }

    /* =========================
       검증 바인딩 실행
    ========================= */
    bindTeacherFormValidation();
    bindStudentFormValidation();

    /* =========================
       공통 메뉴 토글
    ========================= */
    setupToggleMenu('.menu-toggle-btn', '.card-menu');
    setupToggleMenu('.row-menu-toggle-btn', '.table-row-menu');
    setupOutsideClose('.card-menu, .table-row-menu', ['#classDetailModal', '#addTeacherModal', '#createStudentModal']);
});