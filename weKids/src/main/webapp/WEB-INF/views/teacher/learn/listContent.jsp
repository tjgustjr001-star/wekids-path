<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-learn-manage.css">

<section class="teacher-learn-manage-page">
    <div class="teacher-page-top-row">
        <div class="teacher-page-title-box">
            <h1 id="learnPageTitle">
                <c:choose>
                    <c:when test="${trashMode}">휴지통 (학습 관리)</c:when>
                    <c:otherwise>학습 목록 관리</c:otherwise>
                </c:choose>
            </h1>

            <c:if test="${trashMode}">
                <p class="trash-guide-text">삭제된 학습 자료를 복구하거나 영구삭제할 수 있습니다.</p>
            </c:if>
        </div>

        <div class="teacher-page-action-row">
            <div class="teacher-search-box">
                <span class="search-icon"></span>
                <input type="text" id="learnSearchInput" placeholder="학습 자료 검색" />
            </div>

            <c:choose>
                <c:when test="${trashMode}">
                    <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/learns"
                       class="icon-line-btn active"
                       id="trashToggleBtn"
                       title="학습 목록으로">
                        <span class="trash-mini-icon"></span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/learns/trash"
                       class="icon-line-btn"
                       id="trashToggleBtn"
                       title="휴지통">
                        <span class="trash-mini-icon"></span>
                    </a>
                </c:otherwise>
            </c:choose>

            <c:if test="${not trashMode}">
                <button type="button" class="teacher-primary-btn" id="openLearnCreateModalBtn">
                    <span class="plus-mini-icon"></span>
                    <span>학습 자료 등록</span>
                </button>
            </c:if>
        </div>
    </div>

    <div class="teacher-learn-table-card">
        <div class="teacher-table-scroll">
            <div class="teacher-learn-table">
                <div class="teacher-learn-table-head">
                    <div class="col info">학습 자료 정보</div>
                    <div class="col schedule">기한 / 예상 소요시간</div>
                    <div class="col target">대상 / 참여</div>
                    <div class="col manage">관리</div>
                </div>

                <div class="teacher-learn-table-body" id="learnTableBody">
                    <c:choose>
                        <c:when test="${not empty learnList}">
                            <c:forEach var="learn" items="${learnList}">
                                <div class="teacher-learn-row"
                                     data-title="${learn.title}"
                                     data-type="${learn.type}"
                                     data-required="${learn.required}"
                                     data-status="${learn.status}"
                                     data-start-date="${learn.startDate}"
                                     data-end-date="${learn.endDate}"
                                     data-deadline="${learn.deadline}"
                                     data-duration="${learn.duration}"
                                     data-link-url="${learn.linkUrl}"
                                     data-content="${learn.content}"
                                     data-text-content="${learn.textContent}"
                                     data-target="${learn.target}"
                                     data-difficult-count="${learn.difficultCount}"
                                     data-deleted="${learn.deleted}"
                                     data-learning-id="${learn.id}">

                                    <div class="col info">
                                        <div class="learn-info-wrap">
                                            <div class="learn-badge-row">
                                                <c:if test="${learn.deleted}">
                                                    <span class="mini-badge deleted">삭제됨</span>
                                                </c:if>

                                                <span class="mini-badge ${learn.required ? 'required' : 'optional'}">
                                                    ${learn.required ? '필수' : '선택'}
                                                </span>

                                                <span class="mini-badge outline">${learn.type}</span>

                                                <span class="mini-badge
                                                    ${learn.status eq '운영중' ? 'primary' :
                                                      learn.status eq '대기중' ? 'gray' : 'gray'}">
                                                    ${learn.status}
                                                </span>

                                                <c:if test="${learn.difficultCount > 0}">
                                                    <button type="button" class="mini-badge warning difficult-open-btn">
                                                        어려웠어요 ${learn.difficultCount}명
                                                    </button>
                                                </c:if>
                                            </div>

                                            <strong class="learn-title detail-open-text ${learn.deleted ? 'deleted-text' : ''}">
                                                ${learn.title}
                                            </strong>

                                            <c:if test="${not empty learn.linkUrl}">
                                                <a href="${learn.linkUrl}" target="_blank" class="learn-link-text">
                                                    ${learn.linkUrl}
                                                </a>
                                            </c:if>

                                            <p class="learn-desc ${learn.deleted ? 'deleted-text' : ''}">
                                                ${learn.content}
                                            </p>

                                            <c:if test="${learn.deleted and not empty learn.deletedAt}">
                                                <p class="deleted-info-text">
                                                    삭제 일시: ${learn.deletedAt}
                                                </p>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="col schedule">
                                        <div class="learn-schedule-box">
                                            <div class="schedule-chip">
                                                학습: ${learn.startDate} ~ ${learn.endDate}
                                            </div>
                                            <div class="deadline-text">마감일: ${learn.deadline}</div>
                                            <div class="duration-text">예상 소요시간: ${learn.duration}분</div>
                                        </div>
                                    </div>

                                    <div class="col target">
                                        <div class="target-text">${learn.target}</div>
                                    </div>

                                    <div class="col manage">
                                        <c:choose>
                                            <c:when test="${trashMode}">
                                                <div class="trash-action-row">
                                                    <button type="button" class="row-recover-btn">복구</button>
                                                    <button type="button" class="row-remove-btn">영구삭제</button>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="row-menu-wrap">
                                                    <button type="button" class="icon-action-btn row-menu-toggle-btn">
                                                        <span class="more-mini-icon"></span>
                                                    </button>

                                                    <div class="row-menu-dropdown">
                                                        <button type="button" class="row-menu-item edit-open-btn">수정</button>
                                                        <button type="button" class="row-menu-item detail-open-btn">상세</button>
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
                                <c:choose>
                                    <c:when test="${trashMode}">
                                        휴지통에 학습 자료가 없습니다.
                                    </c:when>
                                    <c:otherwise>
                                        등록된 학습 자료가 없습니다.
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>


