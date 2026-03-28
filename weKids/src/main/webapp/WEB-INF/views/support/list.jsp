<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/resources/css/list.css" >
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>1:1 문의 내역 – We-Kids</title>
</head>
<body>


<!-- ════════ MAIN ════════ -->
<main class="main-wrap">

    <!-- 페이지 헤더 -->
    <div class="page-header">
        <a href="${pageContext.request.contextPath}/support/main" class="back-link">
            <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6"/></svg>
            이전으로 돌아가기
        </a>
        <div class="page-header-top">
            <div class="page-title-wrap">
                <svg viewBox="0 0 24 24">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                </svg>
                <h1 class="page-title">1:1 문의 내역</h1>
            </div>
            <div class="header-actions">
                <a href="${pageContext.request.contextPath}/support/faq" class="btn-faq">
                    <svg viewBox="0 0 24 24">
                        <circle cx="12" cy="12" r="10"/>
                        <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/>
                        <line x1="12" y1="17" x2="12.01" y2="17"/>
                    </svg>
                    FAQ 보기
                </a>
                <a href="${pageContext.request.contextPath}/support/submit" class="btn-new">
                    <svg viewBox="0 0 24 24">
                        <line x1="12" y1="5" x2="12" y2="19"/>
                        <line x1="5" y1="12" x2="19" y2="12"/>
                    </svg>
                    새 문의 작성
                </a>
            </div>
        </div>
        <p class="page-subtitle">문의하신 내용과 답변을 확인할 수 있습니다.</p>
    </div>

    <!-- 목록 카드 -->
    <div class="card-list">

        <!-- 필터 탭 + 검색 -->
        <div class="list-toolbar">
            <div class="tab-group">
                <button class="tab-btn active" data-filter="all">전체</button>
                <button class="tab-btn" data-filter="waiting">답변 대기</button>
                <button class="tab-btn" data-filter="done">답변 완료</button>
            </div>
            <div class="search-box">
                <svg viewBox="0 0 24 24">
                    <circle cx="11" cy="11" r="8"/>
                    <line x1="21" y1="21" x2="16.65" y2="16.65"/>
                </svg>
                <input type="text" class="search-input"
                       id="searchInput" placeholder="문의 내용 검색">
            </div>
        </div>

        <!-- 문의 목록 -->
        <div class="support-list" id="supportList">

            <c:choose>
                <c:when test="${empty supportList}">
                    <div class="empty-state">등록된 문의가 없습니다.</div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="item" items="${supportList}">
                        <div class="support-item"
                             data-status="${item.status eq '답변완료' ? 'done' : 'waiting'}"
                             data-title="${item.title}">
                            <div class="support-item-left">
                                <div class="item-meta">
                                    <span class="status-badge ${item.status eq '답변완료' ? 'done' : 'waiting'}">
                                        ${item.status}
                                    </span>
                                    <span class="item-category">${item.category}</span>
                                    <span class="item-divider">|</span>
                                    <span class="item-date">
                                        <fmt:formatDate value="${item.createdAt}" pattern="yyyy.MM.dd"/>
                                    </span>
                                </div>
                                <div class="item-title">${item.title}</div>
                            </div>
                            <a href="${pageContext.request.contextPath}/support/detail?supportNo=${item.supportNo}"
                               class="btn-detail">상세 보기</a>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

</main>
<style>
/* ================================================
   list.css – We-Kids 1:1 문의 내역 페이지 스타일
   ================================================ */

@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600;700&display=swap');

:root {
    --green-primary : #2e7d4f;
    --green-dark    : #1e5c38;
    --green-light   : #eef6f2;
    --blue-primary  : #3b6ef8;
    --blue-dark     : #2a5ce0;
    --bg            : #f2f5f7;
    --white         : #ffffff;
    --border        : #e4e8ec;
    --text-main     : #1a1a1a;
    --text-sub      : #555;
    --text-muted    : #aaa;
    --red           : #e53935;
    --radius-card   : 16px;
    --shadow-card   : 0 2px 16px rgba(0,0,0,.06);
}

* { margin: 0; padding: 0; box-sizing: border-box; }



/* ════════════════════════════════
   MAIN WRAP
════════════════════════════════ */
.main-wrap {
    margin-top: var(--topnav-height);
    flex: 1;
    padding: 44px 56px;
}
/* ════════════════════════════════
   PAGE HEADER
════════════════════════════════ */
.page-header { margin-bottom: 28px; }
.back-link {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    color: var(--text-sub);
    text-decoration: none;
    margin-bottom: 12px;
}
.back-link svg {
    width: 16px; height: 16px;
    stroke: currentColor; fill: none;
    stroke-width: 2; stroke-linecap: round; stroke-linejoin: round;
}
.back-link:hover { color: var(--green-primary); }

.page-header-top {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 16px;
    margin-bottom: 6px;
}
.page-title-wrap {
    display: flex;
    align-items: center;
    gap: 10px;
}
.page-title-wrap svg {
    width: 24px; height: 24px;
    stroke: var(--blue-primary); fill: none;
    stroke-width: 2; stroke-linecap: round; stroke-linejoin: round;
}
.page-title {
    font-size: 22px;
    font-weight: 700;
    color: #1a1a1a;
}
.page-subtitle {
    font-size: 14px;
    color: #888;
    margin-top: 4px;
}

