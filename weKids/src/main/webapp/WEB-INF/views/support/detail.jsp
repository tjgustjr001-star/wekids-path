<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>문의 상세 – We-Kids</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/detail.css">
</head>
<body>



	<!-- ════════ MAIN ════════ -->
	<main class="main-wrap">

		<!-- 뒤로가기 -->
		<a href="${pageContext.request.contextPath}/support/list"
			class="back-link"> <svg viewBox="0 0 24 24">
				<polyline points="15 18 9 12 15 6" /></svg> 목록으로 돌아가기
		</a>

		<!-- 문의 + 답변 카드 (하나의 카드로 통합) -->
		<div class="detail-card">

			<!-- 문의 헤더 -->
			<div class="badge-row">
				<span
					class="status-badge ${support.status eq '답변완료' ? 'done' : 'waiting'}">
					${support.status eq '답변완료' ? '답변 완료' : '답변 대기'} </span> <span
					class="category-badge">${support.category}</span>
			</div>

			<h2 class="detail-title">${support.title}</h2>

			<div class="detail-meta">
				<svg viewBox="0 0 24 24">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
            </svg>
				<span>작성자</span> <span class="dot">•</span>
				<fmt:formatDate value="${support.createdAt}" pattern="yyyy.MM.dd" />
			</div>

			<div class="divider"></div>

			<!-- 문의 본문 -->
			<div class="detail-content">${support.content}</div>

			<div class="divider"></div>

			<!-- 답변 영역 -->
			<div class="answer-section">
				<c:choose>
					<c:when test="${answer != null}">
						<div class="answer-header">
							<svg viewBox="0 0 24 24">
                            <path
									d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                        </svg>
							<span>답변</span> <span class="answer-date"> <fmt:formatDate
									value="${answer.createdAt}" pattern="yyyy.MM.dd" />
							</span>
						</div>
						<div class="answer-content">${answer.answerContent}</div>
					</c:when>
					<c:otherwise>
						<div class="answer-waiting">
							<svg viewBox="0 0 24 24">
                            <circle cx="12" cy="12" r="10" />
                            <polyline points="12 6 12 12 16 14" />
                        </svg>
							<p class="waiting-title">답변을 기다리고 있습니다.</p>
							<p class="waiting-sub">담당자가 내용을 확인한 후 신속하게 답변해 드리겠습니다.</p>
						</div>
					</c:otherwise>
				</c:choose>
			</div>

		</div>

		<!-- 삭제 버튼 (답변 전에만 표시) -->
		<c:if test="${support.status ne '답변완료'}">
			<div class="btn-row">
				<a
					href="${pageContext.request.contextPath}/support/remove?supportNo=${support.supportNo}"
					class="btn-remove" onclick="return confirm('문의를 삭제하시겠습니까?')">
					삭제 </a>
			</div>
		</c:if>

	</main>

</body>
</html>

<style>
/* ===========================
   We-Kids – detail.css
   (JSP 클래스명 기준)
   =========================== */

/* --- Reset & Base --- */
*, *::before, *::after {
	box-sizing: border-box;
	margin: 0;
	padding: 0;
}

html {
	background-color: #f0f2f4;
	min-height: 100%;
}

/* ===========================
   MAIN WRAP
   =========================== */
.main-wrap {
	flex: 1;
	padding: 32px 40px;
	min-height: calc(100vh - 56px);
	width: calc(100% - 210px);
	display: flex;
	flex-direction: column;
	align-items: center;
}

/* ===========================
   BACK LINK
   =========================== */
.back-link {
	display: inline-flex;
	align-items: center;
	gap: 6px;
	font-size: 14px;
	color: #5a8060;
	text-decoration: none;
	margin-bottom: 20px;
	transition: color 0.15s;
	max-width: 860px;
	margin-left: auto;
	margin-right: auto;
	width: 100%;
}

.back-link:hover {
	color: #2a6e3f;
}

.back-link svg {
	width: 16px;
	height: 16px;
	flex-shrink: 0;
	stroke: currentColor;
	fill: none;
	stroke-width: 2;
	stroke-linecap: round;
	stroke-linejoin: round;
	overflow: visible;
}

/* ===========================
   DETAIL CARD
   =========================== */
.detail-card {
	background-color: #FFFFFF;
	border-radius: 14px;
	padding: 32px 40px;
	box-shadow: 0 1px 6px rgba(42, 110, 63, 0.07);
	margin-bottom: 16px;
	max-width: 860px; /* ← 추가 */ width : 100%; /* ← 추가 */
	margin-left: auto;
	margin-right: auto;
	width: 100%;
}

