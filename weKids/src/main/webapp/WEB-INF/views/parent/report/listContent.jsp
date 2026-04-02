<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-report.css">

<section class="parent-report-page">
    <div class="class-page-hero">
        <div class="class-page-hero-icon">
            <span class="menu-icon chart-mini-icon"></span>
        </div>
        <div class="class-page-hero-text">
            <h1 class="class-page-hero-title">리포트</h1>
            <p class="class-page-hero-subtitle">
                <c:choose>
                    <c:when test="${not empty classInfo.className}">${classInfo.className}</c:when>
                    <c:when test="${not empty className}">${className}</c:when>
                    <c:otherwise>현재 클래스</c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>

    <!-- 목록 -->
    <div class="report-list-section">
        <div class="section-title-row section-title-row--with-filter">
            <h3>리포트 목록</h3>

            <div class="report-filter-section report-filter-section--inline">
                <form action="${pageContext.request.contextPath}/parent/classes/${classId}/reports"
                      method="get"
                      class="report-filter-form report-filter-form--inline"
                      id="parentReportFilterForm">
                    <div class="filter-top-row filter-top-row--inline">
                        <div class="form-group child-select-group child-select-group--inline">
                            <label for="studentId">자녀</label>
                            <select name="studentId" id="studentId">
                                <option value="">전체 자녀</option>
                                <c:forEach var="child" items="${childList}">
                                    <option value="${child.studentId}"
                                        <c:if test="${selectedStudentId != null and selectedStudentId == child.studentId}">selected</c:if>>
                                        ${child.studentName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="filter-chip-group">
                        <button type="button" class="filter-chip is-active" data-report-type="ALL">전체</button>
                        <button type="button" class="filter-chip" data-report-type="PERSONAL">개인</button>
                        <button type="button" class="filter-chip" data-report-type="CLASS">학급</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="parent-report-list">
            <c:choose>
                <c:when test="${not empty reportList}">
                    <c:forEach var="report" items="${reportList}">
                        <div class="parent-report-card"
						     data-report-type="${report.reportType}"
						     data-student-id="${report.studentId}">
                            <div class="report-card-top">
                                <div class="badge-group">
                                    <span class="report-badge ${report.periodType eq 'MONTHLY' ? 'is-monthly' : 'is-weekly'}">
                                        <c:choose>
                                            <c:when test="${report.periodType eq 'MONTHLY'}">월간</c:when>
                                            <c:otherwise>주간</c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="report-type-badge ${report.reportType eq 'CLASS' ? 'is-class' : 'is-personal'}">
                                        <c:choose>
                                            <c:when test="${report.reportType eq 'CLASS'}">학급</c:when>
                                            <c:otherwise>개인</c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="child-name-badge">
                                        <c:choose>
                                            <c:when test="${report.reportType eq 'CLASS'}">학급 전체</c:when>
                                            <c:otherwise>${report.studentName}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <span class="report-created-at">${report.createdAt}</span>
                            </div>

                            <h4 class="report-card-title">
							    <c:choose>
							        <c:when test="${report.reportType eq 'CLASS'}">학급 요약 리포트</c:when>
							        <c:otherwise>개인 리포트</c:otherwise>
							    </c:choose>
							</h4>

                            <div class="report-card-period">
                                ${report.startDate} ~ ${report.endDate}
                            </div>

                            <div class="report-card-bottom">
                                <div class="view-status-wrap">
                                    <c:choose>
                                        <c:when test="${report.parentViewCount != null and report.parentViewCount > 0}">
                                            <span class="view-status is-read">열람</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="view-status is-unread">미열람</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <button type="button"
                                        class="detail-btn"
                                        data-report-id="${report.reportId}"
                                        data-student-id="${report.studentId}"
                                        data-report-type="${report.reportType}">
                                    상세 보기
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="report-empty-box">
                        조회 가능한 리포트가 없습니다.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<!-- 상세 모달 -->
<div class="report-detail-modal" id="reportDetailModal" style="display:none;">
    <div class="report-detail-backdrop" id="reportDetailBackdrop"></div>

    <div class="report-detail-dialog">
        <div class="report-detail-header">
            <h3 id="detailTitle">리포트 상세</h3>
            <button type="button" class="modal-close-btn" id="closeReportDetailModal">닫기</button>
        </div>

        <div class="report-detail-body">
            <div class="detail-info-grid">
                <div class="detail-info-box">
                    <span>자녀</span>
                    <strong id="detailStudentName"></strong>
                </div>
                <div class="detail-info-box">
                    <span>기간 유형</span>
                    <strong id="detailPeriodType"></strong>
                </div>
                <div class="detail-info-box">
                    <span>기간</span>
                    <strong id="detailPeriod"></strong>
                </div>
                <div class="detail-info-box">
                    <span>생성일</span>
                    <strong id="detailCreatedAt"></strong>
                </div>
            </div>

            <div class="detail-info-grid second-row">
                <div class="detail-info-box">
                    <span>담당 교사</span>
                    <strong id="detailTeacherName"></strong>
                </div>
                <div class="detail-info-box info-wide">
                    <span>리포트 제목</span>
                    <strong id="detailHeaderTitle"></strong>
                </div>
            </div>

            <div class="detail-block">
                <h4>요약 통계</h4>
                <div class="summary-grid">
                    <div class="summary-card">
                        <span>학습 완료율</span>
                        <strong id="summaryLearningRate">0%</strong>
                    </div>
                    <div class="summary-card">
                        <span>과제 제출률</span>
                        <strong id="summaryAssignmentRate">0%</strong>
                    </div>
                    <div class="summary-card">
                        <span>완료 학습</span>
                        <strong id="summaryLearningCount">0 / 0</strong>
                    </div>
                    <div class="summary-card">
                        <span>제출 과제</span>
                        <strong id="summaryAssignmentCount">0 / 0</strong>
                    </div>
                </div>
            </div>

            <div class="detail-block">
                <h4>미제출 과제</h4>
                <div id="missingAssignmentList" class="detail-list-box">
                    <div class="empty-inline-text">없음</div>
                </div>
            </div>

            <div class="detail-block">
                <h4>미완료 학습</h4>
					<div id="learningFeedbackList" class="detail-list-box">
					    <div class="empty-inline-text">없음</div>
					</div>
            </div>

            <div class="detail-block">
                <h4>과제 피드백</h4>
                <div id="assignmentFeedbackList" class="detail-list-box">
                    <div class="empty-inline-text">없음</div>
                </div>
            </div>

            <div class="detail-block">
                <h4>교사 코멘트</h4>
                <div id="detailComent" class="detail-comment-box">-</div>
            </div>
        </div>
    </div>
</div>

<script>
    window.reportPageConfig = {
        contextPath: '${pageContext.request.contextPath}',
        classId: '${classId}',
        role: 'parent'
    };
</script>
<script src="${pageContext.request.contextPath}/resources/js/parent/parent-report.js"></script>