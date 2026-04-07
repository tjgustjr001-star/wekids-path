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
	</c:choose>
</c:forEach>

<c:set var="totalUserCount"
	value="${studentCount + parentCount + teacherCount}" />

<input type="hidden" id="totalUserCount" value="${totalUserCount}" />
<input type="hidden" id="studentUserCount" value="${studentCount}" />
<input type="hidden" id="parentUserCount" value="${parentCount}" />
<input type="hidden" id="teacherUserCount" value="${teacherCount}" />
<input type="hidden" id="inspectionNeedCount"
	value="${inspectionNeedCount}" />

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
					<span class="admin-stats-legend-dot-a total">전체
						${totalUserCount}명</span>
				</div>
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
					<span class="admin-stats-legend-dot-a insepect">점검 필요
						${inspectionNeedCount}명</span>
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

        <div class="page-header-actions">
            <div class="search-box">
                <input type="text" id="userSearchInput" placeholder="이름, 로그인 ID, 이메일 검색" />
            </div>
        </div>
    </div>

    <div class="user-table-wrap">
        <table class="user-table">
            <thead>
                <tr>
                    <th>이름</th>
                    <th>로그인 ID</th>
                    <th>이메일</th>
                    <th>역할</th>
                    <th>연결 상태</th>
                    <th>최근 로그인</th>
                    <th>계정 상태</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty userList}">
                        <c:forEach var="user" items="${userList}">
                            <tr class="user-row"
                                data-role="${user.roleCode}"
                                data-name="${fn:escapeXml(user.name)}"
                                data-login-id="${fn:escapeXml(user.loginId)}"
                                data-email="${fn:escapeXml(user.email)}">

                                <td class="user-name-cell">
                                    <c:choose>
                                        <c:when test="${user.roleCode eq 'TEACHER'}">
                                            <a href="${pageContext.request.contextPath}/admin/teachers/${user.memberId}">
                                                ${user.name}
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/admin/users/${user.memberId}">
                                                ${user.name}
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>${user.loginId}</td>
                                <td>${user.email}</td>

                                <td>
                                    <span class="role-pill
                                        <c:choose>
                                            <c:when test='${user.roleCode eq "STUDENT"}'>student</c:when>
                                            <c:when test='${user.roleCode eq "PARENT"}'>parent</c:when>
                                            <c:when test='${user.roleCode eq "TEACHER"}'>teacher</c:when>
                                            <c:otherwise>default</c:otherwise>
                                        </c:choose>">
                                        ${user.roleName}
                                    </span>
                                </td>

                                <td>${user.connectionStatus}</td>
                                <td>${user.lastLoginAt}</td>

                                <td>
                                    <span class="status-pill
                                        <c:choose>
                                            <c:when test='${user.accountStatus eq "ACTIVE"}'>active</c:when>
                                            <c:when test='${user.accountStatus eq "ARCHIVED"}'>archive</c:when>
                                            <c:when test='${user.accountStatus eq "BLINDED"}'>blind</c:when>
                                            <c:when test='${user.accountStatus eq "DORMANT"}'>dormant</c:when>
                                            <c:when test='${user.accountStatus eq "SUSPENDED"}'>suspended</c:when>
                                            <c:otherwise>deleted</c:otherwise>
                                        </c:choose>">
                                        <c:choose>
                                            <c:when test="${user.accountStatus eq 'ACTIVE'}">활성</c:when>
                                            <c:when test="${user.accountStatus eq 'ARCHIVED'}">아카이브</c:when>
                                            <c:when test="${user.accountStatus eq 'BLINDED'}">블라인드</c:when>
                                            <c:when test="${user.accountStatus eq 'DORMANT'}">휴면</c:when>
                                            <c:when test="${user.accountStatus eq 'SUSPENDED'}">정지</c:when>
                                            <c:otherwise>탈퇴</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>

                                <td>
                                    <div class="table-action-wrap">
                                        <c:choose>
                                            <c:when test="${user.roleCode eq 'TEACHER'}">
                                                <a href="${pageContext.request.contextPath}/admin/teachers/${user.memberId}"
                                                   >
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/admin/users/${user.memberId}"
                                                   >
                                                </a>
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="menu-wrap">
                                            <button type="button" class="row-menu-toggle-btn">⋯</button>

                                            <div class="table-row-menu">
                                                <form action="${pageContext.request.contextPath}/admin/users/status" method="post">
                                                    <input type="hidden" name="memberId" value="${user.memberId}" />
                                                    <input type="hidden" name="redirectPage" value="list" />
                                                    <input type="hidden" name="accountStatus" value="ACTIVE" />
                                                    <button type="submit" class="menu-action">계정 활성화</button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/admin/users/status" method="post">
                                                    <input type="hidden" name="memberId" value="${user.memberId}" />
                                                    <input type="hidden" name="redirectPage" value="list" />
                                                    <input type="hidden" name="accountStatus" value="DORMANT" />
                                                    <button type="submit" class="menu-action">휴면 처리</button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/admin/users/status" method="post">
                                                    <input type="hidden" name="memberId" value="${user.memberId}" />
                                                    <input type="hidden" name="redirectPage" value="list" />
                                                    <input type="hidden" name="accountStatus" value="SUSPENDED" />
                                                    <button type="submit" class="menu-action danger">계정 정지</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="empty-cell">조회된 사용자가 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="users-board-footer">
        총 <strong id="userCountText">${fn:length(userList)}</strong>명의 사용자가 조회되었습니다.
    </div>
</section>

<!-- 점검 필요 계정 모달 -->
<div class="class-detail-modal" id="inspectionModal">
	<div class="class-detail-dialog inspection-dialog">
		<div class="class-detail-header">
			<h3>점검 필요 계정 리스트</h3>
			<button type="button" class="close-modal-btn"
				id="closeInspectionModalBtn">&times;</button>
		</div>

		<div class="class-detail-body">
			<p class="panel-subtext">점검이 필요한 계정입니다.</p>

			<div class="table-wrap">
				<table class="inspection-table">
					<thead>
						<tr>
							<th>회원번호</th>
							<th>로그인 ID</th>
							<th>이메일</th>
							<th>상태</th>
							<th>최근 로그인</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty inspectionNeedList}">
								<c:forEach var="item" items="${inspectionNeedList}">
									<tr>
										<td>${item.memberId}</td>
										<td>${item.loginId}</td>
										<td>${item.email}</td>
										<td>${item.accountStatus}</td>
										<td>${item.lastLoginAt}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="5" class="empty-cell">점검 필요 계정이 없습니다.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>

			<div class="detail-action-row">
				<button type="button" class="modal-action-btn active-btn"
					id="inspectionModalCloseBtn">닫기</button>
			</div>
		</div>
	</div>
</div>

<!-- 학생 계정 생성 모달 -->
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