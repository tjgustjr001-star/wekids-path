<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 상태별 카운트 집계 --%>
<c:set var="pendingCount" value="0" />
<c:set var="answeredCount" value="0" />
<c:set var="reportCount" value="0" />

<c:forEach items="${supportList}" var="s">
	<c:choose>
		<c:when test="${s.status eq '대기중'}">
			<c:set var="pendingCount" value="${pendingCount + 1}" />
		</c:when>
		<c:when test="${s.status eq '답변완료'}">
			<c:set var="answeredCount" value="${answeredCount + 1}" />
		</c:when>
	</c:choose>
	<c:if test="${s.category eq 'report'}">
		<c:set var="reportCount" value="${reportCount + 1}" />
	</c:if>

</c:forEach>

<c:set var="totalCount" value="${fn:length(supportList)}" />

<div class="admin-support-page">
	<%-- ── 페이지 헤더 ── --%>
	<div class="page-header-row">
		<div>
			<h1 class="page-title">고객지원 관리</h1>
			<p class="page-desc">사용자 문의 및 신고 내역을 확인하고 답변을 등록합니다.</p>
		</div>
		<div class="page-header-actions">
			<button type="button" class="primary-btn" onclick="location.href='${pageContext.request.contextPath}/admin/supportFaq'">FAQ 관리</button>
				
		</div>
	</div>

	<%-- ── 통계 카드 ── --%>
	<div class="support-stat-grid">
		<section class="panel-card">
			<div class="support-stat-item">
				<span class="support-stat-label">전체 문의</span> <strong
					class="support-stat-value">${totalCount}<span
					class="support-stat-unit">건</span></strong>
			</div>
		</section>
		<section class="panel-card">
			<div class="support-stat-item">
				<span class="support-stat-label">답변 대기</span> <strong
					class="support-stat-value pending">${pendingCount}<span
					class="support-stat-unit">건</span></strong>
			</div>
		</section>
		<section class="panel-card">
			<div class="support-stat-item">
				<span class="support-stat-label">답변 완료</span> <strong
					class="support-stat-value answered">${answeredCount}<span
					class="support-stat-unit">건</span></strong>
			</div>
		</section>
		<section class="panel-card">
			<div class="support-stat-item">
				<span class="support-stat-label">신고</span> <strong
					class="support-stat-value report">${reportCount}<span
					class="support-stat-unit">건</span></strong>
			</div>
		</section>
	</div>

	<%-- ── 주간 차트 ── --%>
	<section class="panel-card support-chart-panel">
		<h2 class="panel-title">주간 문의 처리 현황</h2>
		<div class="chart-wrap">
			<canvas id="supportWeeklyChart"></canvas>
		</div>
	</section>

	<%-- ── 문의 목록 ── --%>
	<section class="users-board">
		<div class="users-board-top">
			<div class="tab-group">
				<button type="button" class="support-tab-btn active"
					data-filter="전체">전체</button>
				<button type="button" class="support-tab-btn" data-filter="대기중">답변
					대기</button>
				<button type="button" class="support-tab-btn" data-filter="답변완료">답변
					완료</button>
			</div>

			<div class="support-board-right">
				<select class="support-select" id="supportCategoryFilter">
					<option value="">유형 전체</option>
					<option value="payment">결제 / 환불</option>
					<option value="class">수업 / 클래스</option>
					<option value="account">계정 / 로그인</option>
					<option value="tech">오류 / 버그</option>
					<option value="report">신고</option>
					<option value="other">기타</option>
				</select>

				<div class="search-box">
					<input type="text" id="supportSearchInput" placeholder="제목, 작성자 검색">
				</div>
			</div>
		</div>

		<div class="user-table-wrap">
			<table class="user-table" id="supportTable">
				<thead>
					<tr>
						<th>상태</th>
						<th>유형</th>
						<th>제목</th>
						<th>작성자</th>
						<th>작성일</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${supportList}" var="s">
						<tr class="user-row support-row" data-status="${s.status}"
							data-category="${s.category}" data-title="${s.title}"
							data-writer="${s.writerId}">
							<td><span
								class="status-pill ${s.status eq '대기중' ? 'dormant' : s.category eq '신고' ? 'suspended' : 'active'}">
									<c:choose>
										<c:when test="${s.status eq '대기중'}">답변 대기</c:when>
										<c:when test="${s.status eq '신고'}">신고</c:when>
										<c:otherwise>답변 완료</c:otherwise>
									</c:choose>
							</span></td>
							<td><span class="role-pill category"> <c:choose>
										<c:when test="${fn:toLowerCase(s.category) eq 'payment'}">결제 / 환불</c:when>
										<c:when test="${fn:toLowerCase(s.category) eq 'class'}">수업 / 클래스</c:when>
										<c:when test="${fn:toLowerCase(s.category) eq 'account'}">계정 / 로그인</c:when>
										<c:when test="${fn:toLowerCase(s.category) eq 'tech'}">오류 / 버그</c:when>
										<c:when test="${fn:toLowerCase(s.category) eq 'report'}">신고</c:when>
										<c:when test="${fn:toLowerCase(s.category) eq 'other'}">기타</c:when>




										<c:otherwise>${fn:toLowerCase(s.category)}</c:otherwise>
									</c:choose>
							</span></td>
							<td class="user-name-cell support-title-cell">${s.title}</td>
							<td>${s.writerId}</td>
							<td><fmt:formatDate value="${s.createdAt}"
									pattern="yyyy.MM.dd HH:mm" /></td>
							<td class="manage-cell">
								<div class="table-action-wrap">
									<c:choose>
										<c:when test="${s.status eq '답변완료'}">
											<a
												href="${pageContext.request.contextPath}/admin/support/detail?supportNo=${s.supportNo}"
												class="detail-link-btn">상세 보기</a>
										</c:when>
										<c:otherwise>
											<a
												href="${pageContext.request.contextPath}/admin/support/answer?supportNo=${s.supportNo}"
												class="detail-link-btn primary">답변하기</a>
										</c:otherwise>
									</c:choose>
								</div>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty supportList}">
						<tr>
							<td colspan="6" style="text-align: center; padding: 30px;">등록된
								문의가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>

		<div class="users-board-footer">
			<span>총 <span id="supportCountText">${totalCount}</span>건</span>
			<div class="support-pagination" id="paginationWrap"></div>
		</div>
	</section>
