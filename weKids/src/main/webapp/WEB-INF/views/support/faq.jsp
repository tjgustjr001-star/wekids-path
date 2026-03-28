<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>자주 묻는 질문 – We-Kids</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/resources/css/faq.css">
</head>
<body>



<!-- ════════ MAIN ════════ -->
<main class="main-wrap">

    <!-- 페이지 헤더 -->
    <div class="faq-page-header">
        <div class="faq-title-row">
            <a href="javascript:history.back()" class="back-btn" aria-label="뒤로가기">
                <svg viewBox="0 0 24 24"><polyline points="15 18 9 12 15 6"/></svg>
            </a>
            <div>
                <h1 class="faq-title">자주 묻는 질문 (FAQ)</h1>
                <p class="faq-subtitle">궁금하신 내용을 검색하거나 카테고리별로 확인해보세요.</p>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/support/list" class="btn-contact">
            <svg viewBox="0 0 24 24">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            </svg>
            1:1 문의하기
        </a>
    </div>

    <!-- FAQ 카드 -->
    <div class="faq-card">

        <!-- 검색 -->
        <div class="faq-search-wrap">
            <svg viewBox="0 0 24 24" class="search-icon">
                <circle cx="11" cy="11" r="8"/>
                <line x1="21" y1="21" x2="16.65" y2="16.65"/>
            </svg>
            <input
                type="text"
                id="faqSearchInput"
                class="faq-search-input"
                placeholder="궁금하신 내용을 입력해주세요. (예: 비밀번호 찾기, 클래스 가입 등)"
                oninput="filterFaq()"
            />
        </div>

        <!-- 카테고리 탭 (DB에서 동적 생성) -->
        <div class="faq-tabs">
            <button class="faq-tab ${currentCategory eq 'all' ? 'active' : ''}"
                    onclick="setTab(this, 'all')">전체</button>
            <c:forEach var="cat" items="${categoryList}">
                <button class="faq-tab ${currentCategory eq cat ? 'active' : ''}"
                        onclick="setTab(this, '${cat}')">
                    ${cat}
                </button>
            </c:forEach>
        </div>

        <!-- FAQ 목록 (DB 데이터) -->
        <ul class="faq-list" id="faqList">
            <c:choose>
                <c:when test="${not empty faqList}">
                    <c:forEach var="faq" items="${faqList}">
                        <li class="faq-item" data-category="${faq.category}">
                            <button class="faq-question" onclick="toggleFaq(this)">
                                <span class="faq-q-mark">Q.</span>
                                <span class="faq-q-text">${faq.question}</span>
                                <svg class="faq-chevron" viewBox="0 0 24 24">
                                    <polyline points="6 9 12 15 18 9"/>
                                </svg>
                            </button>
                            <div class="faq-answer">
                                <p>${faq.answer}</p>
                            </div>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="faq-empty">
                        <p>등록된 FAQ가 없습니다.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </ul>

        <!-- 검색 결과 없음 -->
        <div class="faq-empty" id="faqEmpty" style="display:none;">
            <p>검색 결과가 없습니다.</p>
        </div>

    </div>
</main>

<style>
/* ===========================
   We-Kids – faq.css
   =========================== */

/* --- Reset & Base --- */
*, *::before, *::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html {
  background-color: #eef2ee;
  min-height: 100%;
}




/* ===========================
   MAIN WRAP
   =========================== */
.main-wrap {
  margin-left: 210px;
  margin-top: 56px;
  flex: 1;
  padding: 36px 40px;
  min-height: calc(100vh - 56px);
  width: calc(100% - 210px);
  
}

/* ===========================
   FAQ PAGE HEADER
   =========================== */
.faq-page-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
  max-width: 860px;
  margin-left: auto;
  margin-right: auto;
}

.faq-title-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.back-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  color: #5a8060;
  text-decoration: none;
  flex-shrink: 0;
  transition: background-color 0.15s, color 0.15s;
}

.back-btn:hover {
  background-color: #ddeae0;
  color: #2a6e3f;
}

.back-btn svg {
  width: 20px;
  height: 20px;
  stroke: currentColor;
  fill: none;
  stroke-width: 2.5;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.faq-title {
  font-size: 22px;
  font-weight: 700;
  color: #1a2a1e;
  line-height: 1.3;
}

.faq-subtitle {
  font-size: 13px;
  color: #7a9a7e;
  margin-top: 4px;
}

/* 1:1 문의하기 버튼 */
.btn-contact {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  background-color: #3b6ef8;
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  padding: 10px 20px;
  border-radius: 10px;
  text-decoration: none;
  white-space: nowrap;
  flex-shrink: 0;
  transition: background-color 0.15s;
}

.btn-contact:hover {
  background-color: #2a5ce0;
}

.btn-contact svg {
  width: 16px;
  height: 16px;
  stroke: currentColor;
  fill: none;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  flex-shrink: 0;
}

/* ===========================
   FAQ CARD
   =========================== */
.faq-card {
  background-color: #ffffff;
  border-radius: 16px;
  padding: 28px 32px;
  box-shadow: 0 1px 6px rgba(42, 110, 63, 0.07);
  max-width: 860px;
  margin: 0 auto;
}

/* ===========================
   SEARCH
   =========================== */
.faq-search-wrap {
  position: relative;
  margin-bottom: 24px;
}

.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  height: 16px;
  stroke: #a0b5a4;
  fill: none;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  pointer-events: none;
}

