<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/class/join.css">

<section class="class-join-page">
    <div class="class-join-card">
        <div class="join-icon-box">
            <div class="join-icon"></div>
        </div>

        <h1>클래스 가입하기</h1>
        <p class="join-sub-text">선생님께 전달받은 클래스 초대 코드를 입력해주세요.</p>

        <form action="${pageContext.request.contextPath}/student/classes/join" method="post" class="class-join-form">
            <div class="form-group">
                <label for="inviteCode">초대 코드</label>

                <div class="invite-input-wrap">
                    <span class="invite-key-icon"></span>
                    <input type="text"
                           id="inviteCode"
                           name="inviteCode"
                           value="${inviteCode}"
                           placeholder="영문, 숫자 조합 입력"
                           maxlength="30"
                           autocomplete="off" />
                </div>

                <c:if test="${not empty joinError}">
                    <p class="join-error">${joinError}</p>
                </c:if>
            </div>

            <div class="join-guide-box">
                <h3>알아두세요!</h3>
                <ul>
                    <li>초대 코드는 클래스를 개설한 선생님이 발급할 수 있습니다.</li>
                    <li>대소문자를 구분하지 않습니다.</li>
                    <li>유효기간이 만료된 코드는 사용할 수 없습니다.</li>
                </ul>
            </div>

            <button type="submit" class="join-submit-btn">클래스 입장하기 →</button>

            <a href="javascript:history.back();" class="join-back-btn">뒤로 가기</a>
        </form>
    </div>
</section>

<script src="${pageContext.request.contextPath}/resources/js/student/class/join.js"></script>