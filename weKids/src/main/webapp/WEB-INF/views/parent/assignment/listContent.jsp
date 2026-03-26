<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-assignment.css">

<section class="parent-assignment-page">
    <div class="parent-assignment-topbar">
        <h2 class="parent-assignment-page-title">과제 조회</h2>

        <div class="parent-assignment-child-select-wrap">
            <span class="parent-assignment-usercheck-icon"></span>
            <select id="parentAssignmentChildSelect" class="parent-assignment-child-select">
                <c:forEach var="child" items="${assignmentChildren}">
                    <option value="${child.id}" ${child.id eq selectedChildId ? 'selected' : ''}>
                        ${child.name} (${child.grade})
                    </option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="parent-assignment-info-box">
        <span class="parent-assignment-alert-icon"></span>
        <div>
            <p class="title">학부모 조회 모드</p>
            <p class="desc">자녀의 과제 현황을 확인할 수 있습니다. 과제 제출은 학생 계정에서만 가능합니다.</p>
        </div>
    </div>

    <div class="parent-assignment-list-group assignment-child-panel active" data-child-id="1">
        <div class="parent-assignment-grid">
            <c:forEach var="assignment" items="${child1Assignments}">
                <div class="parent-assignment-card"
                     data-id="${assignment.id}"
                     data-title="${assignment.title}"
                     data-subject="${assignment.subject}"
                     data-status="${assignment.status}"
                     data-deadline="${assignment.deadline}"
                     data-type="${assignment.type}"
                     data-submitted="${assignment.submitted}"
                     data-submitted-at="${assignment.submittedAt}"
                     data-feedback="${assignment.feedback}"
                     data-content="${assignment.content}"
                     data-my-submission="${assignment.mySubmission}">

                    <div>
                        <div class="parent-assignment-card-badges">
                            <span class="parent-assignment-status-badge
                                <c:choose>
                                    <c:when test="${assignment.status eq '진행'}">progress</c:when>
                                    <c:when test="${assignment.status eq '마감임박'}">urgent</c:when>
                                    <c:when test="${assignment.status eq '제출완료'}">done</c:when>
                                    <c:when test="${assignment.status eq '반려'}">reject</c:when>
                                    <c:otherwise>default</c:otherwise>
                                </c:choose>
                            ">${assignment.status}</span>

                            <span class="parent-assignment-deadline-badge">
                                <span class="parent-assignment-clock-icon"></span>
                                ${assignment.deadline}
                            </span>
                        </div>

                        <div class="parent-assignment-meta-line">
                            <span class="parent-assignment-subject-badge">${assignment.subject}</span>
                            <span class="parent-assignment-type-badge">${assignment.type}</span>
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
                                    <span>제출 완료 <c:if test="${not empty assignment.submittedAt}">(${assignment.submittedAt})</c:if></span>
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
    </div>

    <div class="parent-assignment-list-group assignment-child-panel" data-child-id="2">
        <div class="parent-assignment-grid">
            <c:forEach var="assignment" items="${child2Assignments}">
                <div class="parent-assignment-card"
                     data-id="${assignment.id}"
                     data-title="${assignment.title}"
                     data-subject="${assignment.subject}"
                     data-status="${assignment.status}"
                     data-deadline="${assignment.deadline}"
                     data-type="${assignment.type}"
                     data-submitted="${assignment.submitted}"
                     data-submitted-at="${assignment.submittedAt}"
                     data-feedback="${assignment.feedback}"
                     data-content="${assignment.content}"
                     data-my-submission="${assignment.mySubmission}">

                    <div>
                        <div class="parent-assignment-card-badges">
                            <span class="parent-assignment-status-badge
                                <c:choose>
                                    <c:when test="${assignment.status eq '진행'}">progress</c:when>
                                    <c:when test="${assignment.status eq '마감임박'}">urgent</c:when>
                                    <c:when test="${assignment.status eq '제출완료'}">done</c:when>
                                    <c:when test="${assignment.status eq '반려'}">reject</c:when>
                                    <c:otherwise>default</c:otherwise>
                                </c:choose>
                            ">${assignment.status}</span>

                            <span class="parent-assignment-deadline-badge">
                                <span class="parent-assignment-clock-icon"></span>
                                ${assignment.deadline}
                            </span>
                        </div>

                        <div class="parent-assignment-meta-line">
                            <span class="parent-assignment-subject-badge">${assignment.subject}</span>
                            <span class="parent-assignment-type-badge">${assignment.type}</span>
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
                                    <span>제출 완료 <c:if test="${not empty assignment.submittedAt}">(${assignment.submittedAt})</c:if></span>
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
    </div>

    <div class="parent-assignment-empty-box assignment-child-empty hidden" data-child-id="empty">
        <p>아직 등록된 과제가 없습니다.</p>
    </div>
</section>

<div class="parent-assignment-modal-overlay" id="parentAssignmentModalOverlay">
    <div class="parent-assignment-modal">
        <div class="parent-assignment-modal-header">
            <div>
                <div class="parent-assignment-modal-badges">
                    <span class="parent-assignment-modal-subject" id="parentAssignmentModalSubject">과학</span>
                    <span class="parent-assignment-modal-type" id="parentAssignmentModalType">파일 제출</span>
                    <span class="parent-assignment-modal-status" id="parentAssignmentModalStatus">진행</span>
                </div>
                <h2 id="parentAssignmentModalTitle">과제 제목</h2>
            </div>

            <button type="button" class="parent-modal-close-btn" id="parentAssignmentModalCloseBtn">×</button>
        </div>

        <div class="parent-assignment-modal-body">
            <div class="parent-assignment-detail-box">
                <h3>선생님의 설명</h3>
                <p id="parentAssignmentModalContent">과제 상세 내용입니다.</p>

                <div class="parent-assignment-deadline-row">
                    <span class="parent-assignment-clock-icon"></span>
                    <span class="label">마감일:</span>
                    <strong id="parentAssignmentModalDeadline">오늘 18:00</strong>
                </div>
            </div>

            <div class="parent-assignment-reject-box" id="parentAssignmentRejectBox">
                <div class="inner">
                    <span class="parent-assignment-alert-icon"></span>
                    <div>
                        <h4>반려 사유</h4>
                        <p id="parentAssignmentModalFeedback"></p>
                    </div>
                </div>
            </div>

            <div class="parent-assignment-submitted-box" id="parentAssignmentSubmittedBox">
                <h3>제출 내용</h3>

                <div class="parent-assignment-file-box" id="parentAssignmentSubmittedFileBox">
                    <div class="icon-wrap">
                        <span class="parent-assignment-paperclip-icon"></span>
                    </div>
                    <div class="file-info">
                        <p class="file-name">과제_제출_파일.hwp</p>
                        <p class="file-size">1.2 MB</p>
                    </div>
                    <button type="button" class="download-btn">다운로드</button>
                </div>

                <div class="parent-assignment-text-box" id="parentAssignmentSubmittedTextBox">
                    <p id="parentAssignmentSubmittedText">제출된 텍스트 내용입니다.</p>
                </div>

                <div class="parent-assignment-submitted-state">
                    <span class="parent-assignment-check-icon"></span>
                    <span id="parentAssignmentSubmittedAtText">제출 완료</span>
                </div>
            </div>
        </div>

        <div class="parent-assignment-modal-footer">
            <button type="button" class="parent-secondary-btn" id="parentAssignmentCloseFooterBtn">닫기</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/parent/parent-assignment.js"></script>