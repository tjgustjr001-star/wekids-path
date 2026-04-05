<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 카테고리별 카운트 집계 --%>
<c:set var="totalFaqCount" value="${fn:length(faqList)}" />

<div class="admin-support-page">

    <%-- ── 페이지 헤더 ── --%>
    <div class="page-header-row">
        <div>
            <h1 class="page-title">FAQ 관리</h1>
            <p class="page-desc">자주 묻는 질문 목록을 확인하고 등록 및 삭제합니다.</p>
        </div>
        <div class="page-header-actions">
            <button type="button" class="primary-btn" id="openFaqFormBtn">FAQ 등록</button>
            <button type="button" class="btn-danger-outline" id="toggleDeleteBtn">삭제 선택</button>
        </div>
    </div>

    <%-- ── 통계 카드 ── --%>
    <div class="support-stat-grid">
        <section class="panel-card">
            <div class="support-stat-item">
                <span class="support-stat-label">전체 FAQ</span>
                <strong class="support-stat-value">${totalFaqCount}<span class="support-stat-unit">건</span></strong>
            </div>
        </section>
        <c:forEach var="cat" items="${categoryList}">
            <section class="panel-card">
                <div class="support-stat-item">
                    <span class="support-stat-label">${cat}</span>
                    <strong class="support-stat-value answered">
                        <c:set var="catCount" value="0"/>
                        <c:forEach var="faq" items="${faqList}">
                            <c:if test="${faq.category eq cat}">
                                <c:set var="catCount" value="${catCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${catCount}<span class="support-stat-unit">건</span>
                    </strong>
                </div>
            </section>
        </c:forEach>
    </div>

    <%-- ── FAQ 등록 폼 (기본 숨김) ── --%>
    <div id="faqAddFormWrap" style="display:none; margin-bottom:16px;">
        <section class="panel-card">
            <h2 class="panel-title">FAQ 등록</h2>
            <form action="${pageContext.request.contextPath}/admin/faq/add" method="post">
                <div class="faq-form-row">
                    <div class="faq-form-group">
                        <label>문의 유형 <span class="required-mark">*</span></label>
						<div class="select-wrapper">
							<select name="category" class="faq-category-select" required>
								<option value="">유형 전체</option>
								<option value="결제 / 환불">결제 / 환불</option>
								<option value="수업 / 클래스">수업 / 클래스</option>
								<option value="계정 / 로그인">계정 / 로그인</option>
								<option value="오류 / 버그">오류 / 버그</option>
								<option value="신고">신고</option>
								<option value="기타">기타</option>
							</select>
						</div>                        
                        
                    </div>
                    <div class="faq-form-group faq-form-group--wide">
                        <label>질문 <span class="required-mark">*</span></label>
                        <input type="text" name="question" placeholder="FAQ 질문을 입력하세요." required>
                    </div>
                </div>
                <div class="faq-form-group" style="margin-bottom:12px;">
                    <label>답변 <span class="required-mark">*</span></label>
                    <textarea name="answer" rows="3" placeholder="FAQ 답변을 입력하세요." required></textarea>
                </div>
                <div class="faq-form-button" style="display:flex; justify-content:flex-end; gap:8px;">
                    <button type="button" class="modal-cancel-btn" onclick="toggleFaqForm()">취소</button>
                    <button type="submit" class="modal-submit-btn">등록하기</button>
                </div>
            </form>
        </section>
    </div>

    <%-- ── 삭제 확인 바 (삭제 모드일 때만) ── --%>
    <div id="deleteConfirmBar" style="display:none; text-align:right; margin-bottom:8px;">
        <button type="button" class="btn-danger" onclick="deleteSelectedFaqs()">선택 항목 삭제</button>
        <button type="button" class="modal-cancel-btn" onclick="toggleDeleteMode()">취소</button>
    </div>

    <%-- ── FAQ 목록 ── --%>
    <section class="users-board">
        <div class="users-board-top">
            <div class="tab-group">
                <button type="button" class="support-tab-btn active" data-filter="전체">전체</button>
                <c:forEach var="cat" items="${categoryList}">
                    <button type="button" class="support-tab-btn" data-filter="${cat}">${cat}</button>
                </c:forEach>
            </div>
            <div class="support-board-right">
                <div class="search-box">
                    <input type="text" id="faqSearchInput" placeholder="질문 내용 검색">
                </div>
            </div>
        </div>

        <div class="user-table-wrap">
            <table class="user-table" id="faqTable">
                <thead>
                    <tr>
                        <th id="checkboxHeader" style="display:none; width:40px;"></th>
                        <th>카테고리</th>
                        <th>질문</th>
                        <th>답변</th>
                        <th>등록일</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="faq" items="${faqList}">
                        <tr class="user-row faq-row"
                            data-category="${faq.category}"
                            data-question="${faq.question}">

                            <%-- 체크박스 셀 (삭제 모드) --%>
                            <td class="faq-check-cell" style="display:none; text-align:center;">
                                <input type="checkbox" class="faq-checkbox"
                                       name="faqCheck" value="${faq.faqId}"
                                       style="width:16px; height:16px; cursor:pointer; accent-color:#dc2626;">
                            </td>

                            <td><span class="role-pill category">${faq.category}</span></td>

                            <td class="user-name-cell support-title-cell">${faq.question}</td>

                            <td class="faq-answer-cell">${faq.answer}</td>

                            <td><fmt:formatDate value="${faq.createdAt}" pattern="yyyy.MM.dd"/></td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty faqList}">
                        <tr>
                            <td colspan="5" style="text-align:center; padding:30px;">
                                등록된 FAQ가 없습니다.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <div class="users-board-footer">
            <span>총 <span id="faqCountText">${totalFaqCount}</span>건</span>
            <div class="support-pagination" id="paginationWrap"></div>
        </div>
    </section>

