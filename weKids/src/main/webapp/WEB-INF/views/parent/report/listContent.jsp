<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-report.css">

<section class="parent-report-page">
    <div class="parent-report-topbar">
        <div class="parent-report-left">
            <h2 class="parent-report-page-title">학습 리포트</h2>

            <div class="parent-report-tab-group">
                <button type="button" class="parent-report-tab-btn active" data-tab="weekly">주간</button>
                <button type="button" class="parent-report-tab-btn" data-tab="monthly">월간</button>
                <button type="button" class="parent-report-tab-btn" data-tab="semester">학기</button>
            </div>
        </div>

        <div class="parent-report-child-select-wrap">
            <span class="parent-report-user-icon"></span>
            <select id="parentReportChildSelect" class="parent-report-child-select">
                <c:forEach var="child" items="${reportChildren}">
                    <option value="${child.id}" ${child.id eq selectedChildId ? 'selected' : ''}>
                        ${child.name} (${child.grade})
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="parent-report-rate-box" id="parentReportRateBox">
        <span class="parent-report-star-icon"></span>
        <div class="parent-report-rate-text">김학생의 이번 주 과제 제출율: ${child1WeeklyReport.overallRate}%</div>
    </div>

    <!-- child 1 -->
    <div class="parent-report-child-panel active" data-child-id="1">
        <div class="parent-report-period-panel active" data-tab="weekly"
             data-child-name="김학생"
             data-tab-label="이번 주"
             data-overall-rate="${child1WeeklyReport.overallRate}">
            <div class="parent-report-grid">
                <div class="parent-report-chart-card">
                    <div class="parent-report-card-head">
                        <h3>
                            <span class="parent-report-head-icon chart"></span>
                            과목별 제출 현황
                        </h3>
                        <span class="parent-report-unit">단위: 건</span>
                    </div>

                    <div class="parent-report-chart-wrap">
                        <c:forEach var="item" items="${child1WeeklyReport.data}">
                            <div class="parent-report-chart-item">
                                <div class="parent-report-chart-bars">
                                    <div class="parent-report-chart-bar total">
                                        <div class="parent-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                    </div>
                                    <div class="parent-report-chart-bar submitted">
                                        <div class="parent-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                    </div>
                                </div>
                                <div class="parent-report-chart-values">
                                    <span>${item.submitted}</span>
                                    <span>${item.total}</span>
                                </div>
                                <div class="parent-report-chart-label">${item.subject}</div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="parent-report-legend">
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot parent"></span>
                            <span>김학생 제출</span>
                        </div>
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot gray"></span>
                            <span>전체 과제</span>
                        </div>
                    </div>
                </div>

                <div class="parent-report-side">
                    <div class="parent-report-summary-card">
                        <h3>이번 주 학습 요약</h3>
                        <div class="parent-report-summary-list">
                            <div class="parent-report-summary-item">
                                <span>완료한 학습</span>
                                <strong>${child1WeeklyReport.completedLearning}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>제출한 과제</span>
                                <strong>${child1WeeklyReport.submittedAssignments}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>학습 시간</span>
                                <strong>${child1WeeklyReport.learningHours}시간</strong>
                            </div>
                        </div>
                    </div>

                    <div class="parent-report-comment-card">
                        <h3>
                            <span class="parent-report-head-icon comment"></span>
                            선생님 코멘트
                        </h3>
                        <div class="parent-report-comment-box">
                            <span class="teacher">김선생 선생님</span>
                            ${child1WeeklyReport.comment}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="parent-report-period-panel" data-tab="monthly"
             data-child-name="김학생"
             data-tab-label="이번 달"
             data-overall-rate="${child1MonthlyReport.overallRate}">
            <div class="parent-report-grid">
                <div class="parent-report-chart-card">
                    <div class="parent-report-card-head">
                        <h3>
                            <span class="parent-report-head-icon chart"></span>
                            과목별 제출 현황
                        </h3>
                        <span class="parent-report-unit">단위: 건</span>
                    </div>

                    <div class="parent-report-chart-wrap">
                        <c:forEach var="item" items="${child1MonthlyReport.data}">
                            <div class="parent-report-chart-item">
                                <div class="parent-report-chart-bars">
                                    <div class="parent-report-chart-bar total">
                                        <div class="parent-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                    </div>
                                    <div class="parent-report-chart-bar submitted">
                                        <div class="parent-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                    </div>
                                </div>
                                <div class="parent-report-chart-values">
                                    <span>${item.submitted}</span>
                                    <span>${item.total}</span>
                                </div>
                                <div class="parent-report-chart-label">${item.subject}</div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="parent-report-legend">
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot parent"></span>
                            <span>김학생 제출</span>
                        </div>
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot gray"></span>
                            <span>전체 과제</span>
                        </div>
                    </div>
                </div>

                <div class="parent-report-side">
                    <div class="parent-report-summary-card">
                        <h3>이번 달 학습 요약</h3>
                        <div class="parent-report-summary-list">
                            <div class="parent-report-summary-item">
                                <span>완료한 학습</span>
                                <strong>${child1MonthlyReport.completedLearning}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>제출한 과제</span>
                                <strong>${child1MonthlyReport.submittedAssignments}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>학습 시간</span>
                                <strong>${child1MonthlyReport.learningHours}시간</strong>
                            </div>
                        </div>
                    </div>

                    <div class="parent-report-comment-card">
                        <h3>
                            <span class="parent-report-head-icon comment"></span>
                            선생님 코멘트
                        </h3>
                        <div class="parent-report-comment-box">
                            <span class="teacher">김선생 선생님</span>
                            ${child1MonthlyReport.comment}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="parent-report-period-panel" data-tab="semester"
             data-child-name="김학생"
             data-tab-label="1학기"
             data-overall-rate="${child1SemesterReport.overallRate}">
            <div class="parent-report-grid">
                <div class="parent-report-chart-card">
                    <div class="parent-report-card-head">
                        <h3>
                            <span class="parent-report-head-icon chart"></span>
                            과목별 제출 현황
                        </h3>
                        <span class="parent-report-unit">단위: 건</span>
                    </div>

                    <div class="parent-report-chart-wrap">
                        <c:forEach var="item" items="${child1SemesterReport.data}">
                            <div class="parent-report-chart-item">
                                <div class="parent-report-chart-bars">
                                    <div class="parent-report-chart-bar total">
                                        <div class="parent-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                    </div>
                                    <div class="parent-report-chart-bar submitted">
                                        <div class="parent-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                    </div>
                                </div>
                                <div class="parent-report-chart-values">
                                    <span>${item.submitted}</span>
                                    <span>${item.total}</span>
                                </div>
                                <div class="parent-report-chart-label">${item.subject}</div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="parent-report-legend">
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot parent"></span>
                            <span>김학생 제출</span>
                        </div>
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot gray"></span>
                            <span>전체 과제</span>
                        </div>
                    </div>
                </div>

                <div class="parent-report-side">
                    <div class="parent-report-summary-card">
                        <h3>1학기 학습 요약</h3>
                        <div class="parent-report-summary-list">
                            <div class="parent-report-summary-item">
                                <span>완료한 학습</span>
                                <strong>${child1SemesterReport.completedLearning}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>제출한 과제</span>
                                <strong>${child1SemesterReport.submittedAssignments}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>학습 시간</span>
                                <strong>${child1SemesterReport.learningHours}시간</strong>
                            </div>
                        </div>
                    </div>

                    <div class="parent-report-comment-card">
                        <h3>
                            <span class="parent-report-head-icon comment"></span>
                            선생님 코멘트
                        </h3>
                        <div class="parent-report-comment-box">
                            <span class="teacher">김선생 선생님</span>
                            ${child1SemesterReport.comment}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- child 2 -->
    <div class="parent-report-child-panel" data-child-id="2">
        <div class="parent-report-period-panel active" data-tab="weekly"
             data-child-name="김동생"
             data-tab-label="이번 주"
             data-overall-rate="${child2WeeklyReport.overallRate}">
            <div class="parent-report-grid">
                <div class="parent-report-chart-card">
                    <div class="parent-report-card-head">
                        <h3>
                            <span class="parent-report-head-icon chart"></span>
                            과목별 제출 현황
                        </h3>
                        <span class="parent-report-unit">단위: 건</span>
                    </div>

                    <div class="parent-report-chart-wrap">
                        <c:forEach var="item" items="${child2WeeklyReport.data}">
                            <div class="parent-report-chart-item">
                                <div class="parent-report-chart-bars">
                                    <div class="parent-report-chart-bar total">
                                        <div class="parent-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                    </div>
                                    <div class="parent-report-chart-bar submitted">
                                        <div class="parent-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                    </div>
                                </div>
                                <div class="parent-report-chart-values">
                                    <span>${item.submitted}</span>
                                    <span>${item.total}</span>
                                </div>
                                <div class="parent-report-chart-label">${item.subject}</div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="parent-report-legend">
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot parent"></span>
                            <span>김동생 제출</span>
                        </div>
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot gray"></span>
                            <span>전체 과제</span>
                        </div>
                    </div>
                </div>

                <div class="parent-report-side">
                    <div class="parent-report-summary-card">
                        <h3>이번 주 학습 요약</h3>
                        <div class="parent-report-summary-list">
                            <div class="parent-report-summary-item">
                                <span>완료한 학습</span>
                                <strong>${child2WeeklyReport.completedLearning}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>제출한 과제</span>
                                <strong>${child2WeeklyReport.submittedAssignments}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>학습 시간</span>
                                <strong>${child2WeeklyReport.learningHours}시간</strong>
                            </div>
                        </div>
                    </div>

                    <div class="parent-report-comment-card">
                        <h3>
                            <span class="parent-report-head-icon comment"></span>
                            선생님 코멘트
                        </h3>
                        <div class="parent-report-comment-box">
                            <span class="teacher">김선생 선생님</span>
                            ${child2WeeklyReport.comment}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="parent-report-period-panel" data-tab="monthly"
             data-child-name="김동생"
             data-tab-label="이번 달"
             data-overall-rate="${child2MonthlyReport.overallRate}">
            <div class="parent-report-grid">
                <div class="parent-report-chart-card">
                    <div class="parent-report-card-head">
                        <h3>
                            <span class="parent-report-head-icon chart"></span>
                            과목별 제출 현황
                        </h3>
                        <span class="parent-report-unit">단위: 건</span>
                    </div>

                    <div class="parent-report-chart-wrap">
                        <c:forEach var="item" items="${child2MonthlyReport.data}">
                            <div class="parent-report-chart-item">
                                <div class="parent-report-chart-bars">
                                    <div class="parent-report-chart-bar total">
                                        <div class="parent-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                    </div>
                                    <div class="parent-report-chart-bar submitted">
                                        <div class="parent-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                    </div>
                                </div>
                                <div class="parent-report-chart-values">
                                    <span>${item.submitted}</span>
                                    <span>${item.total}</span>
                                </div>
                                <div class="parent-report-chart-label">${item.subject}</div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="parent-report-legend">
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot parent"></span>
                            <span>김동생 제출</span>
                        </div>
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot gray"></span>
                            <span>전체 과제</span>
                        </div>
                    </div>
                </div>

                <div class="parent-report-side">
                    <div class="parent-report-summary-card">
                        <h3>이번 달 학습 요약</h3>
                        <div class="parent-report-summary-list">
                            <div class="parent-report-summary-item">
                                <span>완료한 학습</span>
                                <strong>${child2MonthlyReport.completedLearning}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>제출한 과제</span>
                                <strong>${child2MonthlyReport.submittedAssignments}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>학습 시간</span>
                                <strong>${child2MonthlyReport.learningHours}시간</strong>
                            </div>
                        </div>
                    </div>

                    <div class="parent-report-comment-card">
                        <h3>
                            <span class="parent-report-head-icon comment"></span>
                            선생님 코멘트
                        </h3>
                        <div class="parent-report-comment-box">
                            <span class="teacher">김선생 선생님</span>
                            ${child2MonthlyReport.comment}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="parent-report-period-panel" data-tab="semester"
             data-child-name="김동생"
             data-tab-label="1학기"
             data-overall-rate="${child2SemesterReport.overallRate}">
            <div class="parent-report-grid">
                <div class="parent-report-chart-card">
                    <div class="parent-report-card-head">
                        <h3>
                            <span class="parent-report-head-icon chart"></span>
                            과목별 제출 현황
                        </h3>
                        <span class="parent-report-unit">단위: 건</span>
                    </div>

                    <div class="parent-report-chart-wrap">
                        <c:forEach var="item" items="${child2SemesterReport.data}">
                            <div class="parent-report-chart-item">
                                <div class="parent-report-chart-bars">
                                    <div class="parent-report-chart-bar total">
                                        <div class="parent-report-chart-fill gray" style="height:${item.totalHeight}%;"></div>
                                    </div>
                                    <div class="parent-report-chart-bar submitted">
                                        <div class="parent-report-chart-fill ${item.colorClass}" style="height:${item.submittedHeight}%;"></div>
                                    </div>
                                </div>
                                <div class="parent-report-chart-values">
                                    <span>${item.submitted}</span>
                                    <span>${item.total}</span>
                                </div>
                                <div class="parent-report-chart-label">${item.subject}</div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="parent-report-legend">
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot parent"></span>
                            <span>김동생 제출</span>
                        </div>
                        <div class="parent-report-legend-item">
                            <span class="parent-report-legend-dot gray"></span>
                            <span>전체 과제</span>
                        </div>
                    </div>
                </div>

                <div class="parent-report-side">
                    <div class="parent-report-summary-card">
                        <h3>1학기 학습 요약</h3>
                        <div class="parent-report-summary-list">
                            <div class="parent-report-summary-item">
                                <span>완료한 학습</span>
                                <strong>${child2SemesterReport.completedLearning}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>제출한 과제</span>
                                <strong>${child2SemesterReport.submittedAssignments}건</strong>
                            </div>
                            <div class="parent-report-summary-item">
                                <span>학습 시간</span>
                                <strong>${child2SemesterReport.learningHours}시간</strong>
                            </div>
                        </div>
                    </div>

                    <div class="parent-report-comment-card">
                        <h3>
                            <span class="parent-report-head-icon comment"></span>
                            선생님 코멘트
                        </h3>
                        <div class="parent-report-comment-box">
                            <span class="teacher">김선생 선생님</span>
                            ${child2SemesterReport.comment}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="${pageContext.request.contextPath}/resources/js/parent/parent-report.js"></script>