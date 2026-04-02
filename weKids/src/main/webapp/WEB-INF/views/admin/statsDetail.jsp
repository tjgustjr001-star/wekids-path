<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/stats.css">

<section class="admin-stat-detail-page">
    <a href="${pageContext.request.contextPath}/admin/stats?period=${period}" class="admin-stat-detail-back">
        ← 대시보드로 돌아가기
    </a>

    <div class="admin-stat-detail-summary">
        <div class="admin-stat-detail-summary-left">
            <h1>
                ${detailSummary.classDisplayName} 통계 상세
                <span class="admin-stat-detail-status-badge
                    <c:choose>
                        <c:when test="${detailSummary.classStatus eq 'ACTIVE'}">active</c:when>
                        <c:when test="${detailSummary.classStatus eq 'ARCHIVED'}">archive</c:when>
                        <c:otherwise>blind</c:otherwise>
                    </c:choose>
                ">
                    <c:choose>
                        <c:when test="${detailSummary.classStatus eq 'ACTIVE'}">정상 운영중</c:when>
                        <c:when test="${detailSummary.classStatus eq 'ARCHIVED'}">아카이브</c:when>
                        <c:otherwise>블라인드</c:otherwise>
                    </c:choose>
                </span>
            </h1>
            <p>해당 클래스의 상세 학습 및 과제 현황을 분석합니다.</p>
        </div>

        <div class="admin-stat-detail-summary-right">
            <div class="admin-stat-detail-summary-item">
                <div class="admin-stat-detail-summary-label">총 학생수</div>
                <div class="admin-stat-detail-summary-value">
                    <fmt:formatNumber value="${detailSummary.studentCount}" pattern="#,##0"/>명
                </div>
            </div>
            <div class="admin-stat-detail-summary-item">
                <div class="admin-stat-detail-summary-label">담당 교사</div>
                <div class="admin-stat-detail-summary-value">
                    ${detailSummary.teacherName}
                </div>
            </div>
            <div class="admin-stat-detail-summary-item">
                <div class="admin-stat-detail-summary-label">개설일</div>
                <div class="admin-stat-detail-summary-value">
                    ${detailSummary.createdDateText}
                </div>
            </div>
        </div>
    </div>

    <div class="admin-stat-detail-grid">
        <article class="admin-stat-detail-panel">
            <div class="admin-stat-detail-panel-head">
                <h2>과제 제출 추이</h2>
                <span class="admin-stat-detail-panel-meta">최근 7구간</span>
            </div>
            <div class="admin-stat-detail-chart-box">
                <canvas id="adminClassSubmissionTrendChart"></canvas>
            </div>
        </article>

        <article class="admin-stat-detail-panel">
            <div class="admin-stat-detail-panel-head">
                <h2>학습 완료 / 미완료</h2>
                <span class="admin-stat-detail-panel-meta">학습 목록 기준</span>
            </div>
            <div class="admin-stat-detail-chart-box">
                <canvas id="adminClassLearnStatusChart"></canvas>
            </div>
        </article>
    </div>

    <article class="admin-stat-detail-board">
        <div class="admin-stat-detail-board-head">
            <h2>학생별 상세 통계</h2>
        </div>

        <div class="admin-stat-detail-table-wrap">
            <table class="admin-stat-detail-table">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>학습 완료</th>
                        <th>학습 진행률</th>
                        <th>과제 제출</th>
                        <th>최근 과제</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty studentStatsList}">
                            <tr>
                                <td colspan="6" class="admin-stat-detail-empty">표시할 학생 통계가 없습니다.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="row" items="${studentStatsList}">
                                <tr>
                                    <td class="admin-stat-detail-name">${row.studentName}</td>
                                    <td>
                                        <fmt:formatNumber value="${row.completedLearnCount}" pattern="#,##0"/> /
                                        <fmt:formatNumber value="${row.totalLearnCount}" pattern="#,##0"/>
                                    </td>
                                    <td>
                                        <div class="admin-stat-detail-progress">
                                            <div class="admin-stat-detail-progress-track">
                                                <div class="admin-stat-detail-progress-fill blue"
                                                     style="width:${row.learnProgressRate}%;"></div>
                                            </div>
                                            <strong>
                                                <fmt:formatNumber value="${row.learnProgressRate}" pattern="#,##0"/>%
                                            </strong>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="admin-stat-detail-progress">
                                            <div class="admin-stat-detail-progress-track">
                                                <c:choose>
                                                    <c:when test="${row.assignmentSubmitRate ge 90}">
                                                        <div class="admin-stat-detail-progress-fill green"
                                                             style="width:${row.assignmentSubmitRate}%;"></div>
                                                    </c:when>
                                                    <c:when test="${row.assignmentSubmitRate ge 70}">
                                                        <div class="admin-stat-detail-progress-fill yellow"
                                                             style="width:${row.assignmentSubmitRate}%;"></div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="admin-stat-detail-progress-fill red"
                                                             style="width:${row.assignmentSubmitRate}%;"></div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <strong>
                                                <fmt:formatNumber value="${row.submittedAssignmentCount}" pattern="#,##0"/> /
                                                <fmt:formatNumber value="${row.totalAssignmentCount}" pattern="#,##0"/>
                                            </strong>
                                        </div>
                                    </td>
                                    <td>${row.lastAssignmentStatusText}</td>
                                    <td>
                                        <span class="admin-stat-detail-status-pill
                                            <c:choose>
                                                <c:when test="${row.statusCode eq 'GOOD'}">good</c:when>
                                                <c:when test="${row.statusCode eq 'WARN'}">warn</c:when>
                                                <c:otherwise>normal</c:otherwise>
                                            </c:choose>
                                        ">
                                            ${row.statusText}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </article>

    <script id="adminClassSubmissionTrendData" type="application/json">${submissionTrendJson}</script>
    <script id="adminClassLearnStatusData" type="application/json">${classLearnStatusJson}</script>
</section>

<script src="${pageContext.request.contextPath}/resources/js/admin/stats-detail.js"></script>