</div>

<%-- ── Chart.js + 필터 스크립트 ── --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function () {

    /* 1. 주간 차트 생성 */
    const ctx = document.getElementById('supportWeeklyChart');
    if (ctx) {
        const labels = [];
        const completedData = [];
        const pendingData = [];

        // 데이터 바인딩 (별칭 대소문자 주의)
        <c:forEach var="stat" items="${weeklyStats}">
            labels.push("${stat.TARGETDATE}"); 
            completedData.push(${stat.COMPLETEDCOUNT});
            pendingData.push(${stat.PENDINGCOUNT});
        </c:forEach>

        if (labels.length > 0) {
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [
                        {
                            label: '답변 완료',
                            data: completedData,
                            borderColor: '#4ade80',
                            backgroundColor: 'rgba(74,222,128,0.07)',
                            tension: 0.45, fill: true, borderWidth: 2.5, pointBackgroundColor: '#4ade80'
                        },
                        {
                            label: '답변 대기',
                            data: pendingData,
                            borderColor: '#facc15',
                            backgroundColor: 'rgba(250,204,21,0.05)',
                            tension: 0.45, fill: true, borderWidth: 2.5, pointBackgroundColor: '#facc15'
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } },
                    scales: {
                        y: {  beginAtZero: true,
                            grid: { color: 'rgba(0,0,0,0.05)' },
                            ticks: {
                                font: { size: 12 },
                                stepSize: 1,      // ← ticks 안으로
                                precision: 0      // ← ticks 안으로
                            },
                            min: 0},
                        x: { grid: { color: 'rgba(0,0,0,0.05)' } }
                    }
                }
            });
        }
    }

    /* 2. 필터 및 페이징 */
    const tabBtns = document.querySelectorAll(".support-tab-btn");
    const catEl   = document.getElementById('supportCategoryFilter');
    const searchEl = document.getElementById('supportSearchInput');

    const PAGE_SIZE = 10;   // 페이지당 행 수
    let currentPage = 1;
    let filteredRows = [];  // 필터 통과한 행 목록

    function applyFilter() {
        const activeBtn = document.querySelector(".support-tab-btn.active");
        const status    = activeBtn ? activeBtn.dataset.filter : '전체';
        const category  = catEl    ? catEl.value               : '';
        const keyword   = searchEl ? searchEl.value.toLowerCase().trim() : '';

        // 필터 통과한 행만 추려서 저장
        filteredRows = [];
        document.querySelectorAll('.support-row').forEach(function(row) {
            const matchStatus   = status === '전체' || row.dataset.status === status;
            const matchCategory = !category || row.dataset.category.includes(category);
            const matchKeyword  = !keyword  ||
                row.dataset.title.toLowerCase().includes(keyword) ||
                (row.dataset.writer || '').toLowerCase().includes(keyword);

            row.style.display = 'none'; // 일단 전부 숨김
            if (matchStatus && matchCategory && matchKeyword) {
                filteredRows.push(row);
            }
        });

        currentPage = 1;  // 필터 바뀌면 1페이지로
        renderPage();
    }

    function renderPage() {
        const start = (currentPage - 1) * PAGE_SIZE;
        const end   = start + PAGE_SIZE;

        // 필터된 행만 현재 페이지 범위만 보이게
        filteredRows.forEach(function(row, i) {
            row.style.display = (i >= start && i < end) ? '' : 'none';
        });

        // 총 건수 업데이트
        const countText = document.getElementById('supportCountText');
        if (countText) countText.textContent = filteredRows.length;

        renderPagination();
    }

    function renderPagination() {
        const wrap      = document.getElementById('paginationWrap');
        if (!wrap) return;

        const totalPages = Math.ceil(filteredRows.length / PAGE_SIZE);
        wrap.innerHTML   = '';

        if (totalPages <= 1) return;

        // 이전 버튼
        const prevBtn = makePageBtn('이전', currentPage === 1, function() {
            if (currentPage > 1) { currentPage--; renderPage(); }
        });
        wrap.appendChild(prevBtn);

        // 페이지 번호
        for (var p = 1; p <= totalPages; p++) {
            (function(page) {
                const btn = makePageBtn(page, false, function() {
                    currentPage = page;
                    renderPage();
                });
                if (page === currentPage) btn.classList.add('active');
                wrap.appendChild(btn);
            })(p);
        }

        // 다음 버튼
        const nextBtn = makePageBtn('다음', currentPage === totalPages, function() {
            if (currentPage < totalPages) { currentPage++; renderPage(); }
        });
        wrap.appendChild(nextBtn);
    }

    function makePageBtn(label, disabled, onClick) {
        const btn = document.createElement('button');
        btn.textContent = label;
        btn.className   = 'support-page-btn';
        btn.disabled    = disabled;
        btn.addEventListener('click', onClick);
        return btn;
    }

    // 이벤트 등록
    tabBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            tabBtns.forEach(function(b) { b.classList.remove('active'); });
            this.classList.add('active');
            applyFilter();
        });
    });

    if (catEl)    catEl.addEventListener('change', applyFilter);
    if (searchEl) searchEl.addEventListener('input', debounce(applyFilter, 250));

    function debounce(fn, ms) {
        let timer;
        return function() {
            clearTimeout(timer);
            timer = setTimeout(() => fn.apply(this, arguments), ms);
        };
    }

    // 초기 실행
    applyFilter();
});
</script>