<!-- 등록/수정 모달 -->
<div class="teacher-modal-backdrop" id="learnFormModal">
    <div class="teacher-modal teacher-learn-form-modal">
        <form class="teacher-modal-form" id="learnForm" method="post">
            <input type="hidden" id="learnId" name="learnId" />
            <input type="hidden" id="classId" value="${classId}" />

            <div class="teacher-modal-header">
                <h3 id="learnFormModalTitle">학습 자료 등록</h3>
                <button type="button" class="teacher-modal-close-btn" data-close-modal="learnFormModal">×</button>
            </div>

            <div class="teacher-modal-body scrollable learn-form-scroll-area">
                <div class="form-field">
                    <label for="learnTitle">제목</label>
                    <input type="text"
                           id="learnTitle"
                           name="title"
                           placeholder="예: 수학 3단원 개념 영상"
                           required />
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="learnType">유형</label>
                        <select id="learnType" name="type">
                            <option value="영상">영상</option>
                            <option value="지문읽기">지문읽기</option>
                            <option value="링크">링크</option>
                            <option value="파일">파일</option>
                        </select>
                    </div>

                    <div class="form-field">
                        <label for="learnRequired">필수 여부</label>
                        <select id="learnRequired" name="required">
                            <option value="true">필수</option>
                            <option value="false">선택</option>
                        </select>
                    </div>
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="learnStatus">상태</label>
                        <select id="learnStatus" name="status">
                            <option value="대기중">대기중</option>
                            <option value="운영중">운영중</option>
                            <option value="마감">마감</option>
                        </select>
                    </div>

                    <div class="form-field">
                        <label for="learnDuration">예상 소요시간(분)</label>
                        <input type="number"
                               id="learnDuration"
                               name="duration"
                               min="1"
                               placeholder="10" />
                    </div>
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="learnStartDate">시작일</label>
                        <input type="date" id="learnStartDate" name="startDate" required />
                    </div>

                    <div class="form-field">
                        <label for="learnDeadline">마감일</label>
                        <input type="datetime-local" id="learnDeadline" name="deadline" required />
                    </div>
                </div>

                <div class="form-field" id="learnLinkField">
                    <label for="learnLinkUrl">링크 주소</label>
                    <input type="text"
                           id="learnLinkUrl"
                           name="linkUrl"
                           placeholder="https://" />
                </div>

                <div class="form-field" id="learnTextField">
                    <label for="learnTextContent">지문 내용</label>
                    <textarea id="learnTextContent"
                              name="textContent"
                              rows="6"
                              placeholder="지문 내용을 입력해주세요."></textarea>
                </div>

                <div class="form-field" id="learnFileField">
                    <label>파일 업로드</label>
                    <div class="file-drop-box">파일 업로드는 2차 구현 대상입니다.</div>
                </div>

                <div class="form-field">
                    <label for="learnContent">설명</label>
                    <textarea id="learnContent"
                              name="content"
                              rows="6"
                              placeholder="학습에 대한 상세한 설명을 입력해주세요."></textarea>
                </div>
            </div>

            <div class="teacher-modal-footer">
                <button type="button" class="teacher-modal-line-btn" data-close-modal="learnFormModal">취소</button>
                <button type="submit" class="teacher-primary-btn">등록 완료</button>
            </div>
        </form>
    </div>
