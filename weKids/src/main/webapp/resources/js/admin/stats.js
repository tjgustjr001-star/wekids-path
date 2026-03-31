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
            console.error('[admin stats] JSON parse error:', id, e);
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
                ticks: { color: '#94a3b8', font: { size: 10 } },
                grid: { color: 'rgba(71, 85, 105, 0.25)' },
                border: { color: 'rgba(71, 85, 105, 0.35)' }
            },
            y: {
                beginAtZero: true,
                ticks: { color: '#94a3b8', font: { size: 10 } },
                grid: { color: 'rgba(71, 85, 105, 0.25)' },
                border: { color: 'rgba(71, 85, 105, 0.35)' }
            }
        };
    }

    const userGrowthData = parseJsonScript('adminUserGrowthData');
    const userTypeData = parseJsonScript('adminUserTypeData');
    const assignmentRateData = parseJsonScript('adminAssignmentRateData');
    const learnCompletionData = parseJsonScript('adminLearnCompletionData');

    createChart(getEl('adminUserGrowthChart'), {
        type: 'line',
        data: {
            labels: userGrowthData.map(function (item) { return item.label; }),
            datasets: [
                {
                    label: '학생',
                    data: userGrowthData.map(function (item) { return item.studentCount; }),
                    borderColor: '#84cc16',
                    backgroundColor: '#84cc16',
                    borderWidth: 3,
                    tension: 0.3,
                    pointRadius: 4,
                    pointHoverRadius: 5,
                    fill: false
                },
                {
                    label: '학부모',
                    data: userGrowthData.map(function (item) { return item.parentCount; }),
                    borderColor: '#a855f7',
                    backgroundColor: '#a855f7',
                    borderWidth: 3,
                    tension: 0.3,
                    pointRadius: 4,
                    pointHoverRadius: 5,
                    fill: false
                },
                {
                    label: '교사',
                    data: userGrowthData.map(function (item) { return item.teacherCount; }),
                    borderColor: '#5b86ff',
                    backgroundColor: '#5b86ff',
                    borderWidth: 3,
                    tension: 0.3,
                    pointRadius: 4,
                    pointHoverRadius: 5,
                    fill: false
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: { mode: 'index', intersect: false },
            plugins: {
                legend: {
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

    createChart(getEl('adminUserTypeChart'), {
        type: 'pie',
        data: {
            labels: userTypeData.map(function (item) { return item.name; }),
            datasets: [{
                data: userTypeData.map(function (item) { return item.value; }),
                backgroundColor: userTypeData.map(function (item) { return item.color; }),
                borderColor: '#1e293b',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#0f172a',
                    borderColor: '#334155',
                    borderWidth: 1,
                    titleColor: '#f8fafc',
                    bodyColor: '#e2e8f0'
                }
            }
        }
    });

    createChart(getEl('adminAssignmentRateChart'), {
        type: 'bar',
        data: {
            labels: assignmentRateData.map(function (item) { return item.className; }),
            datasets: [
                {
                    label: '제출',
                    data: assignmentRateData.map(function (item) { return item.submittedCount; }),
                    backgroundColor: '#6ad39c',
                    borderRadius: 6,
                    stack: 'assignment'
                },
                {
                    label: '미제출',
                    data: assignmentRateData.map(function (item) { return item.unsubmittedCount; }),
                    backgroundColor: '#d95b53',
                    borderRadius: 6,
                    stack: 'assignment'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
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

    createChart(getEl('adminLearnCompletionChart'), {
        type: 'bar',
        data: {
            labels: learnCompletionData.map(function (item) { return item.className; }),
            datasets: [
                {
                    label: '완료',
                    data: learnCompletionData.map(function (item) { return item.completedCount; }),
                    backgroundColor: '#5b86ff',
                    borderRadius: 6
                },
                {
                    label: '미완료',
                    data: learnCompletionData.map(function (item) { return item.incompleteCount; }),
                    backgroundColor: '#475569',
                    borderRadius: 6
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
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

    const downloadBtn = getEl('downloadStatsReportBtn');
    if (downloadBtn) {
        downloadBtn.addEventListener('click', function () {
            alert('리포트 다운로드 기능은 추후 연결 예정입니다.');
        });
    }
});