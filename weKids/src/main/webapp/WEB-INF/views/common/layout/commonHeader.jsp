<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<header class="top-header">
    <div class="top-header-right">
        <div class="header-icon-wrap">
            <button type="button" class="header-icon-btn noti-btn" id="notiToggleBtn" aria-label="알림">
                <span class="header-icon header-bell-icon"></span>
                <span class="noti-badge">2</span>
            </button>

            <div class="header-dropdown notification-dropdown" id="notificationDropdown">
                <div class="dropdown-header">
                    <strong>알림</strong>
                    <button type="button" class="dropdown-link-btn">모두 읽음</button>
                </div>

                <div class="dropdown-body">
                    <a href="${pageContext.request.contextPath}${assignmentUrl}" class="notification-item unread">
                        <div class="notification-title-row">
                            <span class="notification-title">새로운 과제</span>
                            <span class="notification-time">10분 전</span>
                        </div>
                        <p class="notification-text">과학 실험 관찰 보고서가 등록되었습니다.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}${learnUrl}" class="notification-item unread">
                        <div class="notification-title-row">
                            <span class="notification-title">학습 완료</span>
                            <span class="notification-time">1시간 전</span>
                        </div>
                        <p class="notification-text">수학 3단원 개념 영상 시청을 완료했습니다.</p>
                    </a>

                    <a href="${pageContext.request.contextPath}${bulletinUrl}" class="notification-item">
                        <div class="notification-title-row">
                            <span class="notification-title">공지사항</span>
                            <span class="notification-time">어제</span>
                        </div>
                        <p class="notification-text">내일 체육복을 입고 등교해주시기 바랍니다.</p>
                    </a>
                </div>

                <div class="dropdown-footer">
                    <a href="${pageContext.request.contextPath}${allNotificationUrl}">모든 알림 보기</a>
                </div>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}${settingsUrl}" class="header-icon-btn" aria-label="설정">
            <span class="header-icon header-settings-icon"></span>
        </a>
    </div>
</header>