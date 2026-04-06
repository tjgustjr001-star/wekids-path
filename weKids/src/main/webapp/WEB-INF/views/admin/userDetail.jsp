<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<div class="admin-user-detail-page">

	<button type="button" class="back-link-btn"
		onclick="location.href='${pageContext.request.contextPath}/admin/users';">
		← 사용자 목록으로 돌아가기</button>

	<div class="user-detail-grid">
		<div class="user-detail-left">
			<section class="detail-panel-card user-profile-card">
				<div class="user-profile-top-row">
					<h2 class="panel-title no-margin">계정 정보</h2>

					<span class="role-pill
            ${user.roleCode eq 'STUDENT' ? 'student' : ''}
            ${user.roleCode eq 'PARENT' ? 'parent' : ''}
            ${user.roleCode eq 'TEACHER' ? 'teacher' : ''}
            ${user.roleCode ne 'STUDENT' and user.roleCode ne 'PARENT' and user.roleCode ne 'TEACHER' ? 'default' : ''}">
						<c:choose>
							<c:when test="${user.roleCode eq 'STUDENT'}">학생</c:when>
							<c:when test="${user.roleCode eq 'PARENT'}">학부모</c:when>
							<c:when test="${user.roleCode eq 'TEACHER'}">교사</c:when>
							<c:otherwise>사용자</c:otherwise>
						</c:choose>
					</span>
				</div>

				<div class="user-profile-summary">
					<div class="user-profile-avatar"
						${user.roleCode eq 'ROLE_STUDENT' or user.roleCode eq 'STUDENT' ? 'student' : ''}
						${user.roleCode eq 'ROLE_PARENT' or user.roleCode eq 'PARENT' ? 'parent' : ''}${user.roleCode eq 'ROLE_TEACHER' or user.roleCode eq 'TEACHER' ? 'teacher' : ''}">

						<c:choose>
							<c:when
								test="${user.roleCode eq 'ROLE_STUDENT' or user.roleCode eq 'STUDENT'}">
								<i class="fa-solid fa-user-graduate"></i>
							</c:when>
							<c:when
								test="${user.roleCode eq 'ROLE_PARENT' or user.roleCode eq 'PARENT'}">
								<i class="fa-solid fa-people-roof"></i>
							</c:when>
							<c:when
								test="${user.roleCode eq 'ROLE_TEACHER' or user.roleCode eq 'TEACHER'}">
								<i class="fa-solid fa-user-pen"></i>
							</c:when>
							<c:otherwise>
								<i class="fa-solid fa-user-astronaut"></i>
							</c:otherwise>
						</c:choose>
					</div>

					<div class="user-profile-main">
						<h1 class="user-profile-name">${user.name}</h1>
						<p class="user-profile-email">${user.email}</p>
					</div>
				</div>

				<div class="user-profile-info-list">
					<div class="user-profile-info-item">
						<span class="user-profile-label"> <i
							class="fa-solid fa-shield-halved"></i> 계정 상태
						</span> <span
							class="status-pill
                ${user.accountStatus eq 'ACTIVE' ? 'active' :
                  (user.accountStatus eq 'DORMANT' ? 'dormant' : 'suspended')}">
							<c:choose>
								<c:when test="${user.accountStatus eq 'ACTIVE'}">활성</c:when>
								<c:when test="${user.accountStatus eq 'DORMANT'}">휴면</c:when>
								<c:otherwise>정지</c:otherwise>
							</c:choose>
						</span>
					</div>

					<div class="user-profile-info-item">
						<span class="user-profile-label"> <i
							class="fa-regular fa-calendar"></i> 가입일
						</span> <span class="user-profile-value">${user.createdAt}</span>
					</div>

					<div class="user-profile-info-item ">
						<span class="user-profile-label"> <i
							class="fa-solid fa-link"></i> 소속/연결
						</span> <span class="user-profile-value user-profile-value-multi">${user.classInfo}</span>
					</div>

					<div class="user-profile-info-item">
						<span class="user-profile-label"> <i
							class="fa-regular fa-clock"></i> 최근 접속
						</span> <span class="user-profile-value">${user.lastLoginAt}</span>
					</div>

					<div class="user-profile-info-item">
						<span class="user-profile-label"> <i
							class="fa-regular fa-id-card"></i> 아이디
						</span> <span class="user-profile-value">${user.loginId}</span>
					</div>
				</div>
			</section>

			<section class="detail-panel-card teacher-action-card">
				<h2 class="panel-title">관리자 조치</h2>

				<div class="teacher-action-group">
					<form
						action="${pageContext.request.contextPath}/admin/users/status"
						method="post">
						<input type="hidden" name="memberId" value="${user.memberId}">
						<input type="hidden" name="accountStatus" value="ACTIVE">
						<input type="hidden" name="redirectPage" value="detail">
						<button type="submit" class="action-green-btn">계정 활성화</button>
					</form>

					<form
						action="${pageContext.request.contextPath}/admin/users/status"
						method="post">
						<input type="hidden" name="memberId" value="${user.memberId}">
						<input type="hidden" name="accountStatus" value="DORMANT">
						<input type="hidden" name="redirectPage" value="detail">
						<button type="submit" class="action-gray-btn">휴면 처리</button>
					</form>

					<form
						action="${pageContext.request.contextPath}/admin/users/status"
						method="post">
						<input type="hidden" name="memberId" value="${user.memberId}">
						<input type="hidden" name="accountStatus" value="SUSPENDED">
						<input type="hidden" name="redirectPage" value="detail">
						<button type="submit" class="action-red-btn">계정 정지</button>
					</form>

					<button type="button" class="action-gray-btn">비밀번호 초기화</button>
				</div>
			</section>
		</div>

		<div class="user-detail-right">
			<section class="detail-panel-card">
				<h2 class="panel-title">기본 정보</h2>
				<div class="detail-info-grid">
					<div class="detail-box">
						<p>소속</p>
						<p class="c">${user.classInfo}</p>
					</div>
					<div class="detail-box">
						<p>연결</p>
						<p class="c">${user.connectionStatus}</p>
					</div>
				</div>
			</section>

			<section class="detail-panel-card">
				<h2 class="panel-title">주간 활동량 추이</h2>
				<div class="chart-wrap">
					<canvas id="userWeeklyLoginChart"></canvas>
				</div>
			</section>

			<section class="detail-panel-card">
				<h2 class="panel-title">최근 접속 환경</h2>
				<div class="detail-info-grid">
					<div class="detail-box">
						<p>기기</p>
						<p class="c">${empty user.deviceInfo ? '-' : user.deviceInfo}</p>
					</div>
					<div class="detail-box">
						<p>접속 위치</p>
						<p class="c">${empty user.accessLocation ? '-' : user.accessLocation}</p>
					</div>
				</div>
			</section>

			<section class="detail-panel-card">
				<h2 class="panel-title">최근 활동 로그</h2>
				<div class="monitor-list">
					<div class="monitor-item">
						<div class="monitor-icon info"></div>
						<div class="monitor-body">
							<p>최근 로그인 시도가 확인되었습니다.</p>
							<span>10분 전</span>
						</div>
					</div>
					<div class="monitor-item">
						<div class="monitor-icon warning"></div>
						<div class="monitor-body">
							<p>비밀번호 변경 요청 이력이 있습니다.</p>
							<span>어제</span>
						</div>
					</div>
					<div class="monitor-item">
						<div class="monitor-icon info"></div>
						<div class="monitor-body">
							<p>계정 상태 점검이 수행되었습니다.</p>
							<span>3일 전</span>
						</div>
					</div>
				</div>
			</section>
		</div>
	</div>
</div>
<script>
    const userWeeklyLoginLabels = [
        <c:forEach var="item" items="${weeklyLoginTrend}" varStatus="status">
            '${item.dayLabel}'<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const userWeeklyLoginData = [
        <c:forEach var="item" items="${weeklyLoginTrend}" varStatus="status">
            ${item.loginCount}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];
</script>