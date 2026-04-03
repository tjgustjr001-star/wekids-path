<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-assignment-manage.css">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<section class="teacher-assignment-manage-page"
         id="teacherAssignmentPage"
         data-base-url="${pageContext.request.contextPath}/teacher/classes/${classId}/assignments"
         data-trash-mode="${trashMode ? 'true' : 'false'}">

    <c:set var="now" value="<%=new java.util.Date()%>" />

    <div class="class-page-hero">
        <div class="class-page-hero-icon">
            <span class="menu-icon calendar-mini-icon"></span>
        </div>
        <div class="class-page-hero-text">
            <h1 class="class-page-hero-title">과제 관리</h1>
            <p class="class-page-hero-subtitle">
                <c:choose>
                    <c:when test="${not empty classInfo.className}">${classInfo.className}</c:when>
                    <c:when test="${not empty className}">${className}</c:when>
                    <c:otherwise>현재 클래스</c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>

<div class="teacher-assignment-panel">
    <div class="teacher-page-top-row">
        <div class="teacher-page-title-box">
            <h1 id="assignmentPageTitle">${trashMode ? '휴지통 (과제 관리)' : '과제 관리'}</h1>
        </div>

        <div class="teacher-page-action-row">
            <div class="teacher-search-box">
                <span class="search-icon"></span>
                <input type="text" id="assignmentSearchInput" placeholder="과제명 검색" />
            </div>

            <button type="button" class="icon-line-btn ${trashMode ? 'active' : ''}" id="assignmentTrashToggleBtn" title="휴지통">
                <span class="trash-mini-icon"></span>
            </button>

            <c:if test="${not trashMode}">
                <button type="button" class="teacher-primary-btn" id="openAssignmentCreateModalBtn">
                    <span class="plus-mini-icon"></span>
                    <span>새 과제</span>
                </button>
            </c:if>
        </div>
    </div>

    <div class="teacher-assignment-table-card">
        <div class="teacher-table-scroll">
            <div class="teacher-assignment-table">
                <div class="teacher-assignment-table-head">
                    <div class="col info">과제 정보</div>
                    <div class="col status">상태 / 마감일</div>
                    <div class="col progress">제출 현황</div>
                    <div class="col manage">관리</div>
                </div>

                <div class="teacher-assignment-table-body" id="assignmentTableBody">
                    <c:choose>
                        <c:when test="${not empty assignmentList}">
                            <c:forEach var="assignment" items="${assignmentList}">

                                <fmt:parseDate value="${assignment.deadlineValue}"
                                               pattern="yyyy-MM-dd'T'HH:mm"
                                               var="parsedDeadline" />

                                <c:set var="isClosed" value="${parsedDeadline.time lt now.time}" />
                                <c:set var="isUrgent" value="${not isClosed and (parsedDeadline.time - now.time lt 43200000)}" />

                                <div class="teacher-assignment-row"
                                     data-id="${assignment.id}"
                                     data-title="${assignment.title}"
                                     data-subject="${assignment.subject}"
                                     data-status="${assignment.status}"
                                     data-deadline="${assignment.deadline}"
                                     data-deadline-value="${assignment.deadlineValue}"
                                     data-submit-count="${assignment.submitCount}"
                                     data-total-count="${assignment.totalCount}"
                                     data-need-feedback="${assignment.needFeedback}"
                                     data-submit-format="${assignment.submitFormat}"
                                     data-content="${assignment.content}"
                                     data-deleted="${assignment.deleted}">
                                    <div class="col info">
                                        <div class="assignment-info-wrap">
                                            <div class="assignment-badge-row">
                                                <c:if test="${assignment.deleted}">
                                                    <span class="mini-badge deleted">삭제됨</span>
                                                </c:if>
                                                <span class="mini-badge subject">${assignment.subject}</span>
                                            </div>

                                            <strong class="assignment-title ${assignment.deleted ? 'deleted-text' : 'detail-open-text'}">${assignment.title}</strong>

                                            <c:if test="${not empty assignment.content}">
                                                <p class="assignment-desc">${assignment.content}</p>
                                            </c:if>

                                            <c:if test="${assignment.deleted}">
                                                <span class="deleted-info-text">삭제 상태입니다. 7일 후 영구 삭제됩니다.</span>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="col status">
                                        <div class="assignment-status-box">
                                            <c:choose>
                                                <c:when test="${isClosed}">
                                                    <span class="status-badge gray">마감</span>
                                                </c:when>
                                                <c:when test="${isUrgent}">
                                                    <span class="status-badge warning">마감임박</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge primary">진행중</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="deadline-text">${assignment.deadline}</span>
                                        </div>
                                    </div>

                                    <div class="col progress">
                                        <div class="assignment-progress-wrap">
                                            <div class="progress-text-row">
                                                <span>전체 ${assignment.totalCount}명 중 완료 ${assignment.submitCount}명</span>
                                                <strong>${assignment.progressPercent}%</strong>
                                            </div>
                                            <div class="progress-bar">
                                                <div class="progress-fill" style="width:${assignment.progressPercent}%;"></div>
                                            </div>
                                            <c:if test="${assignment.needFeedback gt 0 and not assignment.deleted}">
                                                <div class="feedback-chip">피드백 필요: ${assignment.needFeedback}명</div>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="col manage">
                                        <c:choose>
                                            <c:when test="${assignment.deleted}">
                                                <div class="trash-action-stack">
                                                    <button type="button" class="row-recover-btn">복구</button>
                                                    <button type="button" class="row-remove-btn">영구삭제</button>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="row-menu-wrap">
                                                    <button type="button" class="icon-action-btn row-menu-toggle-btn" title="더보기">
                                                        <span class="more-mini-icon"></span>
                                                    </button>
                                                    <div class="row-menu-dropdown">
                                                        <button type="button" class="row-menu-item detail-open-btn">상세 보기</button>
                                                        <button type="button" class="row-menu-item status-toggle-btn">
                                                            <c:choose>
                                                                <c:when test="${isClosed}">
                                                                    진행중으로 변경
                                                                </c:when>
                                                                <c:otherwise>
                                                                    마감 처리
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </button>
                                                        <button type="button" class="row-menu-item edit-open-btn">수정</button>
                                                        <button type="button" class="row-menu-item danger row-delete-btn">삭제</button>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="teacher-empty-box" id="assignmentEmptyBox">등록된 과제가 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="teacher-empty-box search-empty" id="assignmentSearchEmptyBox" style="display:none;">검색 결과가 없습니다.</div>
            </div>
        </div>
    </div>
