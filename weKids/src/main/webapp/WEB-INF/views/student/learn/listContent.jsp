<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-learn.css">

<section class="student-learning-page">
    <div class="learning-topbar">
        <h2 class="learning-page-title">나의 학습 목록</h2>

        <div class="learning-tab-group">
            <button type="button" class="learning-tab-btn active" data-tab="weekly">주간</button>
            <button type="button" class="learning-tab-btn" data-tab="monthly">월간</button>
        </div>
    </div>

    <div class="learning-card-list" id="learningCardList">
        <c:forEach var="learning" items="${learnList}">
            <div class="learning-card"
		     data-learning-id="${learning.id}"
		     data-title="${learning.title}"
		     data-type="${learning.type}"
		     data-required="${learning.required}"
		     data-status="${learning.status}"
		     data-deadline="${learning.deadline}"
		     data-duration="${learning.duration}"
		     data-progress="${learning.progress}"
		     data-text-content="${learning.textContent}"
		     data-link-url="${learning.linkUrl}"
		     data-last-position="${learning.lastPosition}"
		     data-description="${learning.description}"
			 data-guide-point="${learning.guidePoint}">
			<textarea class="learning-raw-content" hidden><c:out value="${learning.content}" /></textarea>
                <div class="learning-card-inner">
                    <div class="learning-card-left">
                        <div class="learning-type-icon
                            <c:choose>
                                <c:when test="${learning.status eq '완료'}">done</c:when>
                                <c:when test="${learning.status eq '진행중'}">progress</c:when>
                                <c:otherwise>pending</c:otherwise>
                            </c:choose>
                        ">
                            <span class="learning-icon-shape ${learning.type}"></span>
                        </div>

                        <div class="learning-info">
                            <div class="learning-title-row">
                                <c:choose>
                                    <c:when test="${learning.required}">
                                        <span class="learning-required-badge required">필수</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="learning-required-badge optional">선택</span>
                                    </c:otherwise>
                                </c:choose>
								
								<span class="learning-type-badge ${learning.type}">
						            <c:choose>
						                <c:when test="${learning.type eq 'video'}">영상</c:when>
						                <c:when test="${learning.type eq 'text'}">지문</c:when>
						                <c:when test="${learning.type eq 'link'}">링크</c:when>
						                <c:when test="${learning.type eq 'file'}">파일</c:when>
						                <c:otherwise>학습</c:otherwise>
						            </c:choose>
						        </span>
								
                                <h3 class="learning-title">${learning.title}</h3>
                            </div>

                            <div class="learning-meta-row">
                                <span class="learning-meta-item">
                                    <span class="meta-clock-icon"></span>
                                    ${learning.duration} 소요 예상
                                </span>
                                <span class="learning-meta-divider">|</span>
                                <span class="learning-meta-item">${learning.deadline} 마감</span>
                            </div>

                            <c:if test="${learning.status eq '진행중' and learning.progress gt 0}">
                                <div class="learning-inline-progress">
                                    <div class="learning-inline-progress-bar">
                                        <div class="learning-inline-progress-fill" style="width:${learning.progress}%;"></div>
                                    </div>
                                    <span class="learning-inline-progress-text">${learning.progress}%</span>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <div class="learning-card-right">
                        <span class="learning-status-badge
                            <c:choose>
                                <c:when test="${learning.status eq '완료'}">done</c:when>
                                <c:when test="${learning.status eq '진행중'}">progress</c:when>
                                <c:otherwise>pending</c:otherwise>
                            </c:choose>
                        ">
                            ${learning.status}
                        </span>

                        <div class="learning-action-wrap">
                            <c:choose>
                                <c:when test="${learning.status eq '진행중'}">
                                    <button type="button" class="learning-action-btn continue open-learning-btn">
                                        이어보기
                                    </button>
                                </c:when>
                                <c:when test="${learning.status eq '미시작'}">
                                    <button type="button" class="learning-action-btn start open-learning-btn">
                                        학습 시작
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="learning-action-btn done open-learning-btn">
                                        학습완료
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</section>

