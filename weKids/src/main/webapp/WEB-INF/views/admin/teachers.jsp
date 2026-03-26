<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="activeCount" value="0" />
<c:set var="dormantCount" value="0" />
<c:set var="suspendedDeletedCount" value="0" />

<c:forEach items="${teacherList}" var="teacher">
	<c:choose>
		<c:when test="${teacher.accountStatus eq 'ACTIVE'}">
			<c:set var="activeCount" value="${activeCount + 1}" />
		</c:when>
		<c:when test="${teacher.accountStatus eq 'DORMANT'}">
			<c:set var="dormantCount" value="${dormantCount + 1}" />
		</c:when>
		<c:when test="${teacher.accountStatus eq 'SUSPENDED'}">
			<c:set var="suspendedDeletedCount" value="${suspendedDeletedCount + 1}" />
		</c:when>
	</c:choose>
</c:forEach>

<c:set var="totalTeacherCount" value="${fn:length(teacherList)}" />

<div class="admin-teachers-page">

    <div class="page-header-row">
        <div>
            <h1 class="page-title">교사 관리</h1>
            <p class="page-desc">플랫폼에 등록된 교사 계정을 관리하고 권한을 설정합니다.</p>
        </div>

        <div class="page-header-actions">
            <button type="button" class="outline-btn" id="downloadTeacherListBtn">목록 다운로드</button>
            <button type="button" class="primary-btn" id="openAddTeacherModalBtn">교사 계정 생성</button>
        </div>
    </div>

    <div class="teachers-top-grid">

        <!-- 전체 교사 현황 -->
        <section class="panel-card">
            <h2 class="panel-title">전체 교사 현황</h2>
            <div class="teacher-total-count">${totalTeacherCount}<span>명</span></div>

            <div class="teacher-stat-group">
                <div class="teacher-stat-row">
                    <span>활성 계정</span>
                    <strong>${activeCount}명</strong>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill green"
                         style="width:${totalTeacherCount gt 0 ? (activeCount * 100.0 / totalTeacherCount) : 0}%;"></div>
                </div>

                <div class="teacher-stat-row top-gap">
                    <span>휴면 계정</span>
                    <strong>${dormantCount}명</strong>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill yellow"
                         style="width:${totalTeacherCount gt 0 ? (dormantCount * 100.0 / totalTeacherCount) : 0}%;"></div>
                </div>

                <div class="teacher-stat-row top-gap">
                    <span>정지 계정</span>
                    <strong>${suspendedDeletedCount}명</strong>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill red"
                         style="width:${totalTeacherCount gt 0 ? (suspendedDeletedCount * 100.0 / totalTeacherCount) : 0}%;"></div>
                </div>
            </div>
        </section>

        <!-- 가입 추이 -->
        <section class="panel-card wide">
            <div class="panel-head-line">
                <h2 class="panel-title">신규 가입 추이 (최근 6개월)</h2>
            </div>

            <div class="chart-wrap">
                <canvas id="teacherTrendChart"></canvas>
            </div>
        </section>
    </div>

    <section class="teachers-board">
        <div class="teachers-board-top">
            <div class="teacher-filter-row">
                <select id="teacherStatusFilter" class="dark-select">
                    <option value="전체">전체 상태</option>
                    <option value="ACTIVE">활성</option>
                    <option value="DORMANT">휴면</option>
                    <option value="SUSPENDED">정지</option>
                </select>

                <!-- <select id="teacherSchoolFilter" class="dark-select">
                    <option value="전체">학교 전체</option>
                    <option value="-">미지정</option>
                </select> -->
            </div>

            <div class="search-box">
                <input type="text" id="teacherSearchInput" placeholder="이름, 이메일, 학교명 검색">
            </div>
        </div>

        <div class="teacher-table-wrap">
            <table class="teacher-table" id="teacherTable">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>소속 학교</th>
                        <th>운영 클래스</th>
                        <th>가입일</th>
                        <th>상태</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${teacherList}" var="teacher">
                        <tr class="teacher-row"
                            data-id="${teacher.teacherId}"
                            data-name="${teacher.teacherName}"
                            data-email="${teacher.email}"
                            data-school="${teacher.schoolName}"
                            data-status="${teacher.accountStatus}">

                            <td class="teacher-name-cell">${teacher.teacherName}</td>
                            <td>${teacher.email}</td>
                            <td>${teacher.schoolName}</td>
                            <td>${teacher.classCount}개</td>
                            <td>
                                <fmt:formatDate value="${teacher.joinDate}" pattern="yyyy.MM.dd"/>
                            </td>
                            <td>
                                <span class="status-pill
								    ${teacher.accountStatus eq 'ACTIVE' ? 'active' :
								      (teacher.accountStatus eq 'DORMANT' ? 'dormant' :
								      (teacher.accountStatus eq 'SUSPENDED' ? 'suspended' : 'deleted'))}">
                                    <c:choose>
                                        <c:when test="${teacher.accountStatus eq 'ACTIVE'}">활성</c:when>
                                        <c:when test="${teacher.accountStatus eq 'DORMANT'}">휴면</c:when>
                                        <c:when test="${teacher.accountStatus eq 'SUSPENDED'}">정지</c:when>
                                        <c:when test="${teacher.accountStatus eq 'DELETED'}">탈퇴</c:when>
                                        <c:otherwise>${teacher.accountStatus}</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td class="manage-cell">
                                <div class="table-action-wrap">
                                    <a href="${pageContext.request.contextPath}/admin/teachers/${teacher.teacherId}" class="detail-link-btn">상세 보기</a>
                                    <button type="button" class="row-menu-toggle-btn">⋮</button>

                                    <div class="table-row-menu">
									    <form action="${pageContext.request.contextPath}/admin/teachers/status" method="post">
									        <input type="hidden" name="teacherId" value="${teacher.teacherId}">
									        <input type="hidden" name="accountStatus" value="ACTIVE">
									        <button type="submit" class="teacher-status-action">활성 처리</button>
									    </form>
									
									    <form action="${pageContext.request.contextPath}/admin/teachers/status" method="post">
									        <input type="hidden" name="teacherId" value="${teacher.teacherId}">
									        <input type="hidden" name="accountStatus" value="DORMANT">
									        <button type="submit" class="teacher-status-action">휴면 처리</button>
									    </form>
									
									    <form action="${pageContext.request.contextPath}/admin/teachers/status" method="post">
									        <input type="hidden" name="teacherId" value="${teacher.teacherId}">
									        <input type="hidden" name="accountStatus" value="SUSPENDED">
									        <button type="submit" class="teacher-status-action danger">계정 정지</button>
									    </form>
									
									    <form action="${pageContext.request.contextPath}/admin/teachers/status" method="post">
									        <input type="hidden" name="teacherId" value="${teacher.teacherId}">
									        <input type="hidden" name="accountStatus" value="DELETED">
									        <button type="submit" class="teacher-status-action danger">계정 삭제</button>
									    </form>
									</div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty teacherList}">
                        <tr>
                            <td colspan="7" style="text-align:center; padding:30px;">등록된 교사 정보가 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <div class="teachers-board-footer">
            <span>총 <span id="teacherTotalCount">${totalTeacherCount}</span>명</span>

            <div class="pagination-box">
                <button type="button" class="page-btn active">1</button>
            </div>
        </div>
    </section>