<style>
/* 기존 스타일 그대로 유지 */
.support-tab-btn {
	padding: 7px 16px;
	border-radius: 6px;
	font-size: 13px;
	font-weight: 500;
	color: #6b7280;
	background: none;
	border: none;
	cursor: pointer;
	transition: all 0.15s;
}

.support-tab-btn:hover {
	color: #111827;
}

.support-tab-btn.active {
	background: #4a9eff;
	color: #fff;
	font-weight: 600;
}

.support-stat-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 16px;
	margin-bottom: 20px;
}

.support-stat-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
	padding: 4px 0;
}

.support-stat-label {
	font-size: 13px;
	color: #6b7280;
	font-weight: 500;
}

.support-stat-value {
	font-size: 28px;
	font-weight: 700;
	color: #111827;
	letter-spacing: -0.02em;
	line-height: 1.1;
}

.support-stat-value.pending {
	color: #d97706;
}

.support-stat-value.answered {
	color: #16a34a;
}

.support-stat-value.report {
	color: #dc2626;
}

.support-stat-unit {
	font-size: 14px;
	font-weight: 500;
	margin-left: 2px;
	color: #6b7280;
}

.support-chart-panel {
	margin-bottom: 20px;
}

.support-chart-panel .chart-wrap {
	height: 220px;
}

