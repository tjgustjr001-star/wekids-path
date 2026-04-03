<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-assignment.css">

<section class="student-assignment-page" data-context-path="${pageContext.request.contextPath}" data-class-id="${classId}">
    <input type="hidden" id="assignmentCsrfName" value="${_csrf.parameterName}">
    <input type="hidden" id="assignmentCsrfToken" value="${_csrf.token}">

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

    <div class="class-page-panel assignment-content-panel">
        <div class="assignment-topbar">
        <h2 class="assignment-page-title">내 과제 목록</h2>
    </div>

        <div class="assignment-grid">
        <c:forEach var="assignment" items="${assignmentList}">
            <div class="assignment-card"
                 data-id="${assignment.id}"
                 data-title="${assignment.title}"
                 data-subject="${assignment.subject}"
                 data-status="${assignment.status}"
                 data-deadline="${assignment.deadline}"
                 data-deadline-value="${assignment.deadlineValue}"
                 data-type="${assignment.submitFormatLabel}"
                 data-submit-format="${assignment.submitFormat}"
                 data-submitted="${assignment.submitted}"
                 data-submitted-at="${assignment.submittedAt}"
                 data-feedback="${assignment.feedback}"
                 data-content="${assignment.content}"
                 data-my-submission="${assignment.mySubmission}"
                 data-attached-file="${assignment.attachedFile}"
                 data-file-size="${assignment.fileSize}"
                 data-can-submit="${assignment.canSubmit}"
                 data-can-resubmit="${assignment.canResubmit}"
                 data-can-download="${assignment.canDownload}"
                 data-expired="${assignment.expired}"
                 data-submit-count="${assignment.submitCount}"
                 data-max-edit-count="${assignment.maxEditCount}"
                 data-remaining-edit-count="${assignment.remainingEditCount}"
                 data-detail-url="${pageContext.request.contextPath}/student/classes/${classId}/assignments/${assignment.id}"
                 data-submit-url="${pageContext.request.contextPath}/student/classes/${classId}/assignments/${assignment.id}/submit"
                 data-download-url="${pageContext.request.contextPath}/student/classes/${classId}/assignments/${assignment.id}/download">

                <div class="assignment-card-top">
                    <div class="assignment-card-badges">
                        <span class="assignment-status-badge
                            <c:choose>
                                <c:when test="${assignment.status eq '진행'}">progress</c:when>
                                <c:when test="${assignment.status eq '마감임박'}">urgent</c:when>
                                <c:when test="${assignment.status eq '제출완료' or assignment.status eq '확인완료'}">done</c:when>
                                <c:when test="${assignment.status eq '늦은제출'}">late</c:when>
                                <c:when test="${assignment.status eq '반려'}">reject</c:when>
                                <c:otherwise>default</c:otherwise>
                            </c:choose>
                        ">
                            ${assignment.status}
                        </span>

                        <span class="assignment-deadline-badge">
                            <span class="assignment-clock-icon"></span>
                            ${assignment.deadline}
                        </span>
                    </div>

                    <div class="assignment-meta-line">
                        <span class="assignment-subject-badge">${assignment.subject}</span>
                        <span class="assignment-type-badge">${assignment.submitFormatLabel}</span>
                    </div>

                    <h3 class="assignment-title">${assignment.title}</h3>

                    <c:if test="${not empty assignment.feedback}">
                        <div class="assignment-feedback-preview">
                            <span class="assignment-alert-icon"></span>
                            <span>${assignment.feedback}</span>
                        </div>
                    </c:if>
                </div>

                <div class="assignment-card-bottom">
                    <c:choose>
                        <c:when test="${assignment.status eq '반려'}">
                            <div class="assignment-submit-state reject">
                                <span class="assignment-alert-icon"></span>
                                <span>반려 <c:if test="${not empty assignment.submittedAt}">(${assignment.submittedAt})</c:if></span>
                            </div>
                        </c:when>
                        <c:when test="${assignment.submitted}">
                            <div class="assignment-submit-state done">
                                <span class="assignment-check-icon"></span>
                                <span><c:choose><c:when test="${assignment.status eq '확인완료'}">확인 완료</c:when><c:when test="${assignment.status eq '늦은제출'}">늦은 제출 완료</c:when><c:otherwise>제출 완료</c:otherwise></c:choose> <c:if test="${not empty assignment.submittedAt}">(${assignment.submittedAt})</c:if></span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="assignment-submit-state">
                                <span class="assignment-fileplus-icon"></span>
                                <span>미제출</span>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <button type="button" class="assignment-open-btn
                        <c:choose>
                            <c:when test="${assignment.canSubmit or assignment.status eq '반려'}">primary</c:when>
                            <c:otherwise>secondary</c:otherwise>
                        </c:choose>
                    "
                    >
                        <c:choose>
                            <c:when test="${assignment.status eq '반려'}">수정하여 재제출</c:when>
                            <c:when test="${assignment.expired and not assignment.submitted}">늦은 제출하기</c:when>
                            <c:when test="${not assignment.submitted}">제출하기</c:when>
                            <c:otherwise>제출 내용 보기</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </div>
        </c:forEach>
        </div>
    </div>
</section>

