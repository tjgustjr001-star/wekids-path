<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="admin-support-detail-page">

	<%-- ── 페이지 헤더 ── --%>
	<div class="page-header-row">
		<div>
			<a href="${pageContext.request.contextPath}/admin/support"
				class="detail-back-link"> <svg width="16" height="16"
					viewBox="0 0 24 24" fill="none" stroke="currentColor"
					stroke-width="2">
                    <polyline points="15 18 9 12 15 6" />
                </svg> 목록으로 돌아가기
			</a>
			<h1 class="page-title">문의 상세</h1>
			<p class="page-desc">문의 내용과 등록된 답변을 확인합니다.</p>
		</div>
	</div>

	<%-- ── 문의 내용 카드 ── --%>
	<section class="panel-card support-detail-card">
		<div class="support-detail-meta-row">
			<span
				class="status-pill ${support.status eq '대기중' ? 'dormant' : support.status eq '긴급' ? 'suspended' : 'active'}">
				<c:choose>
					<c:when test="${support.status eq '대기중'}">답변 대기</c:when>
					<c:when test="${support.status eq '긴급'}">긴급 처리</c:when>
					<c:otherwise>답변 완료</c:otherwise>
				</c:choose>
			</span> <span class="role-pill category"> <c:choose>
					<c:when test="${support.category eq 'tech'}">기술/오류</c:when>
					<c:when test="${support.category eq 'payment'}">결제</c:when>
					<c:when test="${support.category eq 'class'}">수업/클래스</c:when>
					<c:when test="${support.category eq 'account'}">계정/로그인</c:when>
					<c:when test="${support.category eq 'other'}">기타</c:when>
					<c:otherwise>${support.category}</c:otherwise>
				</c:choose>
			</span> <span class="support-detail-date"> <fmt:formatDate
					value="${support.createdAt}" pattern="yyyy.MM.dd HH:mm" />
			</span>
		</div>

		<h2 class="support-detail-title">${support.title}</h2>
		<p class="support-detail-writer">작성자 ID: ${support.writerId}</p>

		<div class="support-detail-divider"></div>

		<div class="support-detail-content">${support.content}</div>
	</section>
<c:if test="${not empty fileList}">
		<div class="admin-file-viewer" style="margin-top: 20px; padding: 15px; background: rgba(255, 255, 255, 0.05); border-radius: 8px;">
			<p style="color: #9ca3af; font-size: 13px; margin-bottom: 10px;">첨부파일</p>

			<div class="img-preview-grid"
				style="display: flex; flex-wrap: wrap; gap: 10px; justify-content:center">
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
			</c:if>
</div>

<%-- ── 답변 카드 ── --%>
	<section class="panel-card support-answer-card">
		<h2 class="panel-title">답변 내용</h2>

		<c:choose>
			<c:when test="${answer != null}">
				<div class="support-answer-meta">
					<span class="support-answer-admin">관리자 (ID:
						${answer.memberId})</span> <span class="support-answer-date"> <fmt:formatDate
							value="${answer.createdAt}" pattern="yyyy.MM.dd HH:mm" />
					</span>
				</div>
				<div class="support-answer-content">${answer.answerContent}</div>
			</c:when>
			<c:otherwise>
				<div class="support-answer-empty">
					<svg width="36" height="36" viewBox="0 0 24 24" fill="none"
						stroke="currentColor" stroke-width="1.5">
                        <circle cx="12" cy="12" r="10" />
                        <polyline points="12 6 12 12 16 14" />
                    </svg>
					<p>아직 등록된 답변이 없습니다.</p>
					<a
						href="${pageContext.request.contextPath}/admin/support/answer?supportNo=${support.supportNo}"
						class="detail-link-btn primary" style="margin-top: 12px;">답변하기</a>
				</div>
			</c:otherwise>
		</c:choose>
	</section>

<style>

.page-header-row {

    margin-bottom: 30px; /* 이 수치를 높일수록 간격이 더 벌어집니다 */
}

.admin-support-detail-page {
	max-width: 860px;
	margin: 0 auto;
	margin: 0 auto; /* 좌우 여백 자동 설정으로 중앙 정렬 */
	width: 100%; /* 부모 너비 안에서 100% 차지 */
}

.detail-back-link {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	font-size: 13px;
	color: var(--text-secondary, #9ca3af);
	text-decoration: none;
	margin-bottom: 10px;
	transition: color 0.15s;
}

.detail-back-link:hover {
	color: var(--text-primary, #f9fafb);
}

.detail-back-link svg {
	stroke: currentColor;
}

.support-detail-card {
	margin-bottom: 16px;
	margin-top: 50px;
}

.support-answer-card {
	margin-bottom: 16px;
}

.support-detail-meta-row {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 14px;
}

.support-detail-date {
	font-size: 12px;
	color: var(--text-secondary, #9ca3af);
	margin-left: auto;
}

.support-detail-title {
	font-size: 20px;
	font-weight: 700;
	color: var(--text-primary, #f9fafb);
	margin-bottom: 6px;
	letter-spacing: -0.01em;
}

.support-detail-writer {
	font-size: 13px;
	color: var(--text-secondary, #9ca3af);
	margin-bottom: 16px;
}

.support-detail-divider {
	border: none;
	border-top: 1px solid var(--border, rgba(255, 255, 255, 0.08));
	margin: 16px 0;
}

.support-detail-content {
	font-size: 14px;
	line-height: 1.75;
	color: var(--text-primary, #f9fafb);
	white-space: pre-wrap;
	padding: 16px 18px;
	background: rgba(255, 255, 255, 0.03);
	border: 1px solid var(--border, rgba(255, 255, 255, 0.08));
	border-radius: 8px;
}

.support-answer-meta {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 12px;
}

.support-answer-admin {
	font-size: 13px;
	font-weight: 600;
	color: var(--text-primary, #f9fafb);
}

.support-answer-date {
	font-size: 12px;
	color: var(--text-secondary, #9ca3af);
}

.support-answer-content {
	font-size: 14px;
	line-height: 1.75;
	color: var(--text-primary, #f9fafb);
	white-space: pre-wrap;
	padding: 16px 18px;
	background: rgba(255, 255, 255, 0.03);
	border: 1px solid var(--border, rgba(255, 255, 255, 0.08));
	border-radius: 8px;
}

.support-answer-empty {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 40px 0;
	gap: 8px;
	color: var(--text-secondary, #9ca3af);
	text-align: center;
}

.support-answer-empty svg {
	opacity: 0.4;
	margin-bottom: 4px;
}

.support-answer-empty p {
	font-size: 14px;
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

</style>
