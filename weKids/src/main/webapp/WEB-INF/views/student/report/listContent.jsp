<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-report.css">

<section class="student-report-page">
    <div class="student-report-header">
        <div>
            <h2>리포트 확인</h2>
            <p>주간/월간 학습 리포트를 확인할 수 있습니다.</p>
        </div>
    </div>

    <!-- 필터 -->
    <div class="report-filter-section">
        <form action="${pageContext.request.contextPath}/student/classes/${classId}/reports"
              method="get"
              class="report-filter-form">
            <div class="filter-chip-group">
			    <button type="button" class="filter-chip is-active" data-report-type="ALL">전체</button>
			    <button type="button" class="filter-chip" data-report-type="PERSONAL">개인</button>
			    <button type="button" class="filter-chip" data-report-type="CLASS">학급</button>
			</div>
        </form>
    </div>

    <!-- 목록 -->
    <div class="report-list-section">
        <div class="section-title-row">
            <h3>리포트 목록</h3>
        </div>

        <div class="student-report-list">
            <c:choose>
                <c:when test="${not empty reportList}">
                    <c:forEach var="report" items="${reportList}">
                        <div class="student-report-card" data-report-type="${report.reportType}">
                            <div class="report-card-top">
                                <div class="report-badge-group">
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
                                <button type="button"
                                        class="detail-btn"
                                        data-report-id="${report.reportId}">
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
                <div class="detail-info-box">
                    <span>담당 교사</span>
                    <strong id="detailTeacherName"></strong>
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
        role: 'student'
    };
</script>
<script src="${pageContext.request.contextPath}/resources/js/student/student-report.js"></script>