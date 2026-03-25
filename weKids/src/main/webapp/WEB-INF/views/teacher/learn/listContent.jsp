<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-learn-manage.css">

<section class="teacher-learn-manage-page">
    <div class="teacher-page-top-row">
        <div class="teacher-page-title-box">
            <h1 id="learnPageTitle">학습 목록 관리</h1>
        </div>

        <div class="teacher-page-action-row">
            <div class="teacher-search-box">
                <span class="search-icon"></span>
                <input type="text" id="learnSearchInput" placeholder="학습 자료 검색" />
            </div>

            <button type="button" class="icon-line-btn" id="trashToggleBtn" title="휴지통">
                <span class="trash-mini-icon"></span>
            </button>

            <button type="button" class="teacher-primary-btn" id="openLearnCreateModalBtn">
                <span class="plus-mini-icon"></span>
                <span>학습 자료 등록</span>
            </button>
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

                                                <span class="mini-badge ${learn.status eq '운영중' ? 'primary' : 'gray'}">
                                                    ${learn.status}
                                                </span>

                                                <c:if test="${learn.difficultCount gt 0 and not learn.deleted}">
                                                    <button type="button" class="mini-badge warning difficult-open-btn">
                                                        어려웠어요 ${learn.difficultCount}명
                                                    </button>
                                                </c:if>
                                            </div>

                                            <strong class="learn-title ${learn.deleted ? 'deleted-text' : 'detail-open-text'}">
                                                ${learn.title}
                                            </strong>

                                            <c:if test="${not empty learn.linkUrl and not learn.deleted}">
                                                <a href="${learn.linkUrl}" target="_blank" class="learn-link-text">
                                                    ${learn.linkUrl}
                                                </a>
                                            </c:if>

                                            <c:if test="${not empty learn.content}">
                                                <p class="learn-desc">${learn.content}</p>
                                            </c:if>

                                            <c:if test="${learn.deleted}">
                                                <span class="deleted-info-text">
                                                    삭제 상태입니다. 7일 후 영구 삭제됩니다.
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="col schedule">
                                        <div class="learn-schedule-box">
                                            <span class="schedule-chip">
                                                학습: ${learn.startDate} ~ ${learn.endDate}
                                            </span>
                                            <span class="deadline-text">
                                                마감일: ${learn.deadline}
                                            </span>
                                            <span class="duration-text">
                                                예상 소요시간: ${learn.duration}분
                                            </span>
                                        </div>
                                    </div>

                                    <div class="col target">
                                        <span class="target-text">${learn.target}</span>
                                    </div>

                                    <div class="col manage">
                                        <c:choose>
                                            <c:when test="${learn.deleted}">
                                                <button type="button" class="row-recover-btn">복구</button>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="row-menu-wrap">
                                                    <button type="button" class="icon-action-btn row-menu-toggle-btn" title="더보기">
                                                        <span class="more-mini-icon"></span>
                                                    </button>

                                                    <div class="row-menu-dropdown">
                                                        <button type="button" class="row-menu-item status-toggle-btn">
                                                            ${learn.status eq '마감' ? '운영중으로 변경' : '마감 처리'}
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
                                등록된 학습 자료가 없습니다.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>

<div class="teacher-modal-backdrop" id="learnFormModal">
    <div class="teacher-modal teacher-learn-form-modal">
        <div class="teacher-modal-header">
            <h3 id="learnFormModalTitle">학습 자료 등록</h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="learnFormModal">×</button>
        </div>

        <form class="teacher-modal-form" id="learnForm">
            <div class="teacher-modal-body scrollable">
                <div class="form-field">
                    <label for="learnTitle">학습 자료 제목</label>
                    <input type="text" id="learnTitle" name="title" placeholder="예: 수학 3단원 개념 영상" required>
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
                        <label for="learnDuration">예상 소요시간</label>
                        <input type="number" id="learnDuration" name="duration" min="1" value="10">
                    </div>
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="learnStartDate">학습 시작일</label>
                        <input type="date" id="learnStartDate" name="startDate" required>
                    </div>

                    <div class="form-field">
                        <label for="learnDeadline">마감일</label>
                        <input type="datetime-local" id="learnDeadline" name="deadline" required>
                    </div>
                </div>

                <div class="form-field" id="learnLinkField">
                    <label for="learnLinkUrl">자료 링크</label>
                    <input type="url" id="learnLinkUrl" name="linkUrl" placeholder="https://">
                </div>

                <div class="form-field" id="learnTextField" style="display:none;">
                    <label for="learnTextContent">지도 포인트 (주요 내용)</label>
                    <textarea id="learnTextContent" name="textContent" rows="5"
                              placeholder="학생들이 주의 깊게 봐야 할 포인트나 지문 내용을 입력하세요."></textarea>
                </div>

                <div class="form-field" id="learnFileField" style="display:none;">
                    <label>자료 링크 (첨부 파일)</label>
                    <div class="file-drop-box">
                        클릭하여 파일을 업로드하거나 드래그 앤 드롭하세요
                    </div>
                </div>

                <div class="form-field">
                    <label for="learnContent">학습 상세 설명</label>
                    <textarea id="learnContent" name="content" rows="4"
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

