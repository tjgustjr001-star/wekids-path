<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-report.css">

<section class="teacher-report-page">
    <div class="teacher-report-header">
        <div>
            <h2>리포트 관리</h2>
            <p>주간/월간 리포트를 생성하고, 학부모 열람 여부를 확인할 수 있습니다.</p>
        </div>
    </div>

    <!-- 생성 영역 -->
    <div class="report-section report-generate-section">
        <div class="section-title-row">
            <h3>리포트 생성</h3>
        </div>

        <form action="${pageContext.request.contextPath}/teacher/classes/${classId}/reports/generate"
              method="post"
              class="report-generate-form"
              id="reportGenerateForm">

            <div class="form-grid">
                <div class="form-group">
                    <label for="reportType">리포트 유형</label>
                    <select name="reportType" id="reportType">
                        <option value="PERSONAL">개인 리포트</option>
                        <option value="CLASS">학급 요약 리포트</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="periodType">기간 유형</label>
                    <select name="periodType" id="periodType">
                        <option value="WEEKLY">주간</option>
                        <option value="MONTHLY">월간</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="startDate">시작일</label>
                    <input type="date" name="startDate" id="startDate" required>
                </div>

                <div class="form-group">
                    <label for="endDate">종료일</label>
                    <input type="date" name="endDate" id="endDate" required>
                </div>
            </div>

            <div class="form-group student-select-group" id="studentSelectGroup">
                <label>대상 학생</label>
                <div class="student-check-list">
                    <c:choose>
                        <c:when test="${not empty studentList}">
                            <c:forEach var="student" items="${studentList}">
                                <label class="student-check-item">
                                    <input type="checkbox" name="studentIds" value="${student.studentId}">
                                    <span>${student.studentName}</span>
                                </label>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-inline-text">선택 가능한 학생 목록이 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="form-group">
                <label for="coment">교사 코멘트</label>
                <textarea name="coment"
                          id="coment"
                          rows="5"
                          placeholder="가정 지도 포인트, 다음 주 계획 등을 입력하세요."></textarea>
            </div>

            <div class="form-action-row">
                <button type="submit" class="primary-btn">리포트 생성</button>
            </div>
        </form>
    </div>

    <!-- 목록 영역 -->
    <div class="report-section report-list-section">
        <div class="section-title-row">
            <h3>생성된 리포트 목록</h3>
        </div>

        <div class="report-list-table">
            <div class="report-list-head">
                <div class="col title">제목</div>
                <div class="col type">유형</div>
                <div class="col period">기간</div>
                <div class="col target">대상</div>
                <div class="col view">학부모 열람</div>
                <div class="col created">생성일</div>
                <div class="col action">상세</div>
            </div>

            <c:choose>
                <c:when test="${not empty reportList}">
                    <c:forEach var="report" items="${reportList}">
                        <div class="report-list-row"
                             data-report-id="${report.reportId}"
                             data-student-id="${report.studentId}">
                            <div class="col title">
                                <div class="report-title-text">${report.title}</div>
                            </div>

                            <div class="col type">
                                <span class="report-badge ${report.reportType eq 'CLASS' ? 'is-class' : 'is-personal'}">
                                    <c:choose>
                                        <c:when test="${report.reportType eq 'CLASS'}">학급</c:when>
                                        <c:otherwise>개인</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <div class="col period">
                                <div>${report.startDate} ~ ${report.endDate}</div>
                                <div class="sub-text">
                                    <c:choose>
                                        <c:when test="${report.periodType eq 'MONTHLY'}">월간</c:when>
                                        <c:otherwise>주간</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="col target">
                                <c:choose>
                                    <c:when test="${report.reportType eq 'CLASS'}">
                                        <span>학급 전체</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>${report.studentName}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col view">
                                <c:choose>
                                    <c:when test="${report.parentViewCount != null and report.parentViewCount > 0}">
                                        <span class="view-status is-read">열람</span>
                                        <div class="sub-text">${report.parentViewedAt}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="view-status is-unread">미열람</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col created">
                                ${report.createdAt}
                            </div>

                            <div class="col action">
                                <button type="button"
                                        class="detail-btn"
                                        data-report-id="${report.reportId}">
                                    보기
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="report-empty-box">
                        생성된 리포트가 없습니다.
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
                    <span>유형</span>
                    <strong id="detailReportType"></strong>
                </div>
                <div class="detail-info-box">
                    <span>기간</span>
                    <strong id="detailPeriod"></strong>
                </div>
                <div class="detail-info-box">
                    <span>대상</span>
                    <strong id="detailStudentName"></strong>
                </div>
                <div class="detail-info-box">
                    <span>생성일</span>
                    <strong id="detailCreatedAt"></strong>
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
                <h4>학습 피드백</h4>
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
        role: 'teacher'
    };
</script>
<script src="${pageContext.request.contextPath}/resources/js/teacher/teacher-report.js"></script>