</div>

<script>
document.addEventListener('DOMContentLoaded', function () {

    /* ── 탭 필터 + 검색 + 페이징 ── */
    const tabBtns  = document.querySelectorAll('.support-tab-btn');
    const searchEl = document.getElementById('faqSearchInput');
    const PAGE_SIZE = 10;
    let currentPage  = 1;
    let filteredRows = [];

    function applyFilter() {
        const activeBtn  = document.querySelector('.support-tab-btn.active');
        const category   = activeBtn ? activeBtn.dataset.filter : '전체';
        const keyword    = searchEl ? searchEl.value.toLowerCase().trim() : '';

        filteredRows = [];
        document.querySelectorAll('.faq-row').forEach(function(row) {
            const matchCat     = category === '전체' || row.dataset.category === category;
            const matchKeyword = !keyword  || row.dataset.question.toLowerCase().includes(keyword);

            row.style.display = 'none';
            if (matchCat && matchKeyword) filteredRows.push(row);
        });

        currentPage = 1;
        renderPage();
    }

    function renderPage() {
        const start = (currentPage - 1) * PAGE_SIZE;
        const end   = start + PAGE_SIZE;

        filteredRows.forEach(function(row, i) {
            row.style.display = (i >= start && i < end) ? '' : 'none';
        });

        const countEl = document.getElementById('faqCountText');
        if (countEl) countEl.textContent = filteredRows.length;

        renderPagination();
    }

    function renderPagination() {
        const wrap = document.getElementById('paginationWrap');
        if (!wrap) return;

        const totalPages = Math.ceil(filteredRows.length / PAGE_SIZE);
        wrap.innerHTML = '';
        if (totalPages <= 1) return;

        wrap.appendChild(makePageBtn('이전', currentPage === 1, function() {
            if (currentPage > 1) { currentPage--; renderPage(); }
        }));

        for (var p = 1; p <= totalPages; p++) {
            (function(page) {
                const btn = makePageBtn(page, false, function() {
                    currentPage = page; renderPage();
                });
                if (page === currentPage) btn.classList.add('active');
                wrap.appendChild(btn);
            })(p);
        }

        wrap.appendChild(makePageBtn('다음', currentPage === totalPages, function() {
            if (currentPage < totalPages) { currentPage++; renderPage(); }
        }));
    }

    function makePageBtn(label, disabled, onClick) {
        const btn = document.createElement('button');
        btn.textContent = label;
        btn.className   = 'support-page-btn';
        btn.disabled    = disabled;
        btn.addEventListener('click', onClick);
        return btn;
    }

    tabBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            tabBtns.forEach(function(b) { b.classList.remove('active'); });
            this.classList.add('active');
            applyFilter();
        });
    });

    if (searchEl) searchEl.addEventListener('input', debounce(applyFilter, 250));

    function debounce(fn, ms) {
        let timer;
        return function() { clearTimeout(timer); timer = setTimeout(() => fn.apply(this, arguments), ms); };
    }

    applyFilter();

    /* ── FAQ 등록 폼 토글 ── */
    window.toggleFaqForm = function() {
        const wrap = document.getElementById('faqAddFormWrap');
        const isVisible = wrap.style.display !== 'none';
        wrap.style.display = isVisible ? 'none' : 'block';
        if (!isVisible && deleteMode) toggleDeleteMode();
    };

    document.getElementById('openFaqFormBtn')
        .addEventListener('click', toggleFaqForm);

    /* ── 삭제 모드 ── */
    var deleteMode = false;

    window.toggleDeleteMode = function() {
        deleteMode = !deleteMode;
        const checkCells   = document.querySelectorAll('.faq-check-cell');
        const checkboxHeader = document.getElementById('checkboxHeader');
        const confirmBar   = document.getElementById('deleteConfirmBar');
        const toggleBtn    = document.getElementById('toggleDeleteBtn');

        checkCells.forEach(function(td) { td.style.display = deleteMode ? '' : 'none'; });
        if (checkboxHeader) checkboxHeader.style.display = deleteMode ? '' : 'none';
        if (confirmBar) confirmBar.style.display = deleteMode ? 'block' : 'none';
        if (toggleBtn)  toggleBtn.classList.toggle('active', deleteMode);

        // 체크박스 초기화
        document.querySelectorAll('.faq-checkbox').forEach(function(cb) { cb.checked = false; });

        if (deleteMode) {
            const form = document.getElementById('faqAddFormWrap');
            if (form) form.style.display = 'none';
        }
    };

    document.getElementById('toggleDeleteBtn')
        .addEventListener('click', toggleDeleteMode);

    /* ── 선택 삭제 ── */
    window.deleteSelectedFaqs = function() {
        const selectedIds = [];
        document.querySelectorAll('input[name="faqCheck"]:checked').forEach(function(cb) {
            selectedIds.push(cb.value);
        });

        if (selectedIds.length === 0) {
            alert('삭제할 항목을 선택해주세요.');
            return;
        }

        if (confirm(selectedIds.length + '개의 FAQ를 삭제하시겠습니까?')) {
            location.href = '${pageContext.request.contextPath}/admin/faq/deleteMany?faqIds='
                            + selectedIds.join(',');
        }
    };
});
</script>