</div>
</section>

<form id="assignmentActionForm" method="post" style="display:none;">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>

<div class="teacher-modal-backdrop" id="assignmentFormModal">
    <div class="teacher-modal teacher-assignment-form-modal">
        <div class="teacher-modal-header">
            <h3 id="assignmentFormModalTitle">새 과제 등록</h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="assignmentFormModal">×</button>
        </div>

        <form class="teacher-modal-form" id="assignmentForm" method="post" action="${pageContext.request.contextPath}/teacher/classes/${classId}/assignments/new">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <div class="teacher-modal-body">
                <div class="form-field">
                    <label for="assignmentTitle">과제명</label>
                    <input type="text" id="assignmentTitle" name="title" placeholder="예: 과학 실험 관찰 보고서" required>
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="assignmentSubject">카테고리 (과목)</label>
                        <select id="assignmentSubject" name="subject">
                            <option value="국어">국어</option>
                            <option value="수학">수학</option>
                            <option value="과학">과학</option>
                            <option value="사회">사회</option>
                            <option value="영어">영어</option>
                        </select>
                    </div>

                    <div class="form-field">
                        <label for="assignmentStatus">상태</label>
                        <select id="assignmentStatus" name="status">
                            <option value="진행중">진행중</option>
                            <option value="마감임박">마감임박</option>
                            <option value="마감">마감</option>
                            <option value="확인완료">확인완료</option>
                        </select>
                    </div>
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="assignmentDeadline">마감일시</label>
                        <input type="datetime-local" id="assignmentDeadline" name="deadline" required>
                    </div>

                    <div class="form-field">
                        <label for="assignmentFormat">제출 형식</label>
                        <select id="assignmentFormat" name="submitFormat">
                            <option value="파일">파일</option>
                            <option value="텍스트">텍스트</option>
                            <option value="이미지">이미지</option>
                        </select>
                    </div>
                </div>

                <div class="form-field">
                    <label for="assignmentMaxEditCount">과제 수정 가능 횟수</label>
                    <input type="number" id="assignmentMaxEditCount" name="maxEditCount" min="0" value="3">
                </div>

                <div class="form-field">
                    <label for="assignmentContent">과제 상세 내용</label>
                    <textarea id="assignmentContent" name="content" rows="5" placeholder="학생들이 수행할 과제의 상세 내용을 입력해주세요."></textarea>
                </div>
            </div>

            <div class="teacher-modal-footer">
                <button type="button" class="teacher-modal-line-btn" data-close-modal="assignmentFormModal">취소</button>
                <button type="submit" class="teacher-primary-btn" id="assignmentFormSubmitBtn">등록 완료</button>
            </div>
        </form>
    </div>
</div>

