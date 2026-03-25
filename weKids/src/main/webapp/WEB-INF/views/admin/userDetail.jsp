<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="admin-user-detail-page">

    <button type="button" class="back-link-btn" onclick="history.back();">
        ← 사용자 목록으로 돌아가기
    </button>

    <div class="user-detail-grid">
        <div class="user-detail-left">
            <section class="panel-card user-profile-card">
                <h1 class="user-profile-name">${user.name}</h1>

                <span class="role-pill ${user.roleCode eq 'STUDENT' ? 'student' : 'parent'}">
                    <c:choose>
                        <c:when test="${user.roleCode eq 'STUDENT'}">학생</c:when>
                        <c:otherwise>학부모</c:otherwise>
                    </c:choose>
                </span>

                <div style="margin-top:12px;">
                    <span class="status-pill
                        ${user.accountStatus eq 'ACTIVE' ? 'active' :
                          (user.accountStatus eq 'DORMANT' ? 'dormant' : 'suspended')}">
                        <c:choose>
                            <c:when test="${user.accountStatus eq 'ACTIVE'}">활성</c:when>
                            <c:when test="${user.accountStatus eq 'DORMANT'}">휴면</c:when>
                            <c:otherwise>정지</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="teacher-info-list">
                    <div class="teacher-info-item">
                        <strong>역할</strong>
                        <span>${user.roleName}</span>
                    </div>
                    <div class="teacher-info-item">
                        <strong>아이디</strong>
                        <span>${user.loginId}</span>
                    </div>
                    <div class="teacher-info-item">
                        <strong>이메일</strong>
                        <span>${user.email}</span>
                    </div>
                    <div class="teacher-info-item">
                        <strong>최근 접속</strong>
                        <span>${user.lastLoginAt}</span>
                    </div>
                    <div class="teacher-info-item">
                        <strong>가입일</strong>
                        <span>${user.createdAt}</span>
                    </div>
                </div>
            </section>

            <section class="panel-card teacher-action-card">
                <h2 class="panel-title">관리자 조치</h2>

                <div class="teacher-action-group">
                    <form action="${pageContext.request.contextPath}/admin/users/status" method="post">
                        <input type="hidden" name="memberId" value="${user.memberId}">
                        <input type="hidden" name="accountStatus" value="ACTIVE">
                        <input type="hidden" name="redirectPage" value="detail">
                        <button type="submit" class="action-green-btn">계정 활성화</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/admin/users/status" method="post">
                        <input type="hidden" name="memberId" value="${user.memberId}">
                        <input type="hidden" name="accountStatus" value="DORMANT">
                        <input type="hidden" name="redirectPage" value="detail">
                        <button type="submit" class="action-gray-btn">휴면 처리</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/admin/users/status" method="post">
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
            <section class="panel-card">
                <h2 class="panel-title">기본 정보</h2>
                <div class="detail-info-grid">
                    <div class="detail-box">
                        <p>소속</p>
                        <strong>${user.classInfo}</strong>
                    </div>
                    <div class="detail-box">
                        <p>연결</p>
                        <strong>${user.connectionStatus}</strong>
                    </div>
                </div>
            </section>

            <section class="panel-card">
                <h2 class="panel-title">주간 활동량 추이</h2>
                <div class="chart-wrap">
                    <canvas id="userWeeklyActivityChart"></canvas>
                </div>
            </section>

            <section class="panel-card">
                <h2 class="panel-title">최근 접속 환경</h2>
                <div class="detail-info-grid">
                    <div class="detail-box">
                        <p>기기</p>
                        <strong>Windows 11 / Chrome</strong>
                    </div>
                    <div class="detail-box">
                        <p>접속 위치</p>
                        <strong>대전, 대한민국</strong>
                    </div>
                </div>
            </section>

            <section class="panel-card">
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