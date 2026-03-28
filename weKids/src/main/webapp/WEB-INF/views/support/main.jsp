<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    support/main.jsp  —  콘텐츠 조각
    직접 호출 X.
    studentLayout / teacherLayout / parentLayout 의
    <jsp:include page="${contentPage}"/> 로 삽입됨.
--%>


<a href="${pageContext.request.contextPath}/settings" class="back-link">
    <svg viewBox="0 0 24 24" width="16" height="16" style="vertical-align:middle;">
        <polyline points="15 18 9 12 15 6"/>
    </svg>
    설정으로 돌아가기
</a>

<div class="card card-support">
    <h1 class="card-title">1:1 문의</h1>
    <p class="card-subtitle">궁금한 점이나 불편한 점을 해결해 드립니다. 원하시는 메뉴를 선택해 주세요.</p>

    <div class="menu-grid">
        <a href="${pageContext.request.contextPath}/support/submit" class="menu-option">
            <div class="option-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M12 20h9"/>
                    <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/>
                </svg>
            </div>
            <div class="option-text">
                <div class="title">새 문의 작성하기</div>
                <div class="desc">불편한 점이나 궁금한 사항을 남...</div>
            </div>
            <div class="option-arrow">
                <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="9 18 15 12 9 6"/>
                </svg>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/support/list" class="menu-option">
            <div class="option-icon">
                <svg viewBox="0 0 24 24" width="24" height="24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                </svg>
            </div>
            <div class="option-text">
                <div class="title">내 문의 확인하기</div>
                <div class="desc">지금까지 남겨주신 문의와 답변을...</div>
            </div>
            <div class="option-arrow">
                <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="9 18 15 12 9 6"/>
                </svg>
            </div>
        </a>
    </div>

    <div class="info-box">
        <div class="info-box-title">고객센터 운영 안내</div>
        <div class="info-row">
            <span class="info-label">운영 시간</span>
            <span class="info-value">평일 09:00 - 18:00</span>
        </div>
        <div class="info-row">
            <span class="info-label">이메일</span>
            <span class="info-value">support@we-kids.com</span>
        </div>
        <div class="info-row">
            <span class="info-label">평균 답변 시간</span>
            <span class="info-value">24시간 이내</span>
        </div>
    </div>
</div>
<style>

 :root {
    --green-primary  : #2e7d4f;
    --green-dark     : #1e5c38;
    --green-light    : #eef6f2;
    --green-border   : #c8e6c9;
    --bg             : #f2f5f7;
    --white          : #ffffff;
    --border         : #e4e8ec;
    --text-main      : #1a1a1a;
    --text-sub       : #555;
    --text-muted     : #aaa;
    --red            : #e53935;
    --radius-card    : 16px;
    --radius-btn     : 10px;
    --shadow-card    : 0 2px 16px rgba(0, 0, 0, .06);
}



/* ════════════════════════════════
   MAIN WRAP
════════════════════════════════ */
.main-wrap {
    margin-left: var(--sidebar-width);
    margin-top: var(--topnav-height);
    flex: 1;
    padding: 44px 56px;
}

.back-link {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    font-size: 14px;
    color: #666;
    text-decoration: none;
    margin-bottom: 28px;
    transition: color .15s;
}
.back-link:hover { color: var(--green-primary); }
.back-link svg {
    width: 16px; height: 16px;
    stroke: currentColor;
    fill: none;
    stroke-width: 2;
    stroke-linecap: round;
    stroke-linejoin: round;
}

/* ════════════════════════════════
   CARD
════════════════════════════════ */
.card {
    background: var(--white);
    border: 1px solid var(--border);
    border-radius: var(--radius-card);
    box-shadow: var(--shadow-card);
}

.card-support {
    padding: 38px 44px;
    max-width: 680px;
    margin: 0 auto;
}

.card-title {
    font-size: 22px;
    font-weight: 700;
    color: var(--text-main);
    margin-bottom: 8px;
}
.card-subtitle {
    font-size: 14px;
    color: var(--text-muted);
    margin-bottom: 32px;
    line-height: 1.6;
}
/* ════════════════════════════════
   MENU GRID
════════════════════════════════ */
.menu-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 14px;
    margin-bottom: 28px;
}

.menu-option {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 20px 18px;
    border: 1.5px solid var(--border);
    border-radius: 12px;
    text-decoration: none;
    color: inherit;
    background: var(--white);
    transition: border-color .2s, box-shadow .2s, background .2s;
}
.menu-option:hover {
    border-color: var(--green-primary);
    background: #fbfefb;
    box-shadow: 0 4px 16px rgba(46,125,79,.1);
}

.option-icon {
    width: 42px; height: 42px;
    border-radius: 10px;
    background: var(--green-light);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}
.option-icon svg {
    width: 20px; height: 20px;
    stroke: var(--green-primary);
    fill: none;
    stroke-width: 1.8;
    stroke-linecap: round;
    stroke-linejoin: round;
}

.option-text { flex: 1; min-width: 0; }
.option-text .title {
    font-size: 14px;
    font-weight: 600;
    color: var(--text-main);
    margin-bottom: 3px;
}
.option-text .desc {
    font-size: 12px;
    color: var(--text-muted);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.option-arrow { color: #ccc; flex-shrink: 0; }
.option-arrow svg {
    width: 15px; height: 15px;
    stroke: currentColor;
    fill: none;
    stroke-width: 2;
    stroke-linecap: round;
    stroke-linejoin: round;
}

/* ════════════════════════════════
   INFO BOX
════════════════════════════════ */
.info-box {
    background: #f9fafb;
    border: 1px solid var(--border);
    border-radius: 12px;
    padding: 24px 28px;
}
.info-box-title {
    font-size: 13px;
    font-weight: 700;
    color: #333;
    margin-bottom: 16px;
}
.info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 7px 0;
}
.info-row + .info-row { border-top: 1px solid #f0f2f4; }
.info-label { font-size: 13px; color: var(--text-muted); }
.info-value  { font-size: 13px; color: #444; font-weight: 500; }


</style>