.faq-search-input {
  width: 100%;
  padding: 13px 16px 13px 44px;
  border: 1px solid #e0e8e2;
  border-radius: 10px;
  font-size: 14px;
  font-family: inherit;
  color: #2d3a2d;
  background-color: #f0f2f4;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.faq-search-input::placeholder {
  color: #a0b5a4;
}

.faq-search-input:focus {
  border-color: #2a6e3f;
  box-shadow: 0 0 0 3px rgba(42, 110, 63, 0.08);
  background-color: #ffffff;
}

/* ===========================
   CATEGORY TABS
   =========================== */
.faq-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.faq-tab {
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  font-family: inherit;
  cursor: pointer;
  border: 1px solid #e0e8e2;
  background-color: #ffffff;
  color: #5a7a5e;
  transition: background-color 0.15s, color 0.15s, border-color 0.15s;
}

.faq-tab:hover {
  background-color: #edf4ee;
  border-color: #c0d8c4;
}

.faq-tab.active {
  background-color: #1a2a1e;
  color: #ffffff;
  border-color: #1a2a1e;
  font-weight: 600;
}

/* ===========================
   FAQ LIST
   =========================== */
.faq-list {
  list-style: none;
}

.faq-item {
  border-bottom: 1px solid #edf2ee;
}

.faq-item:last-child {
  border-bottom: none;
}

/* 질문 버튼 */
.faq-question {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 4px;
  background: none;
  border: none;
  cursor: pointer;
  text-align: left;
  font-family: inherit;
}

.faq-q-mark {
  font-size: 15px;
  font-weight: 700;
  color: #3b6ef8;
  flex-shrink: 0;
}

.faq-q-text {
  font-size: 15px;
  font-weight: 500;
  color: #1a2a1e;
  flex: 1;
  line-height: 1.5;
}

.faq-item:hover .faq-q-text {
  color: #2a6e3f;
}

/* 화살표 아이콘 */
.faq-chevron {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  stroke: #8aaa8e;
  fill: none;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  transition: transform 0.25s ease;
}

.faq-item.open .faq-chevron {
  transform: rotate(180deg);
  stroke: #2a6e3f;
}

/* 답변 영역 (아코디언) */
.faq-answer {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.faq-answer p {
  padding: 0 4px 20px 28px;
  font-size: 14px;
  line-height: 1.8;
  color: #5a7a5e;
}

/* ===========================
   EMPTY STATE
   =========================== */
.faq-empty {
  text-align: center;
  padding: 48px 20px;
  font-size: 14px;
  color: #a0b5a4;
}

/* ===========================
   RESPONSIVE
   =========================== */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    transition: transform 0.25s ease;
  }

  .topnav {
    left: 0;
  }

  .main-wrap {
    margin-left: 0;
    padding: 20px 16px;
    width: 100%;
  }

  .faq-page-header {
    flex-direction: column;
    gap: 12px;
  }

  .faq-card {
    padding: 20px 18px;
    border-radius: 12px;
  }

  .faq-title {
    font-size: 18px;
  }
}


</style>

<script>
    let currentCategory = '${currentCategory}';

    function setTab(btn, category) {
        document.querySelectorAll('.faq-tab').forEach(t => t.classList.remove('active'));
        btn.classList.add('active');
        currentCategory = category;
        filterFaq();
    }

    function filterFaq() {
        const keyword = document.getElementById('faqSearchInput').value.trim().toLowerCase();
        const items   = document.querySelectorAll('.faq-item');
        let visibleCount = 0;

        items.forEach(item => {
            const cat       = item.dataset.category;
            const text      = item.querySelector('.faq-q-text').textContent.toLowerCase();
            const matchCat  = currentCategory === 'all' || cat === currentCategory;
            const matchText = keyword === '' || text.includes(keyword);

            if (matchCat && matchText) {
                item.style.display = '';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });

        document.getElementById('faqEmpty').style.display = visibleCount === 0 ? 'block' : 'none';
    }

    function toggleFaq(btn) {
        const item   = btn.closest('.faq-item');
        const answer = item.querySelector('.faq-answer');
        const isOpen = item.classList.contains('open');

        // 다른 열린 항목 닫기
        document.querySelectorAll('.faq-item.open').forEach(el => {
            el.classList.remove('open');
            el.querySelector('.faq-answer').style.maxHeight = null;
        });

        if (!isOpen) {
            item.classList.add('open');
            answer.style.maxHeight = answer.scrollHeight + 'px';
        }
    }
</script>

</body>
</html>
