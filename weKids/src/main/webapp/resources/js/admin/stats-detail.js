window.addEventListener('DOMContentLoaded', function () {
    function getEl(id) {
        return document.getElementById(id);
    }

    function parseJsonScript(id) {
        const el = getEl(id);
        if (!el) return [];
        const raw = (el.textContent || '').trim();
        if (!raw) return [];
        try {
            return JSON.parse(raw);
        } catch (e) {
            console.error('[admin stat detail] JSON parse error:', id, e);
            return [];
        }
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

    function defaultScaleOptions() {
        return {
            x: {
                ticks: { color: '#94a3b8', font: { size: 12 } },
                grid: { color: 'rgba(71, 85, 105, 0.25)' },
                border: { color: 'rgba(71, 85, 105, 0.35)' }
            },
            y: {
                beginAtZero: true,
                ticks: { color: '#94a3b8', font: { size: 12 } },
                grid: { color: 'rgba(71, 85, 105, 0.25)' },
                border: { color: 'rgba(71, 85, 105, 0.35)' }
            }
        };
    }

    const submissionTrendData = parseJsonScript('adminClassSubmissionTrendData');
    const classLearnStatusData = parseJsonScript('adminClassLearnStatusData');

    createChart(getEl('adminClassSubmissionTrendChart'), {
        type: 'bar',
        data: {
            labels: submissionTrendData.map(function (item) { return item.label; }),
            datasets: [
                {
                    label: '제출',
                    data: submissionTrendData.map(function (item) { return item.submittedCount; }),
                    backgroundColor: '#5b86ff',
                    borderRadius: 6,
                    stack: 'submission'
                },
                {
                    label: '미제출',
                    data: submissionTrendData.map(function (item) { return item.unsubmittedCount; }),
                    backgroundColor: '#f08a84',
                    borderRadius: 6,
                    stack: 'submission'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
				
                legend: {
					position: 'bottom',
                    labels: { color: '#cbd5e1', boxWidth: 12, boxHeight: 12 }
                },
                tooltip: {
                    backgroundColor: '#0f172a',
                    borderColor: '#334155',
                    borderWidth: 1,
                    titleColor: '#f8fafc',
                    bodyColor: '#e2e8f0'
                }
            },
            scales: defaultScaleOptions()
        }
    });

    createChart(getEl('adminClassLearnStatusChart'), {
        type: 'bar',
        data: {
            labels: classLearnStatusData.map(function (item) { return item.label; }),
            datasets: [
                {
                    label: '완료',
                    data: classLearnStatusData.map(function (item) { return item.completedCount; }),
                    backgroundColor: '#5b86ff',
                    borderRadius: 6
                },
                {
                    label: '미완료',
                    data: classLearnStatusData.map(function (item) { return item.incompleteCount; }),
                    backgroundColor: '#334155',
                    borderRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
				
                legend: {
					position: 'bottom',
                    labels: { color: '#cbd5e1', boxWidth: 12, boxHeight: 12 }
                },
                tooltip: {
                    backgroundColor: '#0f172a',
                    borderColor: '#334155',
                    borderWidth: 1,
                    titleColor: '#f8fafc',
                    bodyColor: '#e2e8f0'
                }
            },
            scales: defaultScaleOptions()
        }
    });
});