<div class="teacher-modal-backdrop" id="learnDifficultModal">
    <div class="teacher-modal teacher-difficult-modal">
        <div class="teacher-modal-header">
            <h3>도움이 필요한 학생들</h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="learnDifficultModal">×</button>
        </div>

        <div class="teacher-modal-body">
            <p class="modal-sub-title">
                <strong>학습명:</strong>
                <span id="difficultLearnTitle">수학 3단원 개념 영상</span>
            </p>

            <div class="difficult-student-list" id="difficultStudentList">
                <div class="difficult-student-item">
                    <div class="difficult-student-left">
                        <div class="difficult-avatar">학</div>
                        <div>
                            <strong>김학생1</strong>
                            <p>"어려웠어요" 태그 남김</p>
                        </div>
                    </div>
                    <button type="button" class="mini-line-blue-btn">1:1 메시지</button>
                </div>
            </div>

            <div class="modal-bottom-btn-row">
                <button type="button" class="teacher-modal-line-btn">모두 확인 처리</button>
                <button type="button" class="teacher-modal-warning-btn">전체 메시지 발송</button>
            </div>
        </div>
    </div>
</div>

<div class="teacher-modal-backdrop" id="learnDetailModal">
    <div class="teacher-modal teacher-learn-detail-modal">
        <div class="teacher-modal-header">
            <div>
                <h3 id="learnDetailTitle">수학 3단원 개념 영상</h3>
                <div class="detail-badge-row">
                    <span class="mini-badge required" id="detailRequiredBadge">필수</span>
                    <span class="mini-badge outline" id="detailTypeBadge">영상</span>
                    <span class="mini-badge primary" id="detailStatusBadge">운영중</span>
                </div>
            </div>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="learnDetailModal">×</button>
        </div>

        <div class="teacher-modal-body scrollable">
            <div class="detail-info-grid">
                <div class="detail-info-box">
                    <span>학습 기간</span>
                    <strong id="detailPeriodText">2026-03-10 ~ 2026-03-12</strong>
                </div>
                <div class="detail-info-box">
                    <span>마감일시</span>
                    <strong class="danger-text" id="detailDeadlineText">2026-03-12 23:59</strong>
                </div>
                <div class="detail-info-box">
                    <span>예상 소요시간</span>
                    <strong id="detailDurationText">15분</strong>
                </div>
                <div class="detail-info-box">
                    <span>대상</span>
                    <strong id="detailTargetText">전체 (25명)</strong>
                </div>
            </div>

            <div class="detail-content-box" id="detailContentBox">
                <span>학습 상세 설명</span>
                <p id="detailContentText">학습 설명</p>
            </div>

            <div class="detail-link-box" id="detailLinkBox" style="display:none;">
                <span>학습 자료 링크</span>
                <a href="#" target="_blank" id="detailLinkUrl">https://example.com</a>
            </div>

            <div class="detail-progress-box">
                <div class="detail-progress-title-row">
                    <h4>전체 진행률</h4>
                    <strong id="detailCompletionRate">63%</strong>
                </div>

                <div class="big-progress-bar">
                    <div class="big-progress-fill" id="detailCompletionBar" style="width:63%;"></div>
                </div>

                <div class="detail-progress-summary-grid">
                    <div class="progress-summary-card">
                        <span>완료</span>
                        <strong class="green-text" id="completedCountText">3명</strong>
                    </div>
                    <div class="progress-summary-card">
                        <span>진행중</span>
                        <strong class="blue-text" id="inProgressCountText">3명</strong>
                    </div>
                    <div class="progress-summary-card">
                        <span>미시작</span>
                        <strong class="gray-text" id="notStartedCountText">2명</strong>
                    </div>
                </div>
            </div>

            <div class="student-progress-section">
                <h4>학생별 진행 상황 (<span id="studentProgressCount">8</span>명)</h4>
                <div class="student-progress-list" id="studentProgressList"></div>
            </div>
        </div>

        <div class="teacher-modal-footer">
            <button type="button" class="teacher-primary-btn" data-close-modal="learnDetailModal">닫기</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/teacher/teacher-learn-manage.js"></script>