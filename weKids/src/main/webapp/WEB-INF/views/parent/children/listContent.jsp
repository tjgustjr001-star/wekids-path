<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-children.css">

<section class="parent-children-page">
    <div class="parent-children-header">
        <div>
            <h1>자녀 목록</h1>
            <p>등록된 자녀의 학습 현황을 한눈에 확인하세요.</p>
        </div>

        <button type="button" class="parent-add-child-btn" id="openLinkModalBtn">자녀 추가 연동</button>
    </div>

    <div class="parent-child-card-list">
        <c:forEach var="child" items="${childrenList}">
            <a href="${pageContext.request.contextPath}/parent/children/${child.id}" class="parent-child-card">
                <div class="parent-child-avatar">${child.initial}</div>

                <div class="parent-child-info">
                    <div class="parent-child-head">
                        <div class="parent-child-head-left">
                            <h3>${child.name} 학생</h3>
                            <c:if test="${child.pendingItems gt 0}">
                                <span class="parent-pending-badge">${child.pendingItems}건 확인 필요</span>
                            </c:if>
                        </div>
                        <span class="parent-chevron">›</span>
                    </div>

                    <p>${child.className} · ${child.number}번</p>

                    <div class="parent-child-progress-grid">
                        <div>
                            <div class="parent-progress-label-row">
                                <span>학습 진도</span>
                                <strong>${child.learningProgress}%</strong>
                            </div>
                            <div class="parent-progress-bar slim">
                                <div class="parent-progress-fill green" style="width:${child.learningProgress}%;"></div>
                            </div>
                        </div>

                        <div>
                            <div class="parent-progress-label-row">
                                <span>과제 제출</span>
                                <strong>${child.assignmentProgress}%</strong>
                            </div>
                            <div class="parent-progress-bar slim">
                                <div class="parent-progress-fill yellow" style="width:${child.assignmentProgress}%;"></div>
                            </div>
                        </div>

                        <div class="parent-last-access-box">
                            최근 접속: ${child.lastAccess}
                        </div>
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>

    <div class="parent-modal-overlay" id="linkChildModal">
        <div class="parent-link-modal">
            <div class="parent-link-modal-header">
                <h3>자녀 추가 연동</h3>
                <button type="button" class="parent-modal-close-btn" id="closeLinkModalBtn">×</button>
            </div>

            <div class="parent-link-modal-body">
                <div class="parent-link-step" id="linkInputStep">
                    <label>보호자 연결 코드</label>
                    <input type="text" id="linkCodeInput" class="parent-link-code-input" placeholder="ABCD1234" maxlength="8">
                    <p>자녀의 학생 계정 설정 &gt; 보호자 초대 코드에서 발급됩니다</p>

                    <button type="button" class="parent-primary-btn full" id="verifyLinkCodeBtn">코드 확인</button>

                    <div class="parent-link-guide-box">
                        <h4>연결 코드를 받는 방법</h4>
                        <ol>
                            <li>자녀가 학생 계정으로 로그인합니다.</li>
                            <li>설정 → 보호자 초대 코드로 이동합니다.</li>
                            <li>코드 생성하기 버튼을 눌러 발급합니다.</li>
                            <li>발급된 8자리 코드를 위에 입력합니다.</li>
                        </ol>
                    </div>
                </div>

                <div class="parent-link-step hidden" id="linkConfirmStep">
                    <div class="parent-link-preview-box">
                        <div class="parent-link-preview-avatar">김</div>
                        <div>
                            <strong id="linkedChildName">김민준 학생</strong>
                            <p id="linkedChildClassName">2026학년도 5학년 1반</p>
                        </div>
                    </div>

                    <p class="parent-link-confirm-text">위 학생을 자녀 목록에 추가하시겠습니까?</p>

                    <div class="parent-link-confirm-actions">
                        <button type="button" class="parent-secondary-btn" id="retryLinkBtn">다른 코드 입력</button>
                        <button type="button" class="parent-primary-btn" id="confirmLinkBtn">자녀 추가</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="parent-toast-container" id="parentToastContainer"></div>
</section>

<script src="${pageContext.request.contextPath}/resources/js/parent/parent-children.js"></script>