<div class="assignment-modal-overlay" id="assignmentModalOverlay">
    <div class="assignment-modal">
        <div class="assignment-modal-header">
            <div class="assignment-modal-head-left">
                <div class="assignment-modal-badges">
                    <span class="assignment-modal-subject" id="assignmentModalSubject">과학</span>
                    <span class="assignment-modal-type" id="assignmentModalType"></span>
                    <span class="assignment-modal-status" id="assignmentModalStatus">진행</span>
                </div>
                <h2 class="assignment-modal-title" id="assignmentModalTitle">과제 제목</h2>
            </div>

            <button type="button" class="assignment-modal-close-btn" id="assignmentModalCloseBtn">×</button>
        </div>

        <div class="assignment-modal-body">
            <div class="assignment-detail-view" id="assignmentDetailView">
                <div class="assignment-info-box">
                    <h3 class="assignment-info-title">선생님의 설명</h3>
                    <p class="assignment-info-content" id="assignmentModalContent"></p>

                    <div class="assignment-info-meta">
                        <div class="assignment-info-meta-item">
                            <span class="assignment-clock-icon"></span>
                            <span class="assignment-meta-label">마감일:</span>
                            <strong id="assignmentModalDeadline">오늘 18:00</strong>
                        </div>
                        <div class="assignment-info-meta-item">
                            <span class="assignment-edit-icon"></span>
                            <span class="assignment-meta-label">남은 수정 횟수:</span>
                            <strong id="assignmentModalRemainCount">0회</strong>
                        </div>
                    </div>
                </div>

                <div class="assignment-feedback-box" id="assignmentRejectBox">
                    <div class="assignment-feedback-box-inner">
                        <span class="assignment-alert-icon large"></span>
                        <div>
                            <h4 id="assignmentFeedbackTitle">피드백</h4>
                            <p id="assignmentModalFeedback"></p>
                        </div>
                    </div>
                </div>

                <div class="assignment-submitted-box" id="assignmentSubmittedBox">
                    <h3 class="assignment-info-title">내 제출 내용</h3>

                    <div class="assignment-file-box" id="assignmentSubmittedFileBox">
                        <div class="assignment-file-icon-wrap">
                            <span class="assignment-paperclip-icon"></span>
                        </div>
                        <div class="assignment-file-info">
                            <p class="assignment-file-name" id="assignmentSubmittedFileName">파일명</p>
                            <p class="assignment-file-size" id="assignmentSubmittedFileSize">0 KB</p>
                        </div>
                        <button type="button" class="assignment-download-btn" id="assignmentDownloadBtn">다운로드</button>
                    </div>

                    <div class="assignment-submission-text-box" id="assignmentSubmittedTextBox">
                        <p id="assignmentSubmittedText"></p>
                    </div>

                    <div class="assignment-submitted-state">
                        <span class="assignment-check-icon"></span>
                        <span id="assignmentSubmittedAtText">제출이 완료되었습니다.</span>
                    </div>
                </div>
            </div>

            <div class="assignment-submit-view" id="assignmentSubmitView">
                <div class="assignment-submit-box">
                    <h3 class="assignment-info-title">과제 제출 작성</h3>

                    <div class="assignment-submit-inner">
                        <div class="assignment-file-upload-wrap" id="assignmentFileUploadWrap">
                            <div class="assignment-selected-file-box" id="assignmentSelectedFileBox">
                                <div class="assignment-file-icon-wrap">
                                    <span class="assignment-paperclip-icon"></span>
                                </div>
                                <div class="assignment-file-info">
                                    <p class="assignment-file-name" id="assignmentSelectedFileName">파일명</p>
                                    <p class="assignment-file-size" id="assignmentSelectedFileSize">0 KB</p>
                                </div>
                                <button type="button" class="assignment-remove-file-btn" id="assignmentRemoveFileBtn">×</button>
                            </div>

                            <label class="assignment-file-dropzone" id="assignmentFileDropzone">
                                <input type="file" id="assignmentFileInput" hidden>
                                <div class="assignment-upload-icon-wrap">
                                    <span class="assignment-upload-icon"></span>
                                </div>
                                <p class="assignment-dropzone-title">클릭하여 파일을 선택하거나 드래그하세요</p>
                                <p class="assignment-dropzone-desc" id="assignmentDropzoneDesc">지원 형식: HWP, PDF, JPG, PNG (최대 50MB)</p>
                            </label>
                        </div>

                        <div class="assignment-textarea-wrap" id="assignmentTextareaWrap">
                            <textarea id="assignmentSubmissionTextarea"
                                      class="assignment-submission-textarea"
                                      placeholder="과제에 대한 설명이나 내용을 입력해주세요..."></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="assignment-modal-footer">
            <div class="assignment-detail-actions" id="assignmentDetailActions">
                <button type="button" class="assignment-footer-btn secondary" id="assignmentCloseBtn">닫기</button>
                <button type="button" class="assignment-footer-btn primary" id="assignmentGoSubmitBtn">과제 제출하러 가기</button>
            </div>

            <div class="assignment-submit-actions" id="assignmentSubmitActions">
                <button type="button" class="assignment-footer-btn secondary" id="assignmentBackBtn">이전으로</button>
                <button type="button" class="assignment-footer-btn primary" id="assignmentSubmitBtn">
                    <span class="assignment-submit-btn-text" id="assignmentSubmitBtnText">과제 제출하기</span>
                </button>
            </div>
        </div>
    </div>
</div>

<div class="assignment-toast-container" id="assignmentToastContainer"></div>
<script src="${pageContext.request.contextPath}/resources/js/student/student-assignment.js"></script>
