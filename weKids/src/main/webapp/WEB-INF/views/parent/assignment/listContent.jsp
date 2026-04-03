<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-assignment.css">

<section class="parent-assignment-page"
         data-context-path="${pageContext.request.contextPath}"
         data-class-id="${classId}"
         data-selected-child-id="${selectedChildId}">

    <div class="class-page-hero">
        <div class="class-page-hero-icon">
            <span class="menu-icon calendar-mini-icon"></span>
        </div>
        <div class="class-page-hero-text">
            <h1 class="class-page-hero-title">과제</h1>
            <p class="class-page-hero-subtitle">
                <c:choose>
                    <c:when test="${not empty classInfo.className}">${classInfo.className}</c:when>
                    <c:when test="${not empty className}">${className}</c:when>
                    <c:otherwise>현재 클래스</c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>

<div class="parent-assignment-panel">
    <div class="parent-assignment-topbar">
        <h2 class="parent-assignment-page-title">과제 조회</h2>

        <c:if test="${not empty assignmentChildren}">
            <div class="parent-assignment-child-select-wrap">
                <span class="parent-assignment-usercheck-icon"></span>
                <select id="parentAssignmentChildSelect" class="parent-assignment-child-select">
                    <c:forEach var="child" items="${assignmentChildren}">
                        <option value="${child.studentId}" ${child.studentId eq selectedChildId ? 'selected' : ''}>
                            ${child.studentName} (초등 ${child.grade}학년)
                        </option>
                    </c:forEach>
                </select>
            </div>
        </c:if>
    </div>

<!--     <div class="parent-assignment-info-box">
        <span class="parent-assignment-alert-icon"></span>
        <div>
            <p class="title">학부모 조회 모드</p>
            <p class="desc">자녀의 과제 현황을 확인할 수 있습니다. 과제 제출은 학생 계정에서만 가능합니다.</p>
        </div>
    </div> -->

    <c:choose>
        <c:when test="${empty assignmentChildren}">
            <div class="parent-assignment-empty-box">
                <p>이 클래스에 연결된 자녀가 없습니다.</p>
            </div>
        </c:when>
        <c:when test="${empty assignmentList}">
            <div class="parent-assignment-empty-box">
                <p>아직 등록된 과제가 없습니다.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="parent-assignment-grid">
                <c:forEach var="assignment" items="${assignmentList}">
                    <div class="parent-assignment-card"
                         data-id="${assignment.id}"
                         data-title="${fn:escapeXml(assignment.title)}"
                         data-subject="${fn:escapeXml(assignment.subject)}"
                         data-status="${fn:escapeXml(assignment.status)}"
                         data-deadline="${fn:escapeXml(assignment.deadline)}"
                         data-type="${fn:escapeXml(assignment.submitFormatLabel)}"
                         data-submitted="${assignment.submitted}"
                         data-submitted-at="${fn:escapeXml(assignment.submittedAt)}"
                         data-feedback="${fn:escapeXml(assignment.feedback)}"
                         data-content="${fn:escapeXml(assignment.content)}"
                         data-my-submission="${fn:escapeXml(assignment.mySubmission)}"
                         data-attached-file="${fn:escapeXml(assignment.attachedFile)}"
                         data-file-size="${assignment.fileSize}"
                         data-download-url="${pageContext.request.contextPath}/parent/classes/${classId}/assignments/${assignment.id}/download?childId=${selectedChildId}">

                        <div>
                            <div class="parent-assignment-card-badges">
                                <span class="parent-assignment-status-badge
                                    <c:choose>
                                        <c:when test="${assignment.status eq '진행'}">progress</c:when>
                                        <c:when test="${assignment.status eq '마감임박'}">urgent</c:when>
                                        <c:when test="${assignment.status eq '제출완료' or assignment.status eq '확인완료'}">done</c:when>
                                        <c:when test="${assignment.status eq '늦은제출'}">late</c:when>
                                        <c:when test="${assignment.status eq '반려'}">reject</c:when>
                                        <c:otherwise>default</c:otherwise>
                                    </c:choose>
                                ">${assignment.status}</span>

                                <span class="parent-assignment-deadline-badge">
                                    <span class="parent-assignment-clock-icon"></span>
                                    <c:choose>
                                        <c:when test="${not empty assignment.deadline}">${assignment.deadline}</c:when>
                                        <c:otherwise>마감일 없음</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <div class="parent-assignment-meta-line">
                                <span class="parent-assignment-subject-badge">${assignment.subject}</span>
                                <span class="parent-assignment-type-badge">${assignment.submitFormatLabel}</span>
                            </div>

                            <h3 class="parent-assignment-card-title">${assignment.title}</h3>

                            <c:if test="${not empty assignment.feedback}">
                                <div class="parent-assignment-feedback-preview">
                                    <span class="parent-assignment-alert-icon small"></span>
                                    <div>
                                        <p class="feedback-title">선생님 피드백</p>
                                        <span>${assignment.feedback}</span>
                                    </div>
                                </div>
                            </c:if>
                        </div>

                        <div class="parent-assignment-card-footer">
                            <c:choose>
                                <c:when test="${assignment.submitted}">
                                    <div class="parent-assignment-submit-state done">
                                        <span class="parent-assignment-check-icon"></span>
                                        <span>
                                            제출 완료
                                            <c:if test="${not empty assignment.submittedAt}">(${assignment.submittedAt})</c:if>
                                        </span>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="parent-assignment-submit-state">
                                        <span class="parent-assignment-fileplus-icon"></span>
                                        <span>미제출</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <button type="button" class="parent-assignment-detail-btn">상세 보기</button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
    </div>
