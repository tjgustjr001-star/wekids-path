<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<div class="admin-teacher-detail-page">

	<button type="button" class="back-link-btn" onclick="location.href='${pageContext.request.contextPath}/admin/teachers';">
		← 교사 목록으로 돌아가기</button>

	<div class="teacher-detail-grid">
		<div class="teacher-detail-left">
			<section class="panel-card teacher-profile-card">
				<div class="teacher-profile-avatar"><i class="fa-solid fa-user-pen"></i></div>

				<h1 class="teacher-profile-name">
					${teacher.teacherName} <span>선생님</span>
				</h1>

				<span
					class="status-pill
                    ${teacher.accountStatus eq 'ACTIVE' ? 'active' :
                      (teacher.accountStatus eq 'DORMANT' ? 'dormant' :
                      (teacher.accountStatus eq 'SUSPENDED' ? 'suspended' : 'deleted'))}">
					<c:choose>
						<c:when test="${teacher.accountStatus eq 'ACTIVE'}">활성</c:when>
						<c:when test="${teacher.accountStatus eq 'DORMANT'}">휴면</c:when>
						<c:when test="${teacher.accountStatus eq 'SUSPENDED'}">정지</c:when>
						<c:when test="${teacher.accountStatus eq 'DELETED'}">탈퇴</c:when>
					</c:choose>
				</span>

				<div class="teacher-info-list">
					<div class="teacher-info-item">
						<strong>이메일</strong> <span>${teacher.email}</span>
					</div>

					<div class="teacher-info-item">
						<strong>연락처</strong> <span>${teacher.phone}</span>
					</div>

					<div class="teacher-info-item">
						<strong>생년월일</strong> <span><fmt:formatDate
								value="${teacher.birth}" pattern="yyyy.MM.dd" /></span>
					</div>

					<div class="teacher-info-item">
						<strong>가입일</strong> <span><fmt:formatDate
								value="${teacher.joinDate}" pattern="yyyy.MM.dd" /></span>
					</div>

					<div class="teacher-info-item">
						<strong>최근 로그인</strong> <span><fmt:formatDate
								value="${teacher.lastLoginAt}" pattern="yyyy.MM.dd HH:mm" /></span>
					</div>

					<div class="teacher-info-item">
						<strong>소개</strong> <span>${teacher.intro}</span>
					</div>
				</div>
			</section>

			<section class="panel-card teacher-action-card">
				<h2 class="panel-title">관리자 조치</h2>

				<div class="teacher-action-group">
					<form
						action="${pageContext.request.contextPath}/admin/teachers/status"
						method="post">
						<input type="hidden" name="teacherId" value="${teacher.teacherId}">
						<input type="hidden" name="accountStatus" value="ACTIVE">
						<input type="hidden" name="redirectPage" value="detail">
						<button type="submit" class="action-green-btn">계정 활성화</button>
					</form>

					<form
						action="${pageContext.request.contextPath}/admin/teachers/status"
						method="post">
						<input type="hidden" name="teacherId" value="${teacher.teacherId}">
						<input type="hidden" name="accountStatus" value="SUSPENDED">
						<input type="hidden" name="redirectPage" value="detail">
						<button type="submit" class="action-red-btn">계정 정지</button>
					</form>

					<button type="button" class="action-gray-btn">비밀번호 초기화</button>
				</div>
			</section>
		</div>

		<div class="teacher-detail-right">
			<section class="panel-card">
				<h2 class="panel-title">최근 한 달 활동 통계</h2>

				<div class="chart-wrap teacher-bar-chart-wrap">
					<canvas id="teacherActivityChart"
						data-labels='[<c:forEach var="item" items="${teacherActivityChart.labels}" varStatus="status">
             					   "${item}"<c:if test="${!status.last}">,</c:if>
          					  	   </c:forEach>]'
						data-assignment-counts='[<c:forEach var="item" items="${teacherActivityChart.assignmentCounts}" varStatus="status">
             								   ${item}<c:if test="${!status.last}">,</c:if>
          									  </c:forEach>]'
						data-feedback-counts='[<c:forEach var="item" items="${teacherActivityChart.feedbackCounts}" varStatus="status">
            							    ${item}<c:if test="${!status.last}">,</c:if>
         								   </c:forEach>]'>
   					 </canvas>
				</div>

				<div class="chart-legend-row">
					<div class="chart-legend-item">
						<span class="legend-dot blue"></span> <span>신규 과제 등록</span>
					</div>
					<div class="chart-legend-item">
						<span class="legend-dot green"></span> <span>학생 피드백 작성</span>
					</div>
				</div>
			</section>

			<section class="panel-card">
				<div class="panel-head-line">
					<h2 class="panel-title">운영 중인 클래스</h2>
					<span class="count-badge">${fn:length(classList)}개</span>
				</div>

				<div class="teacher-class-table-wrap">
					<table class="teacher-class-table">
						<thead>
							<tr>
								<th>코드</th>
								<th>클래스명</th>
								<th>학생 수</th>
								<th>상태</th>
								<th>시작일</th>
								<th>종료일</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${classList}" var="cls">
								<tr>
									<td>${cls.classId}</td>
									<td>${cls.className}</td>
									<td>${cls.studentCount}명</td>
									<td><span
										class="badge
									        ${cls.classStatus eq 'ACTIVE' ? 'active' :
									          (cls.classStatus eq 'ARCHIVED' ? 'archive' : 'blind')}">
											<c:choose>
												<c:when test="${cls.classStatus eq 'ACTIVE'}">활성</c:when>
												<c:when test="${cls.classStatus eq 'ARCHIVED'}">아카이브</c:when>
												<c:when test="${cls.classStatus eq 'BLINDED'}">블라인드</c:when>
												<c:otherwise>${cls.classStatus}</c:otherwise>
											</c:choose>
									</span></td>
									<td><fmt:formatDate value="${cls.startDate}"
											pattern="yyyy.MM.dd" /></td>
									<td><fmt:formatDate value="${cls.endDate}"
											pattern="yyyy.MM.dd" /></td>
								</tr>
							</c:forEach>

							<c:if test="${empty classList}">
								<tr>
									<td colspan="6" style="text-align: center; padding: 30px;">운영
										중인 클래스가 없습니다.</td>
								</tr>
							</c:if>
						</tbody>
					</table>
				</div>
			</section>
		</div>
	</div>
</div>