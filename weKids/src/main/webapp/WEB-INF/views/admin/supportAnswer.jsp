<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="admin-support-answer-page">

	<div class="page-header-row">
		<div>
			<a href="${pageContext.request.contextPath}/admin/support"
				class="back-link"> <svg width="16" height="16"
					viewBox="0 0 24 24" fill="none" stroke="currentColor"
					stroke-width="2">
                    <polyline points="15 18 9 12 15 6" />
                </svg> 목록으로 돌아가기
			</a>
			<h1 class="page-title">문의 답변</h1>
			<p class="page-desc">문의 내용을 확인하고 답변을 등록합니다.</p>
		</div>
	</div>

	<%-- ── 문의 내용 카드 ── --%>
	<section class="panel-card answer-inquiry-card">
		<h2 class="panel-title">문의 내용</h2>

		<div class="answer-meta-row">
			<span
				class="status-pill ${support.status eq '대기중' ? 'dormant' : support.status eq '긴급' ? 'suspended' : 'active'}">
				<c:choose>
					<c:when test="${support.status eq '대기중'}">답변 대기</c:when>
					<c:when test="${support.status eq '긴급'}">긴급 처리</c:when>
					<c:otherwise>답변 완료</c:otherwise>
				</c:choose>
			</span> <span class="role-pill category"> <c:choose>
					<c:when test="${support.category eq 'account'}">계정 / 로그인</c:when>
					<c:when test="${support.category eq 'class'}">수업 / 클래스</c:when>
					<c:when test="${support.category eq 'payment'}">결제 / 환불</c:when>
					<c:when test="${support.category eq 'tech'}">기술적 문제</c:when>
					<c:when test="${support.category eq 'report'}">신고</c:when>
					<c:when test="${support.category eq 'other'}">기타</c:when>
					<c:otherwise>${support.category}</c:otherwise>
				</c:choose>
			</span> <span class="answer-date"> <fmt:formatDate
					value="${support.createdAt}" pattern="yyyy.MM.dd HH:mm" />
			</span>
		</div>

		<h3 class="answer-inquiry-title">${support.title}</h3>
		<p class="answer-inquiry-writer">작성자 ID: ${support.writerId}</p>

		<div class="answer-inquiry-content">${support.content}</div>
	</section>
	<c:if test="${not empty fileList}">
		<div class="admin-file-viewer" style="margin-top: 20px; padding: 15px; background: rgba(255, 255, 255, 0.05); border-radius: 8px;">
			<p style="color: #9ca3af; font-size: 13px; margin-bottom: 10px;">첨부파일</p>

			<div class="img-preview-grid"
				style="display: flex; flex-wrap: wrap; gap: 10px; justify-content:center;">
				<c:forEach var="file" items="${fileList}">
					<c:set var="fileName" value="${fn:toLowerCase(file.fileOriName)}" />
					<c:if
						test="${fn:endsWith(fileName, '.jpg') || fn:endsWith(fileName, '.png') || fn:endsWith(fileName, '.jpeg') || fn:endsWith(fileName, '.gif')}">
						<img
							src="${pageContext.request.contextPath}/support/file/download?fileName=${file.fileName}"
							style="max-width: 200px; border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 4px; cursor: pointer;"
							onclick="window.open(this.src)">
					</c:if>
				</c:forEach>
			</div>

			<div class="file-download-links" style="margin-top: 10px;">
				<c:forEach var="file" items="${fileList}">
					<a
						href="${pageContext.request.contextPath}/support/file/download?fileName=${file.fileName}&fileOriName=${file.fileOriName}"
						style="color: #6366f1; font-size: 13px; text-decoration: none; margin-right: 15px;">
						📎 ${file.fileOriName} </a>
				</c:forEach>
			</div>
		</div>
	</c:if>
	<%-- ── 답변 입력 폼 ── --%>
	<section class="panel-card answer-form-card">
		<h2 class="panel-title">답변 등록</h2>

		<form action="${pageContext.request.contextPath}/admin/support/answer"
			method="post">
			<input type="hidden" name="supportNo" value="${support.supportNo}">

			<div class="answer-form-group">
				<label class="answer-form-label">답변 내용 <span
					class="required-mark">*</span></label>
				<textarea class="answer-textarea" name="answerContent"
					placeholder="답변 내용을 입력하세요." rows="8" required>${not empty prevAnswerContent ? prevAnswerContent : ''}</textarea>
			</div>

			<div class="answer-form-actions">
				<button type="button" class="modal-cancel-btn"
					onclick="history.back();">취소</button>
				<button type="submit" class="modal-submit-btn">답변 등록</button>
			</div>

		</form>
	</section>

</div>

<style>
.admin-support-answer-page {
	max-width: 860px;
	margin: 0 auto;
	padding: 20px;
}

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

.back-link:hover {
	color: var(--text-primary, #111827);
}

.answer-inquiry-card {
	margin-bottom: 16px;
	margin-top: 20px;
}

.answer-form-card {
	margin-bottom: 16px;
}

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
	color: var(--text-primary, #FFFFFF);
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

.answer-form-group {
	margin-bottom: 20px;
}

.answer-form-label {
	display: block;
	font-size: 13px;
	font-weight: 600;
	color: var(--text-primary, #FFFFFF);
	margin-bottom: 8px;
}

.required-mark {
	color: #dc2626;
	margin-left: 2px;
}

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
	align-items: center;
	gap: 12px; /* 취소와 답변 등록 버튼 사이의 간격 */
	margin-top: 25px; /* 폼과의 간격 */
}

.modal-cancel-btn {
	/* 중앙 정렬을 위한 설정 */
	display: inline-flex;
	align-items: center;
	justify-content: center;
	/* 버튼 형태 유지 */
	width: 120px; /* 적절한 너비 설정 */
	height: 44px; /* 답변 등록 버튼과 높이를 통일 */
	border-radius: 8px;
	border: 1px solid rgba(255, 255, 255, 0.3);
	/* 텍스트 스타일 */
	color: #FFFFFF;
	text-decoration: none;
	font-size: 14px;
	font-weight: 600;
	transition: all 0.2s;
}
.admin-file-viewer {
    margin: 20px 0; /* 위아래 카드의 간격을 벌려줍니다 */
    padding: 20px;
    background: rgba(255, 255, 255, 0.03); 
    border: 1px solid var(--border, rgba(255, 255, 255, 0.08));
    border-radius: 12px;
    margin-bottom : 20px;
    /* 만약 배경색을 카드와 똑같이 하고 싶다면 background: var(--card-bg); 사용 */
}
.inqury-btn {
	display: inline-flex; /* flex를 사용해야 내부 글자 중앙 정렬이 가능합니다 */
	align-items: center;
	justify-content: center;
	width: 100px; /* 버튼 너비 */
	height: 46px; /* 답변 등록 버튼과 높이 통일 */
	color: #FFFFFF !important; /* 글자색 하얀색 */
	font-size: 14px;
	font-weight: 600;
	text-decoration: none; /* 링크 밑줄 제거 */
	background-color: transparent;
	border: 1px solid rgba(255, 255, 255, 0.3); /* 연한 하얀색 테두리 */
	border-radius: 8px;
	transition: all 0.2s;
	cursor: pointer;
}
</style>
