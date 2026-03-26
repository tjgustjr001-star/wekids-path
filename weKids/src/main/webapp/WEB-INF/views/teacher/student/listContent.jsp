<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-student-manage.css">

<section class="teacher-student-manage-page">
    <div class="teacher-page-top-row">
        <div class="teacher-page-title-box">
            <h1>학생 관리</h1>
            <div class="teacher-page-subinfo">
                <span>총 ${empty totalStudentCount ? 25 : totalStudentCount}명</span>
                <span class="divider">|</span>
                <span class="highlight">
                    학부모 연결 ${empty linkedParentCount ? 20 : linkedParentCount}명
                    (${empty linkedParentRate ? 80 : linkedParentRate}%)
                </span>
            </div>
        </div>

        <div class="teacher-page-action-row">
            <div class="teacher-search-box">
                <span class="search-icon"></span>
                <input type="text" id="studentSearchInput" placeholder="학생명 검색" />
            </div>

            <button type="button" class="teacher-primary-btn" id="openInviteModalBtn">
                <span class="plus-mini-icon"></span>
                <span>학생 초대</span>
            </button>
        </div>
    </div>

    <div class="teacher-student-table-card">
        <div class="teacher-table-scroll">
            <div class="teacher-student-table">
                <div class="teacher-student-table-head">
                    <div class="col no">번호</div>
                    <div class="col info">학생 정보</div>
                    <div class="col phone">연락처</div>
                    <div class="col login">최근 접속</div>
                    <div class="col progress">학습 진행률</div>
                    <div class="col manage">관리</div>
                </div>

                <div class="teacher-student-table-body" id="studentTableBody">
                    <c:choose>
                        <c:when test="${not empty studentList}">
                            <c:forEach var="student" items="${studentList}">
							    <div class="teacher-student-row"
								     data-student-class-id="${student.studentClassId}"
								     data-student-name="${student.studentName}"
								     data-student-id="${student.studentId}"
								     data-number="${student.studentNo}"
								     data-parent-linked="${student.parentLinked}"
								     data-phone="${empty student.phone ? '-' : student.phone}"
								     data-last-login="${student.lastLoginAtText}"
								     data-learning-progress="${student.learningProgressRate}"
								     data-recent-assignments="${student.submittedAssignmentCount}/${student.totalAssignmentCount} 제출"
								     data-feedback-sum="${empty student.intro ? '등록된 학생 소개가 없습니다.' : student.intro}"
								     data-tags="${student.tagsCsv}"
								     data-memo="${student.observationMemo}">
							        <div class="col no">
							            <span class="student-number">${student.studentNo}</span>
							        </div>
							
							        <div class="col info">
							            <div class="student-info-wrap">
							                <div class="student-avatar">
							                    ${student.nameFirst}
							                </div>
							
							                <div class="student-meta">
							                    <strong class="student-name">${student.studentName}</strong>
							
							                    <div class="student-parent-link">
							                        <span class="mini-user-icon"></span>
							
							                        <c:choose>
							                            <c:when test="${student.parentLinked eq 1}">
							                                <span class="linked-badge">학부모 연결됨</span>
							                            </c:when>
							                            <c:otherwise>
							                                <span class="unlinked-badge">미연결</span>
							                            </c:otherwise>
							                        </c:choose>
							                    </div>
							                </div>
							            </div>
							        </div>
							
							        <div class="col phone">
							            <span class="student-phone">${empty student.phone ? '-' : student.phone}</span>
							        </div>
							
							        <div class="col login">
							            <span class="student-last-login">${student.lastLoginAtText}</span>
							        </div>
							
							        <div class="col progress">
							            <div class="progress-cell">
							                <div class="progress-text-row">
							                    <span>전체 달성률</span>
							                    <strong class="${student.learningProgressRate lt 60 ? 'danger-text' : 'normal-text'}">
							                        ${student.learningProgressRate}%
							                    </strong>
							                </div>
							
							                <div class="progress-bar">
							                    <div class="progress-fill ${student.learningProgressRate lt 60 ? 'danger' : ''}"
							                         style="width:${student.learningProgressRate}%;"></div>
							                </div>
							            </div>
							        </div>
							
							        <div class="col manage">
							            <div class="student-manage-wrap">
							                <button type="button" class="icon-action-btn" title="메시지 전송">
							                    <span class="message-mini-icon"></span>
							                </button>
							
							                <div class="row-menu-wrap">
							                    <button type="button" class="icon-action-btn row-menu-toggle-btn" title="더보기">
							                        <span class="more-mini-icon"></span>
							                    </button>
							
							                    <div class="row-menu-dropdown">
							                        <button type="button" class="row-menu-item detail-open-btn">상세 정보</button>
							                        <button type="button" class="row-menu-item">학부모 연락</button>
							                        <button type="button" class="row-menu-item danger remove-student-btn">클래스 제외</button>
							                    </div>
							                </div>
							            </div>
							        </div>
							    </div>
							</c:forEach>
                        </c:when>

                        <c:otherwise>
                            <div class="teacher-empty-box">
                                등록된 학생이 없습니다.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>