</div>

<!-- 교사 생성 모달 -->
<div class="teacher-modal" id="addTeacherModal">
    <div class="teacher-modal-dialog">
        <div class="teacher-modal-header">
            <h3>신규 교사 계정 생성</h3>
            <button type="button" class="close-modal-btn" id="closeAddTeacherModalBtn">×</button>
        </div>

        <form id="addTeacherForm"
		      class="teacher-modal-form"
		      action="${pageContext.request.contextPath}/admin/teachers/regist"
		      method="post">
            <div class="teacher-form-grid">
                <div>
                <label>이름 <span class="required-mark">*</span></label>
                <input type="text" name="teacherName" id="teacherNameInput" placeholder="예: 홍길동">
            </div>

            <div>
                <label>로그인 아이디 <span class="required-mark">*</span></label>
                <input type="text" name="loginId" id="teacherLoginIdInput" placeholder="예: t006">
            </div>

            <div>
                <label>이메일 <span class="required-mark">*</span></label>
                <input type="email" name="email" id="teacherEmailInput" placeholder="teacher@daewoo.edu.kr">
            </div>

            <div>
                <label>초기 비밀번호 <span class="required-mark">*</span></label>
                <input type="text" name="initialPassword" id="teacherPasswordInput" placeholder="초기 비밀번호 입력">
            </div>

            <div class="teacher-modal-actions">
                <button type="button" class="modal-cancel-btn" id="cancelAddTeacherBtn">취소</button>
                <button type="submit" class="modal-submit-btn">생성하기</button>
            </div>
        </form>
    </div>
</div>