</div>

<!-- 어려웠어요 모달 -->
<div class="teacher-modal-backdrop" id="learnDifficultModal">
    <div class="teacher-modal teacher-difficult-modal">
        <div class="teacher-modal-header">
            <h3>어려웠어요 학생 목록</h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="learnDifficultModal">×</button>
        </div>
        <div class="teacher-modal-body">
            <p class="modal-sub-title">
                <strong id="difficultLearnTitle">학습 자료명</strong>에 대해 어려움을 표시한 학생 목록입니다.
            </p>
            <div class="difficult-student-list" id="difficultStudentList">
                <div class="teacher-empty-box">어려움 학생 데이터는 2차 연동 대상입니다.</div>
            </div>
        </div>
        <div class="teacher-modal-footer">
            <button type="button" class="teacher-primary-btn" data-close-modal="learnDifficultModal">닫기</button>
        </div>
    </div>
</div>

<!-- 상세 모달 -->
<div class="teacher-modal-backdrop" id="learnDetailModal">
    <div class="teacher-modal teacher-learn-detail-modal">
        <div class="teacher-modal-header">
            <h3 id="learnDetailTitle">학습 상세</h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="learnDetailModal">×</button>
        </div>

        <div class="teacher-modal-body scrollable">
            <div class="detail-badge-row">
                <span class="mini-badge required" id="detailRequiredBadge">필수</span>
                <span class="mini-badge outline" id="detailTypeBadge">영상</span>
                <span class="mini-badge primary" id="detailStatusBadge">운영중</span>
            </div>

            <div class="detail-info-grid">
                <div class="detail-info-box">
                    <span>학습 기간</span>
                    <strong id="detailPeriodText"></strong>
                </div>

                <div class="detail-info-box">
                    <span>마감일</span>
                    <strong id="detailDeadlineText"></strong>
                </div>

                <div class="detail-info-box">
                    <span>예상 소요시간</span>
                    <strong id="detailDurationText"></strong>
                </div>

                <div class="detail-info-box">
                    <span>대상</span>
                    <strong id="detailTargetText"></strong>
                </div>
            </div>

            <div class="detail-link-box" id="detailLinkBox">
                <span>링크</span>
                <a href="#" target="_blank" id="detailLinkUrl"></a>
            </div>

            <div class="detail-content-box">
                <span>설명</span>
                <p id="detailContentText"></p>
            </div>
        </div>

        <div class="teacher-modal-footer">
            <button type="button" class="teacher-primary-btn" data-close-modal="learnDetailModal">닫기</button>
        </div>
    </div>
</div>

<script>
	window.appContextPath = '${pageContext.request.contextPath}';
    window.learnTrashMode = ${trashMode ? 'true' : 'false'};
</script>
<script src="${pageContext.request.contextPath}/resources/js/teacher/teacher-learn-manage.js"></script>