</section>

<div class="parent-assignment-modal-overlay" id="parentAssignmentModalOverlay">
    <div class="parent-assignment-modal">
        <div class="parent-assignment-modal-header">
            <div>
                <div class="parent-assignment-modal-badges">
                    <span class="parent-assignment-modal-subject" id="parentAssignmentModalSubject"></span>
                    <span class="parent-assignment-modal-type" id="parentAssignmentModalType"></span>
                    <span class="parent-assignment-modal-status" id="parentAssignmentModalStatus"></span>
                </div>
                <h3 class="parent-assignment-modal-title" id="parentAssignmentModalTitle"></h3>
            </div>
            <button type="button" class="parent-assignment-modal-close" id="parentAssignmentModalCloseBtn">×</button>
        </div>

        <div class="parent-assignment-modal-body">
            <div class="parent-assignment-modal-section teacher-box">
                <p class="section-title">선생님의 설명</p>
                <p class="section-content" id="parentAssignmentModalContent"></p>
                <div class="parent-assignment-modal-deadline-row">
                    <span class="parent-assignment-clock-icon"></span>
                    <span>마감일: <strong id="parentAssignmentModalDeadline"></strong></span>
                </div>
            </div>

            <div class="parent-assignment-modal-section reject-box" id="parentAssignmentRejectBox">
                <div class="parent-assignment-feedback-preview modal">
                    <span class="parent-assignment-alert-icon small"></span>
                    <div>
                        <p class="feedback-title" id="parentAssignmentFeedbackTitle">피드백</p>
                        <span id="parentAssignmentModalFeedback"></span>
                    </div>
                </div>
            </div>

            <div class="parent-assignment-modal-section submitted-box" id="parentAssignmentSubmittedBox">
                <p class="section-title">제출 내용</p>

                <div class="parent-assignment-file-box" id="parentAssignmentSubmittedFileBox">
                    <div class="parent-assignment-file-icon"></div>
                    <div class="parent-assignment-file-meta">
                        <strong id="parentAssignmentFileName"></strong>
                        <span id="parentAssignmentFileSize"></span>
                    </div>
                    <a href="#" class="parent-assignment-download-btn" id="parentAssignmentDownloadBtn">다운로드</a>
                </div>

                <div class="parent-assignment-text-box" id="parentAssignmentSubmittedTextBox">
                    <p id="parentAssignmentSubmittedText"></p>
                </div>

                <div class="parent-assignment-submit-done-row">
                    <span class="parent-assignment-check-icon"></span>
                    <span id="parentAssignmentSubmittedAtText">제출 완료</span>
                </div>
            </div>
        </div>

        <div class="parent-assignment-modal-footer">
            <button type="button" class="parent-assignment-footer-close" id="parentAssignmentModalCloseFooterBtn">닫기</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/parent/parent-assignment.js"></script>
