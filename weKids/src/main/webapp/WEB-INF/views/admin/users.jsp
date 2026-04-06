<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/admin/stats.css">

<c:set var="studentCount" value="0" />
<c:set var="parentCount" value="0" />
<c:set var="teacherCount" value="0" />

<c:forEach items="${userList}" var="user">
	<c:choose>
		<c:when test="${user.roleCode eq 'STUDENT'}">
			<c:set var="studentCount" value="${studentCount + 1}" />
		</c:when>
		<c:when test="${user.roleCode eq 'PARENT'}">
			<c:set var="parentCount" value="${parentCount + 1}" />
		</c:when>
		<c:when test="${user.roleCode eq 'TEACHER'}">
			<c:set var="teacherCount" value="${teacherCount + 1}" />
		</c:when>
		<c:when test="${user.roleCode eq 'TOTAL'}">
			<c:set var="teacherCount" value="${totalUserCount + 1}" />
		</c:when>
	</c:choose>
</c:forEach>

<c:set var="totalUserCount" value="${fn:length(userList)}" />

<input type="hidden" id="studentUserCount" value="${studentCount}">
<input type="hidden" id="parentUserCount" value="${parentCount}">
<input type="hidden" id="teacherUserCount" value="${teacherCount}">
<input type="hidden" id="totalUserCount" value="${totalUserCount}">

<div class="admin-users-page">

	<div class="page-header-row">
		<div>
			<h1 class="page-title">사용자 관리</h1>
			<p class="page-desc">사용자 계정을 조회하고 상태를 관리합니다.</p>
		</div>
		<div class="page-header-actions">
			<button type="button" class="primary-btn"
				id="openCreateStudentModalBtn">학생 계정 생성</button>
		</div>
	</div>

	<div class="users-top-grid">
		<section class="panel-card wide">
			<h2 class="panel-title">사용자 역할 분포</h2>
			<div class="chart-wrap">
				<canvas id="userRoleChart"></canvas>

			</div>
			<div class="user-stat-box-a">
				<div class="user-stat-row-a">
					<span class="admin-stats-legend-dot-a student">학생
						${studentCount}명</span>
				</div>
				<div class="user-stat-row-a">
					<span class="admin-stats-legend-dot-a parent">학부모
						${parentCount}명</span>
				</div>
				<div class="user-stat-row-a">
					<span class="admin-stats-legend-dot-a teacher">교사
						${teacherCount}명</span>
				</div>
				<div class="user-stat-row-a">
					<span class="admin-stats-legend-dot-a total">전체
						${totalUserCount}명</span>
				</div>
			</div>
		</section>


		<section class="panel-card wide">
			<h2 class="panel-title">사용자별 접속 추이(최근 7일)</h2>
			<div class="chart-wrap">
				<canvas id="userRoleLoginTrendChart"></canvas>
			</div>
		</section>
	</div>

	<section class="users-board">
		<div class="users-board-top">
			<div class="tab-group">
				<button type="button" class="user-tab-btn active" data-filter="전체">전체</button>
				<button type="button" class="user-tab-btn" data-filter="STUDENT">학생</button>
				<button type="button" class="user-tab-btn" data-filter="PARENT">학부모</button>
				<button type="button" class="user-tab-btn" data-filter="TEACHER">교사</button>
			</div>

			<div class="search-box">
				<input type="text" id="userSearchInput"
					placeholder="이름, 아이디, 이메일 검색">
			</div>
		</div>

		<div class="user-table-wrap">
			<table class="user-table" id="userTable">
				<thead>
					<tr>
						<th>이름</th>
						<th>아이디</th>
						<th>이메일</th>
						<th>역할</th>
						<th>연결</th>
						<th>최근 접속</th>
						<th>상태</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${userList}" var="user">
						<tr class="user-row" data-role="${user.roleCode}"
							data-name="${user.name}" data-login-id="${user.loginId}"
							data-email="${user.email}">

							<td class="user-name-cell">${user.name}</td>
							<td>${user.loginId}</td>
							<td>${user.email}</td>

							<td><span
								class="role-pill ${user.roleCode eq 'STUDENT' ? 'student' : ''}
						${user.roleCode eq 'PARENT' ? 'parent' : ''}${user.roleCode eq 'TEACHER' ? 'teacher' : ''}