<div class="teacher-modal-backdrop" id="inviteModal">
    <div class="teacher-modal teacher-invite-modal">
        <div class="teacher-modal-header">
            <h3>
                <span class="modal-user-plus-icon"></span>
                학생 초대하기
            </h3>
            <button type="button" class="teacher-modal-close-btn" data-close-modal="inviteModal">×</button>
        </div>

        <div class="teacher-modal-body">
            <div class="invite-block">
                <label>클래스 초대 코드</label>

                <div class="invite-code-wrap">
                    <div class="invite-code-text" id="inviteCodeText">
                        ${empty inviteCode ? 'WKD-739X-82A' : inviteCode}
                    </div>

                    <button type="button" class="copy-btn" id="copyInviteCodeBtn">복사</button>
                </div>

                <p>학생들이 로그인 후 위 코드를 입력하면 이 클래스에 바로 참여할 수 있습니다.</p>
            </div>

            <div class="invite-divider">
                <span>또는</span>
            </div>

            <div class="invite-block">
                <label>초대 링크 공유</label>

                <div class="invite-link-wrap">
                    <input type="text" readonly id="inviteLinkText"
                           value="${empty inviteLink ? 'https://wekids.com/join/WKD739X82A' : inviteLink}" />
                    <button type="button" class="line-copy-btn" id="copyInviteLinkBtn">복사</button>
                </div>
            </div>
        </div>

        <div class="teacher-modal-footer">
            <button type="button" class="teacher-modal-line-btn" data-close-modal="inviteModal">닫기</button>
        </div>
    </div>
</div>

<div class="teacher-modal-backdrop" id="studentDetailModal">
    <div class="teacher-modal teacher-student-detail-modal">
        <div class="teacher-modal-header large">
            <div class="student-detail-header-left">
                <div class="student-detail-avatar" id="detailAvatar">김</div>

                <div class="student-detail-head-info">
                    <h3>
                        <span id="detailName">김지훈</span> 학생
                        <span class="student-no-badge" id="detailNumber">1번</span>
                    </h3>

                    <div class="student-detail-subinfo">
                        <span>
                            학부모 연동:
                            <strong id="detailParentLinkedText">연동 완료</strong>
                        </span>
                        <span class="divider">|</span>
                        <span id="detailPhone">010-1234-5678</span>
                    </div>
                </div>
            </div>

            <button type="button" class="teacher-modal-close-btn" data-close-modal="studentDetailModal">×</button>
        </div>

        <div class="teacher-student-detail-body">
            <div class="detail-left-panel">
                <section class="detail-box soft">
                    <h4>
                        <span class="detail-file-icon"></span>
                        최근 학습 요약 (이번 주)
                    </h4>

                    <div class="detail-summary-grid">
                        <div class="detail-summary-card">
                            <span>학습 진행률</span>
                            <strong id="detailProgress">95%</strong>
                        </div>

                        <div class="detail-summary-card">
                            <span>과제 제출</span>
                            <strong class="blue-text" id="detailAssignments">3/3 제출</strong>
                        </div>

                        <div class="detail-summary-card">
                            <span>최근 접속</span>
                            <strong class="small-text" id="detailLastLogin">오늘 08:30</strong>
                        </div>
                    </div>

                    <div class="detail-feedback-box">
                        <span>선생님 피드백 요약</span>
                        <p id="detailFeedbackSummary">수학 개념 이해가 빠르며, 과제 제출이 매우 성실함.</p>
                    </div>
                </section>

                <section class="detail-warning-section">
                    <h4>
                        <span class="detail-check-icon"></span>
                        미제출/주의 과제
                    </h4>

                    <div class="detail-warning-box" id="detailWarningBox">
                        <div class="warning-icon"></div>
                        <div>
                            <strong>수학 3단원 문제풀이</strong>
                            <p>마감일이 지났습니다. 학부모 안내가 필요할 수 있습니다.</p>
                        </div>
                    </div>
                </section>
            </div>

            <div class="detail-right-panel">
                <section class="detail-side-box">
                    <h4>
                        <span class="detail-tag-icon"></span>
                        관찰 태그
                    </h4>
                    <p class="detail-side-desc">학생의 특징이나 현재 상태를 태그로 남겨두세요.</p>

                    <div class="tag-button-group" id="tagButtonGroup">
                        <button type="button" class="tag-btn" data-tag="우수">우수</button>
                        <button type="button" class="tag-btn" data-tag="주의">주의</button>
                        <button type="button" class="tag-btn" data-tag="미제출 잦음">미제출 잦음</button>
                        <button type="button" class="tag-btn" data-tag="어려움 겪음">어려움 겪음</button>
                        <button type="button" class="tag-btn" data-tag="태도 좋음">태도 좋음</button>
                        <button type="button" class="tag-btn" data-tag="질문 많음">질문 많음</button>
                    </div>
                </section>

                <section class="detail-side-box yellow">
                    <h4>
                        <span class="detail-note-icon"></span>
                        지도 메모
                        <span class="teacher-only-badge">교사 전용</span>
                    </h4>

                    <textarea id="detailMemoText"
                              placeholder="학생 지도에 필요한 메모를 자유롭게 작성하세요. (학생이나 학부모에게는 노출되지 않습니다)"></textarea>

                    <div class="detail-side-action-row">
                        <button type="button" class="teacher-primary-btn small" id="saveMemoBtn">
                            메모 및 태그 저장
                        </button>
                    </div>
                </section>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/teacher/teacher-student-manage.js"></script>