/* 뱃지 행 */
.badge-row {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 14px;
}

/* 상태 뱃지 */
.status-badge {
	display: inline-flex;
	align-items: center;
	gap: 5px;
	padding: 4px 10px;
	border-radius: 20px;
	font-size: 12px;
	font-weight: 600;
}

.status-badge::before {
	content: '';
	width: 7px;
	height: 7px;
	border-radius: 50%;
	flex-shrink: 0;
}

.status-badge.waiting {
	background-color: #fff4e0;
	color: #b07200;
	border: 1px solid #f5d98b;
}

.status-badge.waiting::before {
	background-color: #f0a500;
}

.status-badge.done {
	background-color: #e6f4ea;
	color: #2a6e3f;
	border: 1px solid #a8d5b5;
}

.status-badge.done::before {
	background-color: #34a853;
}

/* 카테고리 뱃지 */
.category-badge {
	display: inline-flex;
	align-items: center;
	padding: 4px 10px;
	border-radius: 20px;
	font-size: 12px;
	font-weight: 500;
	background-color: #eef2ee;
	color: #4a6a4e;
	border: 1px solid #d4e3d6;
}

/* 제목 */
.detail-title {
	font-size: 22px;
	font-weight: 700;
	color: #1a2a1e;
	line-height: 1.4;
	margin-bottom: 10px;
}

/* 메타 (작성자·날짜) */
.detail-meta {
	display: flex;
	align-items: center;
	gap: 6px;
	font-size: 13px;
	color: #8aaa8e;
	margin-bottom: 4px;
}

.detail-meta svg {
	width: 14px;
	height: 14px;
	flex-shrink: 0;
	stroke: currentColor;
	fill: none;
	stroke-width: 2;
	stroke-linecap: round;
	stroke-linejoin: round;
	overflow: visible;
}

.detail-meta .dot {
	color: #c0d4c4;
}

/* 구분선 */
.divider {
	border: none;
	border-top: 1px solid #edf2ee;
	margin: 20px 0;
}

/* 문의 내용 */
.detail-content {
	font-size: 15px;
	line-height: 1.75;
	color: #3a4e3e;
	white-space: pre-wrap;
}

/* ===========================
   ANSWER SECTION
   =========================== */

/* 답변 대기 */
.answer-waiting {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 32px 0;
	gap: 10px;
	text-align: center;
}

.answer-waiting svg {
	width: 40px;
	height: 40px;
	color: #b0c8b4;
	stroke: currentColor;
	fill: none;
	stroke-width: 1.5;
	stroke-linecap: round;
	stroke-linejoin: round;
	overflow: visible;
	margin-bottom: 4px;
}

.waiting-title {
	font-size: 15px;
	font-weight: 600;
	color: #3a4e3e;
}

.waiting-sub {
	font-size: 13px;
	color: #8aaa8e;
	line-height: 1.6;
}

/* 답변 완료 */
.answer-header {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 14px;
	font-size: 14px;
	font-weight: 700;
	color: #2a6e3f;
}

.answer-header svg {
	width: 16px;
	height: 16px;
	flex-shrink: 0;
	stroke: currentColor;
	fill: none;
	stroke-width: 2;
	stroke-linecap: round;
	stroke-linejoin: round;
	overflow: visible;
}

.answer-date {
	font-size: 12px;
	font-weight: 400;
	color: #8aaa8e;
}

.answer-content {
	font-size: 15px;
	line-height: 1.75;
	color: #3a4e3e;
	white-space: pre-wrap;
	background-color: #f7fbf8;
	border: 1px solid #d4e8da;
	border-radius: 10px;
	padding: 18px 20px;
}

/* ===========================
   DELETE BUTTON ROW
   =========================== */
.btn-row {
	display: flex;
	justify-content: flex-end;
	max-width: 860px;
	margin-left: auto;
	margin-right: auto;
}

.btn-remove {
	display: inline-flex;
	align-items: center;
	padding: 9px 20px;
	font-size: 14px;
	font-weight: 600;
	color: #c0392b;
	background-color: #fff0ef;
	border: 1px solid #f5c0bb;
	border-radius: 8px;
	text-decoration: none;
	cursor: pointer;
	transition: background-color 0.15s, color 0.15s;
}

.btn-remove:hover {
	background-color: #fde0de;
	color: #a93226;
}

/* ===========================
   RESPONSIVE
   =========================== */
@media ( max-width : 768px) {
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
	}
	.detail-card {
		padding: 20px 18px;
		border-radius: 10px;
	}
	.detail-title {
		font-size: 18px;
	}
}
</style>
