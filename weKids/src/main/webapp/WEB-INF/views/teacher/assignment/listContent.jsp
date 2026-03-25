<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-assignment-manage.css">

<section class="teacher-assignment-manage-page">
    <div class="teacher-page-top-row">
        <div class="teacher-page-title-box">
            <h1 id="assignmentPageTitle">과제 관리</h1>
        </div>

        <div class="teacher-page-action-row">
            <div class="teacher-search-box">
                <span class="search-icon"></span>
                <input type="text" id="assignmentSearchInput" placeholder="과제명 검색" />
            </div>

            <button type="button" class="icon-line-btn" id="assignmentTrashToggleBtn" title="휴지통">
                <span class="trash-mini-icon"></span>
            </button>

            <button type="button" class="teacher-primary-btn" id="openAssignmentCreateModalBtn">
                <span class="plus-mini-icon"></span>
                <span>새 과제</span>
            </button>
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
                                <div class="teacher-assignment-row"
                                     data-id="${assignment.id}"
                                     data-title="${assignment.title}"
                                     data-subject="${assignment.subject}"
                                     data-status="${assignment.status}"
                                     data-deadline="${assignment.deadline}"
                                     data-submit-count="${assignment.submitCount}"
                                     data-total-count="${assignment.totalCount}"
                                     data-need-feedback="${assignment.needFeedback}"
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

                                            <strong class="assignment-title ${assignment.deleted ? 'deleted-text' : 'detail-open-text'}">
                                                ${assignment.title}
                                            </strong>

                                            <c:if test="${not empty assignment.content}">
                                                <p class="assignment-desc">${assignment.content}</p>
                                            </c:if>

                                            <c:if test="${assignment.deleted}">
                                                <span class="deleted-info-text">
                                                    삭제 상태입니다. 7일 후 영구 삭제됩니다.
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="col status">
                                        <div class="assignment-status-box">
                                            <span class="status-badge
                                                ${assignment.status eq '진행중' ? 'primary' :
                                                  assignment.status eq '마감임박' ? 'warning' : 'gray'}">
                                                ${assignment.status}
                                            </span>
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
                                                <div class="feedback-chip">
                                                    피드백 필요: ${assignment.needFeedback}명
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="col manage">
                                        <c:choose>
                                            <c:when test="${assignment.deleted}">
                                                <button type="button" class="row-recover-btn">복구</button>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="row-menu-wrap">
                                                    <button type="button" class="icon-action-btn row-menu-toggle-btn" title="더보기">
                                                        <span class="more-mini-icon"></span>
                                                    </button>

                                                    <div class="row-menu-dropdown">
                                                        <button type="button" class="row-menu-item detail-open-btn">상세 보기</button>
                                                        <button type="button" class="row-menu-item status-toggle-btn">
                                                            ${assignment.status eq '마감' ? '진행중으로 변경' : '마감 처리'}
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
                            <div class="teacher-empty-box">
                                등록된 과제가 없습니다.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>

<div class="teacher-modal-backdrop" id="assignmentFormModal">
    <div class="teacher-modal teacher-assignment-form-modal">
        <div class="teacher-modal-header">
            <h3 id="assignmentFormModalTitle">새 과제 등록</h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="assignmentFormModal">×</button>
        </div>

        <form class="teacher-modal-form" id="assignmentForm">
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
                            <option value="대기중">대기중</option>
                            <option value="진행중">진행중</option>
                            <option value="마감임박">마감임박</option>
                            <option value="마감">마감</option>
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
                    <textarea id="assignmentContent" name="content" rows="5"
                              placeholder="학생들이 수행할 과제의 상세 내용을 입력해주세요."></textarea>
                </div>
            </div>

            <div class="teacher-modal-footer">
                <button type="button" class="teacher-modal-line-btn" data-close-modal="assignmentFormModal">취소</button>
                <button type="submit" class="teacher-primary-btn">등록 완료</button>
            </div>
        </form>
    </div>
</div>

<div class="teacher-modal-backdrop" id="assignmentDetailModal">
    <div class="teacher-modal teacher-assignment-detail-modal">
        <div class="teacher-modal-header">
            <div>
                <h3 id="assignmentDetailTitle">과학 실험 관찰 보고서</h3>
                <div class="detail-badge-row">
                    <span class="mini-badge subject" id="detailSubjectBadge">과학</span>
                    <span class="status-badge primary" id="detailStatusBadge">진행중</span>
                </div>
            </div>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="assignmentDetailModal">×</button>
        </div>

        <div class="teacher-modal-body scrollable">
            <div class="detail-top-grid">
                <div class="detail-content-box">
                    <span>과제 상세 내용</span>
                    <p id="detailAssignmentContent">강낭콩 관찰 결과를 보고서 양식에 맞추어 작성하고 사진을 첨부하세요.</p>
                </div>

                <div class="detail-side-stack">
                    <div class="detail-info-box">
                        <span>마감일</span>
                        <strong class="danger-text" id="detailAssignmentDeadline">오늘 18:00</strong>
                    </div>

                    <div class="detail-info-box warning" id="detailFeedbackNeedBox">
                        <span>피드백 대기</span>
                        <strong id="detailNeedFeedback">0명 피드백 필요</strong>
                    </div>
                </div>
            </div>

            <div class="detail-progress-box">
                <div class="detail-progress-title-row">
                    <h4>전체 제출률</h4>
                    <strong id="detailSubmissionRate">52%</strong>
                </div>

                <div class="big-progress-bar">
                    <div class="big-progress-fill" id="detailSubmissionBar" style="width:52%;"></div>
                </div>

                <div class="detail-progress-summary-grid">
                    <div class="progress-summary-card">
                        <span>제출완료</span>
                        <strong class="green-text" id="submittedCountText">0명</strong>
                    </div>
                    <div class="progress-summary-card">
                        <span>재제출</span>
                        <strong class="blue-text" id="resubmittedCountText">0명</strong>
                    </div>
                    <div class="progress-summary-card">
                        <span>늦은제출</span>
                        <strong class="orange-text" id="lateCountText">0명</strong>
                    </div>
                    <div class="progress-summary-card">
                        <span>미제출</span>
                        <strong class="red-text" id="notSubmittedCountText">0명</strong>
                    </div>
                </div>
            </div>

            <div class="student-submission-section">
                <div class="student-submission-top">
                    <h4>학생별 제출 현황 (<span id="submissionStudentCount">25</span>명)</h4>

                    <select id="submissionFilterSelect">
                        <option value="all">전체 보기</option>
                        <option value="제출완료">제출완료만</option>
                        <option value="미제출">미제출만</option>
                        <option value="재제출">재제출만</option>
                        <option value="늦은제출">늦은제출만</option>
                    </select>
                </div>

                <div class="student-submission-list" id="studentSubmissionList"></div>
            </div>
        </div>

        <div class="teacher-modal-footer between">
            <button type="button" class="teacher-modal-warning-btn" id="notifyUnsubmittedBtn">
                미제출 학생 알림 발송
            </button>

            <button type="button" class="teacher-modal-line-btn" data-close-modal="assignmentDetailModal">
                닫기
            </button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/teacher/teacher-assignment-manage.js"></script>