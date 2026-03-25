window.addEventListener('DOMContentLoaded', function () {
    const tabButtons = document.querySelectorAll('.student-report-tab-btn');
    const panels = document.querySelectorAll('.student-report-panel');
    const rateBadge = document.getElementById('studentReportRateBadge');

    function activateTab(tab) {
        tabButtons.forEach(function (btn) {
            btn.classList.toggle('active', btn.dataset.tab === tab);
        });

        panels.forEach(function (panel) {
            const isActive = panel.dataset.tab === tab;
            panel.classList.toggle('active', isActive);

            if (isActive && rateBadge) {
                const label = panel.dataset.tabLabel || '';
                const rate = panel.dataset.overallRate || '0';
                rateBadge.innerHTML =
                    '<span class="student-report-star-icon"></span>' +
                    '<span>' + label + ' 과제 제출율: ' + rate + '%</span>';
            }
        });
    }

    tabButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            activateTab(btn.dataset.tab);
        });
    });

    activateTab('weekly');
});