.support-board-right {
	display: flex;
	align-items: center;
	gap: 10px;
}

.support-select {
	padding: 7px 28px 7px 10px;
	border: 1px solid #e5e7eb;
	border-radius: 6px;
	font-size: 13px;
	background-color: #fff;
	color: #111827;
	cursor: pointer;
	appearance: none;
	background-image:
		url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%236b7280' stroke-width='2'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E");
	background-repeat: no-repeat;
	background-position: right 8px center;
}

.support-title-cell {
	max-width: 320px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.role-pill.category {
	background: #f3f4f6;
	color: #374151;
	border: 1px solid #e5e7eb;
}

.detail-link-btn.primary {
	background: #6366f1;
	color: #fff;
	border-color: #6366f1;
}

.detail-link-btn.primary:hover {
	background: #4f46e5;
	border-color: #4f46e5;
}

.page-header-row {
	margin-bottom: 24px; /* ← 원하는 간격으로 조정 */
}

.admin-support-page {
	padding-top: 24px !important;
}

#supportTable th, #supportTable td {
	text-align: center;
}

#supportTable .manage-cell .table-action-wrap {
	justify-content: center;
}

@media ( max-width : 1024px) {
	.support-stat-grid {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media ( max-width : 640px) {
	.support-stat-grid {
		grid-template-columns: 1fr 1fr;
		gap: 10px;
	}
	.support-board-right {
		flex-wrap: wrap;
	}
	.support-title-cell {
		max-width: 160px;
	}
}

/* ── 페이지네이션 ── */
.users-board-footer {
   display: grid;
    grid-template-columns: 1fr auto 1fr; /* 3분할 레이아웃 */
    align-items: center;
    padding-top: 16px;
    margin-top: 10px;
}

.support-pagination {
    display: flex;
    align-items: center;
    gap: 4px;
}

.support-page-btn {
    min-width: 34px;
    height: 34px;
    padding: 0 10px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background: var(--bg-card, #1a1e2b);
    border: 1px solid var(--border, rgba(255,255,255,0.08));
    border-radius: 6px;
    color: var(--text-secondary, #9ca3af);
    font-size: 13px;
    font-weight: 500;
    font-family: inherit;
    cursor: pointer;
    transition: all 0.15s;
}

.support-page-btn:hover:not(:disabled) {
    background: var(--bg-card-hover, #1f2435);
    color: var(--text-primary, #f9fafb);
    border-color: rgba(255,255,255,0.15);
}

.support-page-btn.active {
    background: #4a9eff;
    border-color: #4a9eff;
    color: #fff;
    font-weight: 700;
}

.support-page-btn:disabled {
    opacity: 0.35;
    cursor: not-allowed;
}
</style>