<style>
/* ── FAQ 전용 추가 스타일 ── */
.btn-danger-outline {
    padding: 8px 16px;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 600;
    font-family: inherit;
    cursor: pointer;
    background: #fff;
    color: #dc2626;
    border: 1px solid #dc2626;
    transition: background 0.15s;
}
.btn-danger-outline:hover,
.btn-danger-outline.active { background: #dc2626; color: #fff; }

.btn-danger {
    padding: 8px 16px;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 600;
    font-family: inherit;
    cursor: pointer;
    background: #dc2626;
    color: #fff;
    border: none;
    transition: background 0.15s;
}
.btn-danger:hover { background: #b91c1c; }

.faq-form-row {
    display: flex;
    gap: 12px;
    margin-bottom: 12px;
}

.faq-form-group {
    display: flex;
    flex-direction: column;
    gap: 6px;
    flex: 1;
}

.faq-form-group--wide { flex: 2; }

.faq-form-group label {
    font-size: 13px;
    font-weight: 600;
    margin-left:300px;
}




.required-mark { color: #dc2626; margin-left: 2px; }

.faq-form-group input{
	width:600px;
    padding: 9px 12px;
    border: 1px solid var(--border, #e5e7eb);
    border-radius: 6px;
    font-size: 13px;
    font-family: inherit;
    background: var(--bg-input, #fff);
    color: var(--text-primary, #111827);
    outline: none;
    resize: vertical;
    transition: border-color 0.15s;
    margin-left:100px;
}
.faq-form-group textarea {
	width:920px;
	margin-left:300px;
    padding: 9px 12px;
    border: 1px solid var(--border, #e5e7eb);
    border-radius: 6px;
    font-size: 13px;
    font-family: inherit;
    background: var(--bg-input, #fff);
    color: var(--text-primary, #111827);
    outline: none;
    resize: vertical;
    transition: border-color 0.15s;
}

.faq-form-group:nth-child(1) label {
    font-size: 13px;
    font-weight: 600;
    margin-left: 300px; !important
}


.faq-form-group input:focus,
.faq-form-group textarea:focus { border-color: #6366f1; }

/* 카테고리 셀렉트 */
.select-wrapper { position: relative; padding-left: 200px;}

.faq-category-select {
    width: 100%;
    padding: 9px 12px;
    border: 1px solid var(--border, #e5e7eb);
    border-radius: 6px;
    font-size: 13px;
    font-family: inherit;
    background: var(--bg-input, #fff);
    color: var(--text-primary, #111827);
    outline: none;
    appearance: none;
    cursor: pointer;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 24 24' fill='none' stroke='%236b7280' stroke-width='2'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 10px center;
    padding-right: 30px;
    margin-left:100px;
}
.faq-category-select:focus { border-color: #6366f1; }

/* 답변 셀 말줄임 */
.faq-answer-cell {
    max-width: 360px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: var(--text-secondary, #6b7280);
    font-size: 13px;
}

/* support.jsp 공통 스타일 재사용 */
.support-tab-btn {
    padding: 7px 16px; border-radius: 6px;
    font-size: 13px; font-weight: 500;
    color: #6b7280; background: none; border: none;
    cursor: pointer; transition: all 0.15s;
}
.support-tab-btn:hover  { color: #111827; }
.support-tab-btn.active { background: #4a9eff; color: #fff; font-weight: 600; }

.support-stat-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    margin-top: 20px;
    margin-bottom: 20px;
}
.support-stat-item  { display: flex; flex-direction: column; gap: 6px; padding: 4px 0; }
.support-stat-label { font-size: 13px; color: #6b7280; font-weight: 500; }
.support-stat-value { font-size: 28px; font-weight: 700; letter-spacing: -0.02em; line-height: 1.1; }
.support-stat-value.answered { color: #16a34a; }
.support-stat-unit  { font-size: 14px; font-weight: 500; margin-left: 2px; color: #6b7280; }

.support-board-right { display: flex; align-items: center; gap: 10px; }

.users-board-footer {
    display: flex; align-items: center;
    justify-content: space-between; flex-wrap: wrap;
    gap: 12px; padding-top: 12px;
}

.support-pagination { display: flex; align-items: center; gap: 4px; }

.support-page-btn {
    min-width: 34px; height: 34px; padding: 0 10px;
    display: inline-flex; align-items: center; justify-content: center;
    background: var(--bg-card, #1a1e2b);
    border: 1px solid var(--border, rgba(255,255,255,0.08));
    border-radius: 6px;
    color: var(--text-secondary, #9ca3af);
    font-size: 13px; font-weight: 500; font-family: inherit;
    cursor: pointer; transition: all 0.15s;
}
.support-page-btn:hover:not(:disabled) {
    background: var(--bg-card-hover, #1f2435);
    color: var(--text-primary, #f9fafb);
}
.support-page-btn.active { background: #4a9eff; border-color: #4a9eff; color: #fff; font-weight: 700; }
.support-page-btn:disabled { opacity: 0.35; cursor: not-allowed; }

.role-pill.category { background: #f3f4f6; color: #374151; border: 1px solid #e5e7eb; }
/* 불필요한 속성은 제거하고 크기와 간격만 유지 */

.faq-form-button{
 
    display: flex !important;           /* flex 레이아웃 강제 적용 */
    justify-content: center !important;  /* 가로축 중앙 정렬 */
    align-items: center !important;      /* 세로축 중앙 정렬 */
    gap: 20px !important;                /* 버튼 사이의 간격 (이미지 기준 벌어지게 설정) */
    width: 100%;                         /* 부모 너비 전체 사용 */
    padding: 24px 0 8px 0;               /* 상단 여백 추가 및 하단 여백 조정 */
    margin-top: 20px;
    
}

.admin-support-page .modal-cancel-btn {
  padding: 8px 16px;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 600;
    font-family: inherit;
    cursor: pointer;
    background: #ff;
    color: #fff;
    border: none;
    transition: background 0.15s;
}

.admin-support-page .modal-submit-btn {
    all: unset; /* 초기화 */
    
    /* 크기 고정 */
    width: 200px !important;
    height: 50px !important;
    background-color: #4a9eff; /* 파란색 적용 */
    
    /* 디자인 */
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 500;
    
    /* 중앙 정렬을 위한 배치 */
    display: inline-flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s;
    border: none !important;
}

.admin-support-page .modal-cancel-btn:hover {
    background-color: #e5e7eb;
    color: #111827;
}

.faq-form-group .faq-form-group--wide{
	margin-left:100px;
	width:100px;
}

.faq-form-group .faq-form-group--wide label{
	
	margin-left:100px;
}

.faq-form-group faq-form-group--wide input{
	
	width:100px;
}







@media (max-width: 1024px) { .support-stat-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px)  { .support-stat-grid { grid-template-columns: 1fr 1fr; gap: 10px; } }
</style>
