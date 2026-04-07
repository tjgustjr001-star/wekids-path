	<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	
	<link rel="stylesheet"
		href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
	
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
				<button type="button" class="primary-btn"
					onclick="location.href='${pageContext.request.contextPath}/support/faq'">FAQ
					등록</button>
	
			</div>
		</div>
	
		<%-- ── 통계 카드 ── --%>
		<div class="stats-grid">
			<div class="stat-card">
				<div class="stat-top">
					<div class="icon-box blue">
						<i class="fa-regular fa-message"></i>
					</div>
				</div>
				<div class="stat-label">전체 문의</div>
				<div class="stat-value">
					<span class="support-stat-value-a">${totalCount} </span>
				</div>
			</div>
	
			<div class="stat-card">
				<div class="stat-top">
					<div class="icon-box yellow">
						<i class="fa-regular fa-clock"></i>
					</div>
	
				</div>
				<div class="stat-label">답변 대기</div>
				<div class="stat-value">
					<span class="support-stat-value-b">${pendingCount} </span>
				</div>
			</div>
	
			<div class="stat-card">
				<div class="stat-top">
					<div class="icon-box green">
						<i class="fa-regular fa-circle-check"></i>
					</div>
				</div>
				<div class="stat-label">답변 완료</div>
				<div class="stat-value">
					<span class="support-stat-value-c">${answeredCount} </span>
				</div>
			</div>
	
	
			<div class="stat-card">
				<div class="stat-top">
					<div class="icon-box pink">
						<i class="fa-solid fa-exclamation"></i>
					</div>
				</div>
				<div class="stat-label">신고</div>
				<div class="stat-value">
					<span class="support-stat-value-d">${reportCount} </span>
				</div>
			</div>
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
				<table class="user-table">
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
									<div class="table-action-wrap-a">
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
				<span>총 ${totalCount}건
				</span>
				<div class="support-pagination" id="paginationWrap"></div>
			</div>
		</section>
	</div>
	
	<%-- ── Chart.js + 필터 스크립트 ── --%>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script>
	document.addEventListener("DOMContentLoaded", function () {
	
	    /* 1. 주간 차트 생성 */
	    const supportChartCanvas = document.getElementById('supportWeeklyChart');
	
	    if (supportChartCanvas && typeof Chart !== 'undefined') {
	        const labels = [];
	        const completedData = [];
	        const pendingData = [];
	
	        <c:forEach var="stat" items="${weeklyStats}">
	            labels.push("${stat.TARGETDATE}");
	            completedData.push(${empty stat.COMPLETEDCOUNT ? 0 : stat.COMPLETEDCOUNT});
	            pendingData.push(${empty stat.PENDINGCOUNT ? 0 : stat.PENDINGCOUNT});
	        </c:forEach>
	
	        if (labels.length > 0) {
	            const oldChart = Chart.getChart(supportChartCanvas);
	            if (oldChart) {
	                oldChart.destroy();
	            }
	
	            new Chart(supportChartCanvas, {
	                type: 'line',
	                data: {
	                    labels: labels,
	                    datasets: [
	                        {
	                            label: '답변 완료',
	                            data: completedData,
	                            borderColor: '#4ade80',
	                            backgroundColor: 'rgba(74, 222, 128, 0.07)',
	                            borderWidth: 3,
	                            tension: 0.38,
	                            fill: true,
	                            pointRadius: 4,
	                            pointHoverRadius: 5,
	                            pointBackgroundColor: '#ffffff',
	                            pointBorderColor: '#4ade80',
	                            pointBorderWidth: 2,
	                            pointHoverBackgroundColor: '#ffffff',
	                            pointHoverBorderColor: '#4ade80'
	                        },
	                        {
	                            label: '답변 대기',
	                            data: pendingData,
	                            borderColor: '#facc15',
	                            backgroundColor: 'rgba(250, 204, 21, 0.05)',
	                            borderWidth: 3,
	                            tension: 0.38,
	                            fill: true,
	                            pointRadius: 4,
	                            pointHoverRadius: 5,
	                            pointBackgroundColor: '#ffffff',
	                            pointBorderColor: '#facc15',
	                            pointBorderWidth: 2,
	                            pointHoverBackgroundColor: '#ffffff',
	                            pointHoverBorderColor: '#facc15'
	                        }
	                    ]
	                },
	                options: {
	                    responsive: true,
	                    maintainAspectRatio: false,
	                    interaction: {
	                        mode: 'index',
	                        intersect: false
	                    },
	                    layout: {
	                        padding: {
	                            top: 10,
	                            right: 18,
	                            left: 18,
	                            bottom: 0
	                        }
	                    },
	                    plugins: {
	                        legend: {
	                            position: 'bottom',
	                            labels: {
	                                color: '#94a3b8',
	                                usePointStyle: true,
	                                pointStyle: 'circle',
	                                boxWidth: 8,
	                                boxHeight: 8,
	                                padding: 16,
	                                font: {
	                                    size: 15
	                                }
	                            }
	                        },
	                        tooltip: {
	                            backgroundColor: '#0f172a',
	                            borderColor: 'rgba(148, 163, 184, 0.25)',
	                            borderWidth: 1,
	                            titleColor: '#f8fafc',
	                            bodyColor: '#dbeafe',
	                            displayColors: true,
	                            padding: 12,
	                            cornerRadius: 10
	                        }
	                    },
	                    scales: {
	                    	
	                        x: {
	                        
	                            ticks: {
	                                color: '#94a3b8',
	                                font: {
	                                    size: 12
	                                },
	                                padding: 12
	                            },
	                            grid: {
	                                color: 'rgba(71, 85, 105, 0.25)'
	                            },
	                            border: {
	                                color: 'rgba(71, 85, 105, 0.35)'
	                            }
	                        },
	                        y: {
	                        	offset: true,
	                            beginAtZero: true,
	                            min: 0,
	                            ticks: {
	                                color: '#94a3b8',
	                                font: {
	                                    size: 12
	                                },
	                                padding: 8,
	                                stepSize: 1,
	                                precision: 0
	                            },
	                            grid: {
	                                color: 'rgba(71, 85, 105, 0.25)'
	                            },
	                            border: {
	                                color: 'rgba(71, 85, 105, 0.35)'
	                            }
	                        }
	                    }
	                }
	            });
	        }
	    }
	
	    /* 2. 필터 및 페이징 */
	    const tabBtns = document.querySelectorAll(".support-tab-btn");
	    const catEl = document.getElementById('supportCategoryFilter');
	    const searchEl = document.getElementById('supportSearchInput');
	
	    const PAGE_SIZE = 10;
	    let currentPage = 1;
	    let filteredRows = [];
	
	    function applyFilter() {
	        const activeBtn = document.querySelector(".support-tab-btn.active");
	        const status = activeBtn ? activeBtn.dataset.filter : '전체';
	        const category = catEl ? catEl.value : '';
	        const keyword = searchEl ? searchEl.value.toLowerCase().trim() : '';
	
	        filteredRows = [];
	
	        document.querySelectorAll('.support-row').forEach(function(row) {
	            const rowStatus = row.dataset.status || '';
	            const rowCategory = row.dataset.category || '';
	            const rowTitle = (row.dataset.title || '').toLowerCase();
	            const rowWriter = (row.dataset.writer || '').toLowerCase();
	
	            const matchStatus = status === '전체' || rowStatus === status;
	            const matchCategory = !category || rowCategory.includes(category);
	            const matchKeyword =
	                !keyword ||
	                rowTitle.includes(keyword) ||
	                rowWriter.includes(keyword);
	
	            row.style.display = 'none';
	
	            if (matchStatus && matchCategory && matchKeyword) {
	                filteredRows.push(row);
	            }
	        });
	
	        currentPage = 1;
	        renderPage();
	    }
	
	    function renderPage() {
	        const start = (currentPage - 1) * PAGE_SIZE;
	        const end = start + PAGE_SIZE;
	
	        filteredRows.forEach(function(row, i) {
	            row.style.display = (i >= start && i < end) ? '' : 'none';
	        });
	
	        const countText = document.getElementById('supportCountText');
	        if (countText) {
	            countText.textContent = filteredRows.length;
	        }
	
	        renderPagination();
	    }
	
	    function renderPagination() {
	        const wrap = document.getElementById('paginationWrap');
	        if (!wrap) return;
	
	        const totalPages = Math.ceil(filteredRows.length / PAGE_SIZE);
	        wrap.innerHTML = '';
	
	        if (totalPages <= 1) return;
	
	        const prevBtn = makePageBtn('이전', currentPage === 1, function() {
	            if (currentPage > 1) {
	                currentPage--;
	                renderPage();
	            }
	        });
	        wrap.appendChild(prevBtn);
	
	        for (let p = 1; p <= totalPages; p++) {
	            const btn = makePageBtn(String(p), false, function() {
	                currentPage = p;
	                renderPage();
	            });
	
	            if (p === currentPage) {
	                btn.classList.add('active');
	            }
	
	            wrap.appendChild(btn);
	        }
	
	        const nextBtn = makePageBtn('다음', currentPage === totalPages, function() {
	            if (currentPage < totalPages) {
	                currentPage++;
	                renderPage();
	            }
	        });
	        wrap.appendChild(nextBtn);
	    }
	
	    function makePageBtn(label, disabled, onClick) {
	        const btn = document.createElement('button');
	        btn.textContent = label;
	        btn.className = 'support-page-btn';
	        btn.disabled = disabled;
	        btn.addEventListener('click', onClick);
	        return btn;
	    }
	
	    function debounce(fn, ms) {
	        let timer;
	        return function() {
	            clearTimeout(timer);
	            timer = setTimeout(() => fn.apply(this, arguments), ms);
	        };
	    }
	
	    tabBtns.forEach(function(btn) {
	        btn.addEventListener('click', function() {
	            tabBtns.forEach(function(b) {
	                b.classList.remove('active');
	            });
	            this.classList.add('active');
	            applyFilter();
	        });
	    });
	
	    if (catEl) {
	        catEl.addEventListener('change', applyFilter);
	    }
	
	    if (searchEl) {
	        searchEl.addEventListener('input', debounce(applyFilter, 250));
	    }
	
	    applyFilter();
	});
	</script>
	<style>
	/* 기존 스타일 그대로 유지 */
	.support-tab-btn {
		padding: 7px 16px;
		border-radius: 50px;
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
	
	.support-stat-unit {
		font-size: 14px;
		font-weight: 500;
		margin-left: 2px;
		color: #6b7280;
	}
	
	.support-chart-panel {
		margin-top: 24px;
		margin-bottom: 24px;
	}
	
	.support-chart-panel .chart-wrap {
		height: 300px;
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
		width: 100px
		border-radius: 50px;
		background: #6366f1;
		color: #fff;
		border-color: #6366f1;
	}
	
	.detail-link-btn.primary:hover {
	border-radius: 50px;
		background: #4f46e5;
		border-color: #4f46e5;
	}
	
	.page-header-row {
		margin-bottom: 24px; /* ← 원하는 간격으로 조정 */
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
		padding: 20px 0;
	
	}
	.users-board-footer span {
      
        padding-left: 25px;
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
		border: 1px solid var(--border, rgba(255, 255, 255, 0.08));
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
		border-color: rgba(255, 255, 255, 0.15);
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
	
	
	