<div class="teacher-modal-backdrop" id="assignmentDetailModal">
    <div class="teacher-modal teacher-assignment-detail-modal teacher-assignment-detail-modal-v2">
        <div class="teacher-modal-header assignment-detail-header-v2">
            <div class="assignment-detail-title-block">
                <div class="assignment-detail-title-row">
                    <h3 id="assignmentDetailTitle">과제 상세</h3>
                </div>
                <div class="detail-badge-row detail-badge-row-v2">
                    <span class="mini-badge subject" id="detailSubjectBadge">과목</span>
                    <span class="status-badge primary" id="detailStatusBadge">진행중</span>
                </div>
            </div>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="assignmentDetailModal">×</button>
        </div>

        <div class="teacher-modal-body scrollable assignment-detail-body-v2">
            <div class="assignment-detail-top-grid-v2">
                <div class="detail-content-box detail-content-box-v2">
                    <span>과제 상세 내용</span>
                    <p id="detailAssignmentContent">-</p>
                </div>

                <div class="detail-side-stack detail-side-stack-v2">
                    <div class="detail-info-box detail-info-box-v2 detail-deadline-box-v2">
                        <span>마감일</span>
                        <strong class="danger-text" id="detailAssignmentDeadline">-</strong>
                    </div>

                    <div class="detail-info-box warning detail-info-box-v2 detail-feedback-box-v2" id="detailFeedbackNeedBox">
                        <span>피드백 대기</span>
                        <strong id="detailNeedFeedback">0명 피드백 필요</strong>
                    </div>
                </div>
            </div>

            <div class="detail-progress-box detail-progress-box-v2">
                <div class="detail-progress-title-row">
                    <h4>전체 제출률</h4>
                    <strong id="detailSubmissionRate">0%</strong>
                </div>
                <div class="big-progress-bar"><div class="big-progress-fill" id="detailSubmissionBar" style="width:0%;"></div></div>
                <div class="detail-progress-summary-grid detail-progress-summary-grid-v2">
                    <div class="progress-summary-card"><span>제출완료</span><strong class="green-text" id="submittedCountText">0명</strong></div>
                    <div class="progress-summary-card"><span>재제출</span><strong class="blue-text" id="resubmittedCountText">0명</strong></div>
                    <div class="progress-summary-card"><span>늦은제출</span><strong class="orange-text" id="lateCountText">0명</strong></div>
                    <div class="progress-summary-card"><span>미제출</span><strong class="red-text" id="notSubmittedCountText">0명</strong></div>
                </div>
            </div>

            <div class="student-submission-section student-submission-section-v2">
                <div class="student-submission-top student-submission-top-v2">
                    <h4>학생별 제출 현황 (<span id="submissionStudentCount">0</span>명)</h4>
                    <select id="submissionFilterSelect">
                        <option value="all">전체 보기</option>
                        <option value="제출완료">제출완료만</option>
                        <option value="미제출">미제출만</option>
                        <option value="재제출">재제출만</option>
                        <option value="늦은제출">늦은제출만</option>
                        <option value="확인완료">확인완료만</option>
                    </select>
                </div>
                <div class="student-submission-list student-submission-list-v2" id="studentSubmissionList"></div>
            </div>
        </div>

        <div class="teacher-modal-footer between assignment-detail-footer-v2">
            <button type="button" class="teacher-modal-warning-btn assignment-notify-btn-v2" id="notifyUnsubmittedBtn">미제출 학생 알림 발송</button>
            <button type="button" class="teacher-modal-line-btn assignment-close-btn-v2" data-close-modal="assignmentDetailModal">닫기</button>
        </div>
    </div>
</div>


