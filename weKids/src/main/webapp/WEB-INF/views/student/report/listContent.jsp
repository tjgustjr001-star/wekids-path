<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-report.css">

<section class="student-report-page">
    <div class="student-report-topbar">
        <div class="student-report-title-group">
            <h2 class="student-report-page-title">학습 리포트</h2>

            <div class="student-report-tab-group">
                <button type="button" class="student-report-tab-btn active" data-tab="weekly">주간</button>
                <button type="button" class="student-report-tab-btn" data-tab="monthly">월간</button>
                <button type="button" class="student-report-tab-btn" data-tab="semester">학기</button>
            </div>
        </div>

        <div class="student-report-rate-badge" id="studentReportRateBadge">
            <span class="student-report-star-icon"></span>
            <span>${weeklyReport.tabLabel} 과제 제출율: ${weeklyReport.overallRate}%</span>
        </div>
    </div>

    <div class="student-report-panel period-panel active" data-tab="weekly"
         data-tab-label="${weeklyReport.tabLabel}"
         data-overall-rate="${weeklyReport.overallRate}">
        <div class="student-report-grid">
            <div class="student-report-chart-card">
                <div class="student-report-card-head">
                    <h3>
                        <span class="student-report-head-icon chart"></span>
                        과목별 제출 현황
                    </h3>
                    <span class="student-report-unit">단위: 건</span>
                </div>

                <div class="student-report-chart-wrap">
                    <c:forEach var="item" items="${weeklyReport.chart}">
                        <div class="student-report-chart-item">
                            <div class="student-report-chart-bars">
                                <div class="student-report-chart-bar total">
                                    <div class="student-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                </div>
                                <div class="student-report-chart-bar submitted">
                                    <div class="student-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                </div>
                            </div>
                            <div class="student-report-chart-values">
                                <span>${item.submitted}</span>
                                <span>${item.total}</span>
                            </div>
                            <div class="student-report-chart-label">${item.subject}</div>
                        </div>
                    </c:forEach>
                </div>

                <div class="student-report-legend">
                    <div class="student-report-legend-item">
                        <span class="student-report-legend-dot green"></span>
                        <span>제출 완료</span>
                    </div>
                    <div class="student-report-legend-item">
                        <span class="student-report-legend-dot gray"></span>
                        <span>전체 과제</span>
                    </div>
                </div>
            </div>

            <div class="student-report-side">
                <div class="student-report-summary-card">
                    <h3>${weeklyReport.tabLabel} 학습 요약</h3>
                    <div class="student-report-summary-list">
                        <div class="student-report-summary-item">
                            <span>완료한 학습</span>
                            <strong>${weeklyReport.summary.completed}건</strong>
                        </div>
                        <div class="student-report-summary-item">
                            <span>제출한 과제</span>
                            <strong>${weeklyReport.summary.submitted}건</strong>
                        </div>
                        <div class="student-report-summary-item">
                            <span>학습 시간</span>
                            <strong>${weeklyReport.summary.hours}시간</strong>
                        </div>
                    </div>
                </div>

                <div class="student-report-comment-card">
                    <h3>
                        <span class="student-report-head-icon comment"></span>
                        선생님 코멘트
                    </h3>
                    <div class="student-report-comment-box">
                        <span class="student-report-comment-teacher">김선생 선생님</span>
                        ${weeklyReport.comment}
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="student-report-panel period-panel" data-tab="monthly"
         data-tab-label="${monthlyReport.tabLabel}"
         data-overall-rate="${monthlyReport.overallRate}">
        <div class="student-report-grid">
            <div class="student-report-chart-card">
                <div class="student-report-card-head">
                    <h3>
                        <span class="student-report-head-icon chart"></span>
                        과목별 제출 현황
                    </h3>
                    <span class="student-report-unit">단위: 건</span>
                </div>

                <div class="student-report-chart-wrap">
                    <c:forEach var="item" items="${monthlyReport.chart}">
                        <div class="student-report-chart-item">
                            <div class="student-report-chart-bars">
                                <div class="student-report-chart-bar total">
                                    <div class="student-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                </div>
                                <div class="student-report-chart-bar submitted">
                                    <div class="student-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                </div>
                            </div>
                            <div class="student-report-chart-values">
                                <span>${item.submitted}</span>
                                <span>${item.total}</span>
                            </div>
                            <div class="student-report-chart-label">${item.subject}</div>
                        </div>
                    </c:forEach>
                </div>

                <div class="student-report-legend">
                    <div class="student-report-legend-item">
                        <span class="student-report-legend-dot green"></span>
                        <span>제출 완료</span>
                    </div>
                    <div class="student-report-legend-item">
                        <span class="student-report-legend-dot gray"></span>
                        <span>전체 과제</span>
                    </div>
                </div>
            </div>

            <div class="student-report-side">
                <div class="student-report-summary-card">
                    <h3>${monthlyReport.tabLabel} 학습 요약</h3>
                    <div class="student-report-summary-list">
                        <div class="student-report-summary-item">
                            <span>완료한 학습</span>
                            <strong>${monthlyReport.summary.completed}건</strong>
                        </div>
                        <div class="student-report-summary-item">
                            <span>제출한 과제</span>
                            <strong>${monthlyReport.summary.submitted}건</strong>
                        </div>
                        <div class="student-report-summary-item">
                            <span>학습 시간</span>
                            <strong>${monthlyReport.summary.hours}시간</strong>
                        </div>
                    </div>
                </div>

                <div class="student-report-comment-card">
                    <h3>
                        <span class="student-report-head-icon comment"></span>
                        선생님 코멘트
                    </h3>
                    <div class="student-report-comment-box">
                        <span class="student-report-comment-teacher">김선생 선생님</span>
                        ${monthlyReport.comment}
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="student-report-panel period-panel" data-tab="semester"
         data-tab-label="${semesterReport.tabLabel}"
         data-overall-rate="${semesterReport.overallRate}">
        <div class="student-report-grid">
            <div class="student-report-chart-card">
                <div class="student-report-card-head">
                    <h3>
                        <span class="student-report-head-icon chart"></span>
                        과목별 제출 현황
                    </h3>
                    <span class="student-report-unit">단위: 건</span>
                </div>

                <div class="student-report-chart-wrap">
                    <c:forEach var="item" items="${semesterReport.chart}">
                        <div class="student-report-chart-item">
                            <div class="student-report-chart-bars">
                                <div class="student-report-chart-bar total">
                                    <div class="student-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                </div>
                                <div class="student-report-chart-bar submitted">
                                    <div class="student-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                </div>
                            </div>
                            <div class="student-report-chart-values">
                                <span>${item.submitted}</span>
                                <span>${item.total}</span>
                            </div>
                            <div class="student-report-chart-label">${item.subject}</div>
                        </div>
                    </c:forEach>
                </div>

                <div class="student-report-legend">
                    <div class="student-report-legend-item">
                        <span class="student-report-legend-dot green"></span>
                        <span>제출 완료</span>
                    </div>
                    <div class="student-report-legend-item">
                        <span class="student-report-legend-dot gray"></span>
                        <span>전체 과제</span>
                    </div>
                </div>
            </div>

            <div class="student-report-side">
                <div class="student-report-summary-card">
                    <h3>${semesterReport.tabLabel} 학습 요약</h3>
                    <div class="student-report-summary-list">
                        <div class="student-report-summary-item">
                            <span>완료한 학습</span>
                            <strong>${semesterReport.summary.completed}건</strong>
                        </div>
                        <div class="student-report-summary-item">
                            <span>제출한 과제</span>
                            <strong>${semesterReport.summary.submitted}건</strong>
                        </div>
                        <div class="student-report-summary-item">
                            <span>학습 시간</span>
                            <strong>${semesterReport.summary.hours}시간</strong>
                        </div>
                    </div>
                </div>

                <div class="student-report-comment-card">
                    <h3>
                        <span class="student-report-head-icon comment"></span>
                        선생님 코멘트
                    </h3>
                    <div class="student-report-comment-box">
                        <span class="student-report-comment-teacher">김선생 선생님</span>
                        ${semesterReport.comment}
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="${pageContext.request.contextPath}/resources/js/student/student-report.js"></script>