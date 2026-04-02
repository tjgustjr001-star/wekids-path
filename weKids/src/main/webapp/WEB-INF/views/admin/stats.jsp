<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">


<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/stats.css">

<section class="admin-stats-page">
    <div class="admin-stats-header">
        <div>
            <h1 class="admin-stats-title">통계 대시보드</h1>
            <p class="admin-stats-subtitle">시스템 사용 현황 및 분석</p>
        </div>

        <div class="admin-stats-actions">
            <form method="get" action="${pageContext.request.contextPath}/admin/stats" class="admin-stats-period-form">
              <select name="period" class="admin-stats-period-select" onchange="this.form.submit()">
        <option value="daily" ${period eq 'daily' ? 'selected' : ''}>일간</option>
        <option value="week" ${period eq 'week' ? 'selected' : ''}>주간</option>
        <option value="month" ${period eq 'month' ? 'selected' : ''}>월간</option>
        <option value="year" ${period eq 'year' ? 'selected' : ''}>연간</option>
    </select>
            </form>

          
        </div>
    </div>

    <div class="admin-stats-kpi-grid">
        <article class="admin-stats-kpi-card">
            <div class="admin-stats-kpi-top">
                <div class="admin-stats-kpi-icon blue">
                    <span><i class="fa-solid fa-user-astronaut"></i></span>
                </div>
                <span class="admin-stats-kpi-change positive">
                    ${dashboardStat.userChangeText}
                </span>
            </div>
            <div class="admin-stats-kpi-label">전체 사용자</div>
            <div class="admin-stats-kpi-value">
                <fmt:formatNumber value="${dashboardStat.totalUsers}" pattern="#,##0"/>
            </div>
        </article>

        <article class="admin-stats-kpi-card">
            <div class="admin-stats-kpi-top">
                <div class="admin-stats-kpi-icon green">
                    <span><i class="fa-solid fa-book-open"></i></span>
                </div>
                <span class="admin-stats-kpi-change positive">
                    ${dashboardStat.activeClassChangeText}
                </span>
            </div>
            <div class="admin-stats-kpi-label">활성 클래스</div>
            <div class="admin-stats-kpi-value">
                <fmt:formatNumber value="${dashboardStat.activeClassCount}" pattern="#,##0"/>
            </div>
        </article>

        <article class="admin-stats-kpi-card">
            <div class="admin-stats-kpi-top">
                <div class="admin-stats-kpi-icon purple">
                    <span><i class="fa-solid fa-clipboard-list"></i></span>
                </div>
                <span class="admin-stats-kpi-change positive">
                    ${dashboardStat.assignmentSubmitChangeText}
                </span>
            </div>
            <div class="admin-stats-kpi-label">제출된 과제</div>
            <div class="admin-stats-kpi-value">
                <fmt:formatNumber value="${dashboardStat.submittedAssignmentCount}" pattern="#,##0"/>
            </div>
        </article>

        <article class="admin-stats-kpi-card">
            <div class="admin-stats-kpi-top">
                <div class="admin-stats-kpi-icon amber">
                    <span><i class="fa-solid fa-chart-line"></i></span>
                </div>
                <span class="admin-stats-kpi-change neutral">
                    ${dashboardStat.learnCompleteRateText}
                </span>
            </div>
            <div class="admin-stats-kpi-label">학습 완료율</div>
            <div class="admin-stats-kpi-value">
                <fmt:formatNumber value="${dashboardStat.learnCompleteRate}" pattern="#,##0"/>%
            </div>
        </article>
    </div>

    <div class="admin-stats-chart-grid admin-stats-chart-grid-top">
        <article class="admin-stats-panel">
            <div class="admin-stats-panel-head">
                <h2>사용자 증가 추이</h2>
                <span class="admin-stats-panel-meta">
                    <c:choose>
                        <c:when test="${period eq 'month'}">최근 4주</c:when>
                        <c:when test="${period eq 'year'}">최근 12개월</c:when>
                        <c:otherwise>최근 7일</c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="admin-stats-chart-box large">
                <canvas id="adminUserGrowthChart"></canvas>
            </div>
        </article>

        <article class="admin-stats-panel">
            <div class="admin-stats-panel-head">
                <h2>사용자 유형 분포</h2>
                <span class="admin-stats-panel-meta">
                    총 <fmt:formatNumber value="${dashboardStat.totalUsers}" pattern="#,##0"/>명
                </span>
            </div>
            <div class="admin-stats-chart-box donut">
                <canvas id="adminUserTypeChart"></canvas>
            </div>

            <div class="admin-stats-legend-grid">
                <c:forEach var="item" items="${userTypeList}">
                    <div class="admin-stats-legend-item">
                        <span class="admin-stats-legend-dot" style="background:${item.color};"></span>
                        <span class="admin-stats-legend-label">${item.name}</span>
                        <strong class="admin-stats-legend-value">
                            <fmt:formatNumber value="${item.value}" pattern="#,##0"/>
                        </strong>
                    </div>
                </c:forEach>
            </div>
        </article>
    </div>

    <div class="admin-stats-chart-grid admin-stats-chart-grid-bottom">
        <article class="admin-stats-panel">
            <div class="admin-stats-panel-head">
                <h2>클래스별 과제 제출률</h2>
                <span class="admin-stats-panel-meta">
                    <c:choose>
                        <c:when test="${period eq 'month'}">이번 달</c:when>
                        <c:when test="${period eq 'year'}">올해</c:when>
                        <c:otherwise>이번 주</c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="admin-stats-chart-box medium">
                <canvas id="adminAssignmentRateChart"></canvas>
            </div>
        </article>

        <article class="admin-stats-panel">
            <div class="admin-stats-panel-head">
                <h2>학습 완료 현황</h2>
                <span class="admin-stats-panel-meta">
                    전체 학습 기준
                </span>
            </div>
            <div class="admin-stats-chart-box medium">
                <canvas id="adminLearnCompletionChart"></canvas>
            </div>
        </article>
    </div>

    <article class="admin-stats-board">
        <div class="admin-stats-board-head">
            <h4>클래스별 상세 통계</h4>
            <span class="admin-stats-board-meta">
                총 <fmt:formatNumber value="${classStatsCount}" pattern="#,##0"/>개 클래스
            </span>
        </div>

        <div class="admin-stats-table-wrap">
            <table class="admin-stats-table">
                <thead>
                    <tr>
                        <th>클래스명</th>
                        <th>학생 수</th>
                        <th>총 학습</th>
                        <th>학습 완료</th>
                        <th>총 과제</th>
                        <th>과제 제출률</th>
                        <th>상세</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty classStatsList}">
                            <tr>
                                <td colspan="7" class="admin-stats-empty">표시할 통계 데이터가 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="row" items="${classStatsList}">
                                <tr>
                                    <td class="admin-stats-cell-strong">
                                        ${row.classDisplayName}
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${row.studentCount}" pattern="#,##0"/>명
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${row.totalLearnCount}" pattern="#,##0"/>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${row.completedLearnCount}" pattern="#,##0"/>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${row.totalAssignmentCount}" pattern="#,##0"/>
                                    </td>
                                    <td>
                                        <div class="admin-stats-progress-wrap">
                                            <div class="admin-stats-progress-bar">
                                                <div class="admin-stats-progress-fill blue"
                                                     style="width:${row.assignmentSubmitRate}%;"></div>
                                            </div>
                                            <strong>
                                                <fmt:formatNumber value="${row.assignmentSubmitRate}" pattern="#,##0"/>%
                                            </strong>
                                        </div>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/stats/classes/${row.classId}?period=${period}"
                                           class="admin-stats-detail-btn">
                                            상세 분석
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </article>

    <script id="adminUserGrowthData" type="application/json">${userGrowthJson}</script>
    <script id="adminUserTypeData" type="application/json">${userTypeJson}</script>
    <script id="adminAssignmentRateData" type="application/json">${assignmentRateJson}</script>
    <script id="adminLearnCompletionData" type="application/json">${learnCompletionJson}</script>
</section>

<script src="${pageContext.request.contextPath}/resources/js/admin/stats.js"></script>