<div class="teacher-modal-backdrop secondary" id="studentSubmissionModal">
    <div class="teacher-modal teacher-student-submission-modal">
        <div class="teacher-modal-header teacher-student-submission-header">
            <div class="teacher-student-submission-head">
                <div class="teacher-student-badge-row">
                    <span class="mini-badge subject" id="studentWorkSubjectBadge">과목</span>
                    <span class="mini-badge" id="studentWorkTypeBadge">파일 제출</span>
                    <span class="mini-badge blue" id="studentWorkStatusBadge">제출완료</span>
                </div>
                <h3 id="studentWorkTitle">과제명</h3>
            </div>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="studentSubmissionModal">×</button>
        </div>

        <div class="teacher-modal-body teacher-student-submission-body">
            <div class="teacher-student-info-box">
                <h4>선생님의 설명</h4>
                <p id="studentWorkAssignmentContent">-</p>
                <div class="teacher-student-meta-row">
                    <span><span class="detail-inline-icon clock"></span>마감일: <strong id="studentWorkDeadline">-</strong></span>
                    <span class="student-remain-edit-wrap" style="display:none;"><span class="detail-inline-icon edit"></span>남은 수정 횟수: <strong id="studentWorkRemainCount">0회</strong></span>
                </div>
            </div>

            <div class="teacher-student-feedback-view" id="teacherStudentFeedbackView" style="display:none;">
                <div class="teacher-student-feedback-inner">
                    <span class="teacher-student-feedback-icon">!</span>
                    <div class="teacher-student-feedback-copy">
                        <h4 id="teacherStudentFeedbackTitle">선생님 피드백</h4>
                        <p id="teacherStudentFeedbackText"></p>
                    </div>
                </div>
            </div>

            <div class="teacher-student-submit-box">
                <h4>내 제출 내용</h4>
                <div class="teacher-student-file-box" id="teacherStudentFileBox">
                    <div class="teacher-student-file-left">
                        <div class="teacher-student-file-icon" id="teacherStudentFileExt">F</div>
                        <div class="teacher-student-file-meta">
                            <strong id="teacherStudentFileName">-</strong>
                            <span id="teacherStudentFileSize">0 KB</span>
                        </div>
                    </div>
                    <button type="button" class="teacher-student-download-btn" id="teacherStudentDownloadBtn">다운로드</button>
                </div>
                <div class="teacher-student-text-box" id="teacherStudentTextBox">
                    <p id="teacherStudentSubmissionText"></p>
                </div>
                <div class="teacher-student-submitted-state" id="teacherStudentSubmittedState">제출이 완료되었습니다.</div>
            </div>

                    </div>

        <div class="teacher-modal-footer teacher-student-submission-footer">
    <button type="button" class="teacher-modal-line-btn" data-close-modal="studentSubmissionModal">닫기</button>

    <button type="button" class="teacher-student-feedback-btn" id="teacherStudentFeedbackBtn" style="display:none;">피드백</button>
    <button type="button" class="teacher-primary-btn teacher-student-complete-btn" id="teacherStudentCompleteBtn" style="display:none;">제출완료로 변경</button>
    <button type="button" class="teacher-primary-btn teacher-student-reject-btn" id="teacherStudentRejectBtn">과제 반려</button>
</div>
    </div>
</div>


<div class="teacher-modal-backdrop secondary" id="teacherRejectModal">
    <div class="teacher-modal teacher-student-submission-modal">
        <div class="teacher-modal-header teacher-student-submission-header">
            <div class="teacher-student-submission-head">
                <h3>반려 사유 작성</h3>
            </div>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="teacherRejectModal">×</button>
        </div>
        <div class="teacher-modal-body teacher-student-submission-body">
            <div class="teacher-student-reject-panel teacher-student-reject-modal-panel">
                <div class="teacher-student-reject-label-row">
                    <h4>학생에게 보여줄 반려 사유</h4>
                    <span class="teacher-student-current-reason" id="teacherRejectCurrentReasonLabel" style="display:none;">기존 반려 사유가 있습니다.</span>
                </div>
                <textarea id="teacherStudentRejectReason" placeholder="학생에게 보여줄 반려 사유를 입력해주세요."></textarea>
            </div>
        </div>
        <div class="teacher-modal-footer teacher-student-submission-footer">
            <button type="button" class="teacher-modal-line-btn" data-close-modal="teacherRejectModal">닫기</button>
            <button type="button" class="teacher-primary-btn teacher-student-reject-btn" id="teacherRejectConfirmBtn">과제 반려</button>
        </div>
    </div>
</div>

<div class="teacher-modal-backdrop secondary" id="teacherFeedbackModal">
    <div class="teacher-modal teacher-student-submission-modal">
        <div class="teacher-modal-header teacher-student-submission-header">
            <div class="teacher-student-submission-head">
                <h3>피드백 작성</h3>
            </div>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="teacherFeedbackModal">×</button>
        </div>
        <div class="teacher-modal-body teacher-student-submission-body">
            <div class="teacher-student-reject-panel">
                <div class="teacher-student-reject-label-row">
                    <h4>학생에게 보낼 피드백</h4>
                </div>
                <textarea id="teacherStudentFeedbackContent" placeholder="학생과 학부모가 확인할 피드백을 입력해주세요."></textarea>
            </div>
        </div>
        <div class="teacher-modal-footer teacher-student-submission-footer">
            <button type="button" class="teacher-modal-line-btn" data-close-modal="teacherFeedbackModal">닫기</button>
            <button type="button" class="teacher-primary-btn teacher-student-complete-btn" id="teacherFeedbackConfirmBtn">확인 완료</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/teacher/teacher-assignment-manage.js"></script>