<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>설정</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/settings/main.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<div class="settings-page">

    <div class="settings-header-card">
        <h1>설정</h1>
        <p>개인정보 및 환경설정을 관리하세요.</p>
    </div>

    <c:choose>
        <c:when test="${role eq 'PARENT' or role eq 'ROLE_PARENT'}">
            <c:set var="profilePath" value="/parent/settings/profile"/>
        </c:when>
        <c:when test="${role eq 'TEACHER' or role eq 'ROLE_TEACHER'}">
            <c:set var="profilePath" value="/teacher/settings/profile"/>
        </c:when>
        <c:otherwise>
            <c:set var="profilePath" value="/student/settings/profile"/>
        </c:otherwise>
    </c:choose>

    <div class="settings-section">
        <div class="section-title">계정 설정</div>

        <a class="setting-item" href="${pageContext.request.contextPath}${profilePath}">
            <div class="item-left">
                <div class="item-icon">
                    <i class="fa-regular fa-user"></i>
                </div>
                <div class="item-text">
                    <div class="item-name">내 정보</div>
                    <div class="item-desc">프로필 사진과 이름을 변경합니다.</div>
                </div>
            </div>
            <div class="item-arrow">
                <i class="fa-solid fa-chevron-right"></i>
            </div>
        </a>

        <a class="setting-item" href="${pageContext.request.contextPath}${baseSettingsPath}/info">
            <div class="item-left">
                <div class="item-icon">
                    <i class="fa-solid fa-key"></i>
                </div>
                <div class="item-text">
                    <div class="item-name">계정정보</div>
                    <div class="item-desc">이메일, 비밀번호 등 로그인 정보를 관리합니다.</div>
                </div>
            </div>
            <div class="item-arrow">
                <i class="fa-solid fa-chevron-right"></i>
            </div>
        </a>

        <c:if test="${role eq 'STUDENT' or role eq 'ROLE_STUDENT'}">
            <a class="setting-item" href="${pageContext.request.contextPath}/settings/child-link">
                <div class="item-left">
                    <div class="item-icon">
                        <i class="fa-solid fa-children"></i>
                    </div>
                    <div class="item-text">
                        <div class="item-name">보호자 초대 코드</div>
                        <div class="item-desc">보호자 연결 코드를 발급하여 학부모님께 전달합니다.</div>
                    </div>
                </div>
                <div class="item-arrow">
                    <i class="fa-solid fa-chevron-right"></i>
                </div>
            </a>
        </c:if>

        <c:if test="${role eq 'PARENT' or role eq 'ROLE_PARENT'}">
            <a class="setting-item" href="${pageContext.request.contextPath}/parent/children">
                <div class="item-left">
                    <div class="item-icon">
                        <i class="fa-solid fa-children"></i>
                    </div>
                    <div class="item-text">
                        <div class="item-name">자녀 관리</div>
                        <div class="item-desc">연결된 자녀 정보를 확인하고 관리합니다.</div>
                    </div>
                </div>
                <div class="item-arrow">
                    <i class="fa-solid fa-chevron-right"></i>
                </div>
            </a>
        </c:if>
    </div>

    <div class="settings-section">
        <div class="section-title">알림 설정</div>

        <a class="setting-item" href="${pageContext.request.contextPath}/settings/notification">
            <div class="item-left">
                <div class="item-icon">
                    <i class="fa-regular fa-bell"></i>
                </div>
                <div class="item-text">
                    <div class="item-name">알림수신설정</div>
                    <div class="item-desc">앱 푸시, 이메일 알림 등을 설정합니다.</div>
                </div>
            </div>
            <div class="item-arrow">
                <i class="fa-solid fa-chevron-right"></i>
            </div>
        </a>
    </div>

    <div class="settings-section">
        <div class="section-title">고객센터</div>

        <a class="setting-item" href="${pageContext.request.contextPath}/support">
            <div class="item-left">
                <div class="item-icon">
                    <i class="fa-solid fa-headset"></i>
                </div>
                <div class="item-text">
                    <div class="item-name">1:1 문의</div>
                    <div class="item-desc">궁금한 점이나 불편한 점을 문의합니다.</div>
                </div>
            </div>
            <div class="item-arrow">
                <i class="fa-solid fa-chevron-right"></i>
            </div>
        </a>

        <a class="setting-item" href="${pageContext.request.contextPath}/support/faq">
            <div class="item-left">
                <div class="item-icon">
                    <i class="fa-regular fa-circle-question"></i>
                </div>
                <div class="item-text">
                    <div class="item-name">FAQ</div>
                    <div class="item-desc">자주 묻는 질문을 확인합니다.</div>
                </div>
            </div>
            <div class="item-arrow">
                <i class="fa-solid fa-chevron-right"></i>
            </div>
        </a>
    </div>

</div>

</body>
</html>