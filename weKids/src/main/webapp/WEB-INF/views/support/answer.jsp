<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="admin-support-answer-page">

    <div class="page-header-row">
        <div>
            <a href="${pageContext.request.contextPath}/admin/support" class="back-link">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <polyline points="15 18 9 12 15 6"/>
                </svg>
                목록으로 돌아가기
            </a>
            <h1 class="page-title">문의 답변</h1>
            <p class="page-desc">문의 내용을 확인하고 답변을 등록합니다.</p>
        </div>
    </div>

    <%-- ── 문의 내용 카드 ── --%>
    <section class="panel-card answer-inquiry-card">
        <h2 class="panel-title">문의 내용</h2>

        <div class="answer-meta-row">
            <span class="status-pill ${support.status eq '대기중' ? 'dormant' : support.status eq '신고' ? 'suspended' : 'active'}">
                <c:choose>
                    <c:when test="${support.status eq '대기중'}">답변 대기</c:when>
                    <c:when test="${support.status eq '신고'}">신고 처리</c:when>
                    <c:otherwise>답변 완료</c:otherwise>
                </c:choose>
            </span>
            <span class="role-pill category">${support.category}</span>
            <span class="answer-date">
                <fmt:formatDate value="${support.createdAt}" pattern="yyyy.MM.dd HH:mm"/>
            </span>
        </div>

        <h3 class="answer-inquiry-title">${support.title}</h3>
        <p class="answer-inquiry-writer">작성자 ID: ${support.writerId}</p>

        <div class="answer-inquiry-content">${support.content}</div>
    </section>

    <%-- ── 답변 입력 폼 ── --%>
    <section class="panel-card answer-form-card">
        <h2 class="panel-title">답변 등록</h2>

        <form action="${pageContext.request.contextPath}/admin/support/answer" method="post">
            <input type="hidden" name="supportNo" value="${support.supportNo}">

            <div class="answer-form-group">
                <label class="answer-form-label">답변 내용 <span class="required-mark">*</span></label>
                <textarea class="answer-textarea"
                          name="answerContent"
                          placeholder="답변 내용을 입력하세요."
                          rows="8"
                          required>${not empty prevAnswerContent ? prevAnswerContent : ''}</textarea>
            </div>

            <div class="answer-form-actions">
                <a href="${pageContext.request.contextPath}/admin/support"
                   class="modal-cancel-btn">취소</a>
                <button type="submit" class="modal-submit-btn">답변 등록</button>
            </div>
        </form>
    </section>

</div>

<style>
.admin-support-answer-page { max-width: 860px; }

.back-link {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: var(--text-secondary, #6b7280);
    text-decoration: none;
    margin-bottom: 10px;
    transition: color 0.15s;
}
.back-link:hover { color: var(--text-primary, #111827); }

.answer-inquiry-card { margin-bottom: 16px; }
.answer-form-card    { margin-bottom: 16px; }

.answer-meta-row {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 14px;
}

.answer-date {
    font-size: 12px;
    color: var(--text-secondary, #6b7280);
    margin-left: auto;
}

.answer-inquiry-title {
    font-size: 18px;
    font-weight: 700;
    color: var(--text-primary, #111827);
    margin-bottom: 6px;
    letter-spacing: -0.01em;
}

.answer-inquiry-writer {
    font-size: 13px;
    color: var(--text-secondary, #6b7280);
    margin-bottom: 16px;
}

.answer-inquiry-content {
    font-size: 14px;
    line-height: 1.75;
    color: var(--text-primary, #111827);
    white-space: pre-wrap;
    background: var(--bg-input, #f9fafb);
    border: 1px solid var(--border, #e5e7eb);
    border-radius: 8px;
    padding: 16px 18px;
}

.answer-form-group  { margin-bottom: 20px; }

.answer-form-label {
    display: block;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-primary, #111827);
    margin-bottom: 8px;
}

.required-mark { color: #dc2626; margin-left: 2px; }

.answer-textarea {
    width: 100%;
    padding: 12px 14px;
    border: 1px solid var(--border, #e5e7eb);
    border-radius: 8px;
    font-size: 14px;
    font-family: inherit;
    line-height: 1.6;
    resize: vertical;
    transition: border-color 0.15s;
    background: #fff;
    color: var(--text-primary, #111827);
}
.answer-textarea:focus {
    outline: none;
    border-color: #6366f1;
}

.answer-form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}
</style>
