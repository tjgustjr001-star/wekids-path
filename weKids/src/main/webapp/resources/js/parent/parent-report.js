window.addEventListener('DOMContentLoaded', function () {
    const tabButtons = document.querySelectorAll('.parent-report-tab-btn');
    const childSelect = document.getElementById('parentReportChildSelect');
    const childPanels = document.querySelectorAll('.parent-report-child-panel');
    const rateBox = document.getElementById('parentReportRateBox');

    let activeTab = 'weekly';

    function updateRateText() {
        const selectedChildId = childSelect ? childSelect.value : '1';
        const activeChildPanel = document.querySelector('.parent-report-child-panel[data-child-id="' + selectedChildId + '"]');
        if (!activeChildPanel) return;

        const activePeriodPanel = activeChildPanel.querySelector('.parent-report-period-panel[data-tab="' + activeTab + '"]');
        if (!activePeriodPanel || !rateBox) return;

        const childName = activePeriodPanel.dataset.childName || '';
        const tabLabel = activePeriodPanel.dataset.tabLabel || '';
        const rate = activePeriodPanel.dataset.overallRate || '0';

        rateBox.innerHTML =
            '<span class="parent-report-star-icon"></span>' +
            '<div class="parent-report-rate-text">' + childName + '의 ' + tabLabel + ' 과제 제출율: ' + rate + '%</div>';
    }

    function updatePanels() {
        const selectedChildId = childSelect ? childSelect.value : '1';

        childPanels.forEach(function (childPanel) {
            const isChildActive = childPanel.dataset.childId === selectedChildId;
            childPanel.classList.toggle('active', isChildActive);

            const periodPanels = childPanel.querySelectorAll('.parent-report-period-panel');
            periodPanels.forEach(function (periodPanel) {
                const isPeriodActive = isChildActive && periodPanel.dataset.tab === activeTab;
                periodPanel.classList.toggle('active', isPeriodActive);
            });
        });

        updateRateText();
    }

    tabButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            activeTab = btn.dataset.tab || 'weekly';

            tabButtons.forEach(function (button) {
                button.classList.remove('active');
            });
            btn.classList.add('active');

            updatePanels();
        });
    });

    if (childSelect) {
        childSelect.addEventListener('change', function () {
            updatePanels();
        });
    }

    updatePanels();
});