<div class="learning-modal-overlay" id="learningModalOverlay">
    <div class="learning-modal">
        <div class="learning-modal-header">
            <div class="learning-modal-header-left">
                <div class="learning-modal-badges">
                    <span class="learning-modal-type-badge" id="modalTypeBadge">영상 학습</span>
                    <span class="learning-modal-required-badge" id="modalRequiredBadge">필수</span>
                </div>
                <h2 class="learning-modal-title" id="modalLearningTitle">학습 제목</h2>
            </div>

            <button type="button" class="learning-modal-close-btn" id="learningModalCloseBtn">×</button>
        </div>

        <div class="learning-modal-body">
            <div class="learning-modal-main" id="learningModalMainContent">
                <div class="learning-content-placeholder" id="learningVideoContent">
                    <div class="fake-video-player">
                        <button type="button" class="fake-video-play-btn" id="fakeVideoPlayBtn">▶</button>

                        <div class="fake-video-controls">
                            <button type="button" class="fake-video-control-play">▶</button>
                            <div class="fake-video-progress" id="fakeVideoProgressBar">
                                <div class="fake-video-progress-fill" id="fakeVideoProgressFill"></div>
                            </div>
                            <span class="fake-video-time" id="fakeVideoTimeText">00:00 / 15분</span>
                        </div>
                    </div>
                </div>

                <div class="learning-text-panel" id="learningTextContent" style="display:none;">
				    <div class="learning-text-card">
				        <h3 id="learningTextTitle">텍스트 학습 제목</h3>
				     		
				        <p class="learning-text-description" id="learningTextDescription"></p>
						
				        <div class="learning-text-guide-point" id="learningTextGuidePoint" style="display:none;"></div>
						
				        <div class="learning-text-scroll-box" id="learningTextScrollBox">
				            <div class="learning-text-body" id="learningTextBody"></div>
				        </div>
				
				        <div class="learning-text-complete-wrap">
				            <button type="button" class="learning-text-complete-btn disabled" id="learningTextCompleteBtn" disabled>
				                다 읽었어요
				            </button>
				        </div>
				    </div>
				</div>

                <div class="learning-content-placeholder text-content" id="learningLinkContent" style="display:none;">
				    <h3 id="learningLinkTitle">참고 링크 학습</h3>
				    <p class="learning-link-description" id="learningLinkDescription"></p>
				    <div class="learning-link-box">
				        <a href="#" class="learning-link-anchor" id="learningLinkAnchor" target="_blank">참고 자료 열기</a>
				    </div>
				    <div class="learning-text-complete-wrap">
				        <button type="button" class="learning-text-complete-btn" id="learningLinkCompleteBtn">
				            링크를 확인했어요
				        </button>
				    </div>
				</div>
				
				<div class="learning-content-placeholder text-content" id="learningFileContent" style="display:none;">
				    <h3 id="learningFileTitle">파일 학습</h3>
				    <p class="learning-file-description" id="learningFileDescription"></p>
				    <div class="learning-link-box">
				        <a href="#" class="learning-link-anchor" id="learningFileAnchor" target="_blank">첨부파일 열기</a>
				    </div>
				    <div class="learning-text-complete-wrap">
				        <button type="button" class="learning-text-complete-btn" id="learningFileCompleteBtn">
				            파일을 확인했어요
				        </button>
				    </div>
				</div>


            </div>

            <div class="learning-modal-side">
                <div class="learning-side-section">
                    <h3>학습 진행률</h3>

                    <div class="learning-side-progress-head">
                        <span>현재 진도</span>
                        <strong id="learningCurrentProgressText">0%</strong>
                    </div>

                    <div class="learning-side-progress-bar">
                        <div class="learning-side-progress-fill" id="learningSideProgressFill"></div>
                    </div>
                </div>

                <div class="learning-side-content">
                    <div class="learning-difficulty-panel" id="difficultyIntroPanel">
                        <h4>학습이 어렵나요?</h4>
                        <p>내용이 너무 어렵거나 이해가 안 가는 부분이 있다면 선생님께 알려주세요.</p>
                        <button type="button" class="difficulty-open-btn" id="difficultyOpenBtn">학습 어려웠어요</button>
                    </div>

                    <div class="learning-difficulty-form" id="difficultyFormPanel">
                        <div class="difficulty-form-top">
                            <button type="button" class="difficulty-back-btn" id="difficultyBackBtn">×</button>
                            <h4>어떤 부분이 어려웠나요?</h4>
                        </div>

                        <div class="difficulty-reason-list">
                            <button type="button" class="difficulty-reason-btn" data-reason="개념이 잘 이해되지 않아요">개념이 잘 이해되지 않아요</button>
                            <button type="button" class="difficulty-reason-btn" data-reason="설명이 너무 빠르거나 어려워요">설명이 너무 빠르거나 어려워요</button>
                            <button type="button" class="difficulty-reason-btn" data-reason="예시나 추가 설명이 필요해요">예시나 추가 설명이 필요해요</button>
                            <button type="button" class="difficulty-reason-btn" data-reason="문제 풀이가 어려워요">문제 풀이가 어려워요</button>
                            <button type="button" class="difficulty-reason-btn" data-reason="기타 (선생님께 남길 말)">기타 (선생님께 남길 말)</button>
                        </div>

                        <textarea id="difficultyMessageInput" class="difficulty-message-input" placeholder="선생님께 남길 말을 입력해주세요."></textarea>

                        <button type="button" class="difficulty-submit-btn" id="difficultySubmitBtn">선생님께 전달하기</button>
                    </div>
                </div>

                <div class="learning-side-footer">
                    <button type="button" class="learning-save-btn" id="learningSaveBtn">진행 상태 저장 후 나가기</button>
                    <button type="button" class="learning-complete-btn" id="learningCompleteBtn">학습 완료하기</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="learning-toast-container" id="learningToastContainer"></div>

<script>
    window.appContextPath = '${pageContext.request.contextPath}';
    window.studentClassId = '${classId}';
</script>
<script src="${pageContext.request.contextPath}/resources/js/student/student-learn.js"></script>