/* 헤더 우측 버튼 그룹 */
.header-actions {
    display: flex;
    align-items: center;
    gap: 10px;
    flex-shrink: 0;
}
.btn-faq {
    display: flex;
    align-items: center;
    gap: 6px;
    background: var(--white);
    border: 1px solid var(--border);
    color: var(--text-sub);
    padding: 9px 18px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 500;
    font-family: inherit;
    cursor: pointer;
    text-decoration: none;
    transition: background .15s, border-color .15s;
}
.btn-faq svg {
    width: 16px; height: 16px;
    stroke: currentColor; fill: none;
    stroke-width: 1.8; stroke-linecap: round; stroke-linejoin: round;
}
.btn-faq:hover { background: #f5f6f8; border-color: #ccc; }

.btn-new {
    display: flex;
    align-items: center;
    gap: 6px;
    background: var(--blue-primary);
    color: var(--white);
    border: none;
    padding: 9px 18px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    font-family: inherit;
    cursor: pointer;
    text-decoration: none;
    transition: background .2s;
}
.btn-new svg {
    width: 16px; height: 16px;
    stroke: currentColor; fill: none;
    stroke-width: 2.5; stroke-linecap: round; stroke-linejoin: round;
}
.btn-new:hover { background: var(--blue-dark); }

/* ════════════════════════════════
   CARD
════════════════════════════════ */
.card-list {
    background: var(--white);
    border: 1px solid var(--border);
    border-radius: var(--radius-card);
    padding: 24px 28px;
    max-width: 860px;
    margin: 0 auto;          /* 추가 */
    box-shadow: var(--shadow-card);
}
/* ════════════════════════════════
   FILTER + SEARCH 바
════════════════════════════════ */
.list-toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    gap: 16px;
}
.tab-group {
    display: flex;
    gap: 6px;
}
.tab-btn {
    background: none;
    border: 1px solid transparent;
    padding: 7px 18px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 500;
    font-family: inherit;
    color: var(--text-sub);
    cursor: pointer;
    transition: background .15s, color .15s, border-color .15s;
}
.tab-btn:hover { background: #f0f1f3; }
.tab-btn.active {
    background: var(--text-main);
    color: var(--white);
    border-color: var(--text-main);
}

.search-box {
    position: relative;
    flex-shrink: 0;
}
.search-box svg {
    position: absolute;
    left: 12px; top: 50%;
    transform: translateY(-50%);
    width: 16px; height: 16px;
    stroke: var(--text-muted); fill: none;
    stroke-width: 1.8; stroke-linecap: round; stroke-linejoin: round;
    pointer-events: none;
}
.search-input {
    padding: 8px 14px 8px 36px;
    border: 1px solid #e0e4e8;
    border-radius: 8px;
    font-size: 13px;
    color: #222;
    font-family: inherit;
    outline: none;
    width: 200px;
    transition: border-color .2s, box-shadow .2s;
}
.search-input:focus {
    border-color: var(--green-primary);
    box-shadow: 0 0 0 3px rgba(46,125,79,.08);
}

/* ════════════════════════════════
   문의 목록 아이템
════════════════════════════════ */
.support-list { display: flex; flex-direction: column; }

.support-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 18px 0;
    border-bottom: 1px solid #f0f2f4;
    gap: 16px;
}
.support-item:last-child { border-bottom: none; }

.support-item-left { flex: 1; min-width: 0; }

.item-meta {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 6px;
}

/* 상태 배지 */
.status-badge {
    display: inline-flex;
    align-items: center;
    padding: 3px 10px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
}
.status-badge.waiting {
    background: #fff4e5;
    color: #e07b00;
}
.status-badge.done {
    background: #eef6f2;
    color: var(--green-primary);
}

.item-category {
    font-size: 13px;
    color: var(--text-muted);
}
.item-date {
    font-size: 13px;
    color: var(--text-muted);
}
.item-divider {
    font-size: 12px;
    color: #ddd;
}

.item-title {
    font-size: 15px;
    font-weight: 500;
    color: var(--text-main);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

/* 상세 보기 버튼 */
.btn-detail {
    flex-shrink: 0;
    background: var(--white);
    border: 1px solid var(--border);
    color: var(--text-sub);
    padding: 7px 18px;
    border-radius: 8px;
    font-size: 13px;
    font-weight: 500;
    font-family: inherit;
    cursor: pointer;
    text-decoration: none;
    transition: background .15s, border-color .15s;
    white-space: nowrap;
}
.btn-detail:hover { background: #f5f6f8; border-color: #ccc; }

/* 빈 목록 */
.empty-state {
    text-align: center;
    padding: 60px 20px;
    color: var(--text-muted);
    font-size: 14px;
}

</style>

<script>
    /* ── 탭 필터 ── */
    var tabBtns     = document.querySelectorAll('.tab-btn');
    var items       = document.querySelectorAll('.support-item');
    var currentFilter = 'all';
    var currentKeyword = '';

    tabBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            tabBtns.forEach(function (b) { b.classList.remove('active'); });
            btn.classList.add('active');
            currentFilter = btn.getAttribute('data-filter');
            applyFilter();
        });
    });

    /* ── 검색 ── */
    document.getElementById('searchInput').addEventListener('input', function () {
        currentKeyword = this.value.trim().toLowerCase();
        applyFilter();
    });

    function applyFilter() {
        items.forEach(function (item) {
            var status  = item.getAttribute('data-status');
            var title   = item.getAttribute('data-title').toLowerCase();
            var matchTab    = currentFilter === 'all' || status === currentFilter;
            var matchSearch = title.indexOf(currentKeyword) !== -1;
            item.style.display = (matchTab && matchSearch) ? '' : 'none';
        });
    }
</script>

</body>
</html>
