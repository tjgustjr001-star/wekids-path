<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<div class="dashboard-page">
	<div class="page-header-row">
		<div>
			<h1 class="page-title">관리자 홈</h1>
			<p class="page-desc">플랫폼 전체의 현황을 모니터링하고 관리하세요.</p>
		</div>
	</div>

	<div class="stats-grid-a">
		<div class="stat-card">
			<div class="stat-top">
				<div class="icon-box blue">
					<i class="fa-solid fa-user-astronaut"></i>
				</div>

			</div>
			<div class="stat-label">전체 사용자</div>
			<div class="stat-value">
				<span id="userCountText">${totalUserCount}</span>
			</div>
		</div>

		<div class="stat-card">
			<div class="stat-top">
				<div class="icon-box green">
					<i class="fa-solid fa-book-open"></i>
				</div>

			</div>
			<div class="stat-label">활성 클래스</div>
			<div class="stat-value">
				<span id="activeClassCountText">${activeClassCount}</span>
			</div>
		</div>

		<div class="stat-card">
			<div class="stat-top">
				<div class="icon-box pink">
					<i class="fa-solid fa-user-gear"></i>
				</div>

			</div>
			<div class="stat-label">신규 가입 교사</div>
			<div class="stat-value">
				<span id="newTeacherCountText">${newTeacherCount}</span>
			</div>
		</div>

		<div class="stat-card">
			<div class="stat-top">
				<div class="icon-box gray">
					<i class="fa-regular fa-hourglass-half"></i>
				</div>
			</div>
			<div class="stat-label">미해결 문의</div>
			<div class="stat-value">
				<span class="support-stat-value-b">${pendingSupportCount} </span>
			</div>
		</div>
	</div>

	<div class="dashboard-bottom">
		<section class="chart-panel">
			<div class="panel-header">
				<h2>주간 접속 트렌드</h2>
			</div>
			<div class="chart-box">
				<canvas id="weeklyTrendChart"></canvas>
			</div>
		</section>

		<section class="alert-panel">
			<h2>시스템 알림</h2>

			<div class="alert-item">
				<span class="dot info"></span>
				<div>
					<div class="alert-title">DB 백업 완료</div>
					<div class="alert-time">10분 전</div>
				</div>
			</div>

			<div class="alert-item">
				<span class="dot warning"></span>
				<div>
					<div class="alert-title">API 응답 지연 발생</div>
					<div class="alert-time">1시간 전</div>
				</div>
			</div>

			<div class="alert-item">
				<span class="dot info"></span>
				<div>
					<div class="alert-title">신규 관리자 로그인</div>
					<div class="alert-time">3시간 전</div>
				</div>
			</div>

			<div class="alert-item no-border">
				<span class="dot danger"></span>
				<div>
					<div class="alert-title">서버 리소스 사용량 80% 초과</div>
					<div class="alert-time">5시간 전</div>
				</div>
			</div>
		</section>
	</div>
</div>
<script>
    const weeklyTrendLabels = [
        <c:forEach var="item" items="${weeklyLoginTrend}" varStatus="status">
            '${item.dayLabel}'<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const weeklyTrendData = [
        <c:forEach var="item" items="${weeklyLoginTrend}" varStatus="status">
            ${item.loginCount}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
</script>