">
									<c:choose>
										<c:when test="${user.roleCode eq 'STUDENT'}">학생</c:when>
										<c:when test="${user.roleCode eq 'PARENT'}">학부모</c:when>
										<c:otherwise>교사</c:otherwise>
									</c:choose>
							</span></td>

							<td>${user.connectionStatus}</td>
							<td>${user.lastLoginAt}</td>

							<td><span
								class="status-pill
                                    ${user.accountStatus eq 'ACTIVE' ? 'active' :
                                      (user.accountStatus eq 'DORMANT' ? 'dormant' : 'suspended')}">
									<c:choose>
										<c:when test="${user.accountStatus eq 'ACTIVE'}">활성</c:when>
										<c:when test="${user.accountStatus eq 'DORMANT'}">휴면</c:when>
										<c:otherwise>정지</c:otherwise>
									</c:choose>
							</span></td>

							<td class="manage-cell">
								<div class="table-action-wrap">
									<c:choose>
										<c:when test="${user.roleCode eq 'TEACHER'}">
											<a
												href="${pageContext.request.contextPath}/admin/teachers/${user.memberId}"
												class="detail-link-btn"> 상세보기 </a>
										</c:when>
										<c:otherwise>
											<a
												href="${pageContext.request.contextPath}/admin/users/${user.memberId}"
												class="detail-link-btn"> 상세보기 </a>
										</c:otherwise>
									</c:choose>
									<button type="button" class="row-menu-toggle-btn">⋮</button>

									<div class="table-row-menu">
										<form
											action="${pageContext.request.contextPath}/admin/users/status"
											method="post">
											<input type="hidden" name="memberId" value="${user.memberId}">
											<input type="hidden" name="accountStatus" value="ACTIVE">
											<button type="submit" class="teacher-status-action">계정
												활성화</button>
										</form>

										<form
											action="${pageContext.request.contextPath}/admin/users/status"
											method="post">
											<input type="hidden" name="memberId" value="${user.memberId}">
											<input type="hidden" name="accountStatus" value="DORMANT">
											<button type="submit" class="teacher-status-action">휴면
												처리</button>
										</form>

										<form
											action="${pageContext.request.contextPath}/admin/users/status"
											method="post">
											<input type="hidden" name="memberId" value="${user.memberId}">
											<input type="hidden" name="accountStatus" value="SUSPENDED">
											<button type="submit" class="teacher-status-action danger">계정
												정지</button>
										</form>
									</div>
								</div>
							</td>
						</tr>
					</c:forEach>

					<c:if test="${empty userList}">
						<tr>
							<td colspan="8" style="text-align: center; padding: 30px;">조회된
								사용자 정보가 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>

		<div class="users-board-footer">
			<span>총 <span id="userCountText">${totalUserCount}</span>명
			</span>
		</div>
	</section>
</div>
<div class="teacher-modal" id="createStudentModal">
	<div class="teacher-modal-dialog">
		<div class="teacher-modal-header">
			<h3>학생 계정 생성</h3>
			<button type="button" class="close-modal-btn"
				id="closeCreateStudentModalBtn">×</button>
		</div>

		<form id="createStudentForm" class="teacher-modal-form"
			action="${pageContext.request.contextPath}/admin/users/students/regist"
			method="post">

			<div>
				<label>학생 이름 <span class="required-mark">*</span></label> <input
					type="text" name="studentName" id="studentNameInput"
					placeholder="예: 김학생">
			</div>

			<div>
				<label>로그인 아이디 <span class="required-mark">*</span></label> <input
					type="text" name="loginId" id="studentLoginIdInput"
					placeholder="예: s006">
			</div>

			<div>
				<label>이메일 <span class="required-mark">*</span></label> <input
					type="email" name="email" id="studentEmailInput"
					placeholder="student@daewoo.edu.kr">
			</div>

			<div>
				<label>초기 비밀번호 <span class="required-mark">*</span></label> <input
					type="text" name="initialPassword" id="studentPasswordInput"
					placeholder="초기 비밀번호 입력">
			</div>

			<div class="teacher-modal-actions">
				<button type="button" class="modal-cancel-btn"
					id="cancelCreateStudentBtn">취소</button>
				<button type="submit" class="modal-submit-btn">생성하기</button>
			</div>
		</form>
	</div>
</div>
<script>
    const userRoleLoginLabels = [
        <c:forEach var="item" items="${userRoleLoginTrend}" varStatus="st">
            '${item.dayLabel}'<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    const studentLoginTrendData = [
        <c:forEach var="item" items="${userRoleLoginTrend}" varStatus="st">
            ${item.studentCount}<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    const parentLoginTrendData = [
        <c:forEach var="item" items="${userRoleLoginTrend}" varStatus="st">
            ${item.parentCount}<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    const teacherLoginTrendData = [
        <c:forEach var="item" items="${userRoleLoginTrend}" varStatus="st">
            ${item.teacherCount}<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];
</script>