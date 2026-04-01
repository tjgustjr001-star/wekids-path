<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<style>
    * { box-sizing: border-box; }
    .account-page { max-width: 860px; margin: 0 auto; padding: 24px 24px 60px; }
    .top-back { margin-bottom: 14px; }
    .top-back a { color: #7a7f87; text-decoration: none; font-size: 13px; }
    .top-back a:hover { color: #1d8f63; }
    .account-card { background: #fff; border: 1px solid #e5e7eb; border-radius: 16px; box-shadow: 0 4px 18px rgba(0,0,0,.03); overflow: hidden; }
    .page-header { padding: 26px 30px 8px; }
    .page-header h2 { margin: 0; font-size: 28px; font-weight: 800; }
    .page-header p { margin: 10px 0 0; color: #8b93a1; font-size: 14px; }
    .form-body { padding: 0 30px 30px; }
    .section-title { display: flex; align-items: center; gap: 8px; margin: 22px 0 12px; font-size: 18px; font-weight: 800; color: #0f172a; }
    .section-title i { color: #19a974; }
    .field { margin-bottom: 16px; }
    .field label { display: block; margin-bottom: 8px; font-size: 13px; font-weight: 700; color: #374151; }
    .field input { width: 100%; height: 46px; border: 1px solid #d6dbe4; border-radius: 10px; padding: 0 14px; font-size: 14px; outline: none; }
    .field input:focus { border-color: #1aa36f; box-shadow: 0 0 0 4px rgba(26,163,111,.08); }
    .field-help { margin-top: 8px; font-size: 12px; color: #9aa1ad; }
    .divider { height: 1px; background: #edf0f4; margin: 18px 0 24px; }
    .security-box { margin-top: 10px; padding: 18px 18px 14px; background: #eef5ff; border: 1px solid #cfe0ff; border-radius: 12px; }
    .security-box h4 { margin: 0 0 10px; color: #285ea8; font-size: 14px; }
    .security-box ul { margin: 0; padding-left: 18px; color: #4168a7; font-size: 12px; line-height: 1.7; }
    .btn-row { display: flex; gap: 10px; margin-top: 24px; }
    .save-btn, .cancel-btn, .danger-btn { height: 46px; border-radius: 10px; font-size: 14px; font-weight: 700; cursor: pointer; }
    .save-btn { flex: 1; border: 0; background: #177e53; color: #fff; }
    .save-btn:hover { background: #136b46; }
    .cancel-btn { width: 96px; border: 1px solid #d1d5db; background: #fff; color: #374151; }
    .danger-wrap { margin-top: 28px; padding-top: 24px; border-top: 1px solid #edf0f4; }
    .danger-title { margin: 0 0 8px; color: #e53935; font-size: 14px; font-weight: 800; }
    .danger-desc { margin: 0 0 14px; color: #8b93a1; font-size: 12px; }
    .danger-btn { min-width: 118px; border: 1px solid #f2b6b4; background: #fff; color: #d94242; }
    .toast { position: fixed; top: 26px; right: 26px; min-width: 240px; max-width: 360px; padding: 14px 16px; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,.12); font-size: 13px; font-weight: 700; opacity: 0; pointer-events: none; transform: translateY(-10px); transition: all .25s ease; z-index: 9999; }
    .toast.show { opacity: 1; transform: translateY(0); }
    .toast.success { background: #edfdf4; color: #12724d; border: 1px solid #bdeacb; }
    .toast.error { background: #fff1f1; color: #c62828; border: 1px solid #f2b8b5; }
</style>
<div id="toastSuccess" class="toast success"></div>
<div id="toastError" class="toast error"></div>

<div class="account-page">
    <div class="top-back">
        <a href="${pageContext.request.contextPath}${baseSettingsPath}"><i class="fa-solid fa-arrow-left"></i> 설정으로 돌아가기</a>
    </div>

    <div class="account-card">
        <div class="page-header">
            <h2>계정정보</h2>
            <p>이메일, 비밀번호 등 로그인 정보를 관리합니다.</p>
        </div>

        <div class="form-body">
            <div class="section-title"><i class="fa-solid fa-mobile-screen-button"></i> 휴대폰 번호</div>
            <div class="field">
                <label for="phone">연락처</label>
                <input type="text" id="phone" name="phone" value="${member.phone}" placeholder="010-1234-5678">
            </div>

            <div class="divider"></div>

            <div class="section-title"><i class="fa-regular fa-envelope"></i> 이메일 주소</div>
            <div class="field">
                <label for="email">이메일</label>
                <input type="text" id="email" name="email" value="${member.email}" placeholder="student@example.com">
                <div class="field-help">이메일 변경 시 본인 인증이 필요합니다.</div>
            </div>

            <div class="divider"></div>

            <div class="section-title"><i class="fa-solid fa-lock"></i> 비밀번호 변경</div>
            <div class="field">
                <label for="currentPwd">현재 비밀번호</label>
                <input type="password" id="currentPwd" name="currentPwd" placeholder="현재 비밀번호를 입력하세요">
            </div>
            <div class="field">
                <label for="newPwd">새 비밀번호</label>
                <input type="password" id="newPwd" name="newPwd" placeholder="새 비밀번호를 입력하세요">
            </div>
            <div class="field">
                <label for="confirmPwd">새 비밀번호 확인</label>
                <input type="password" id="confirmPwd" name="confirmPwd" placeholder="새 비밀번호를 다시 입력하세요">
            </div>

            <div class="security-box">
                <h4>보안 안내</h4>
                <ul>
                    <li>비밀번호는 정기적으로 변경하는 것이 좋습니다.</li>
                    <li>다른 서비스와 동일한 비밀번호 사용을 피하세요.</li>
                    <li>타인과 계정 정보를 공유하지 마세요.</li>
                </ul>
            </div>

            <div class="btn-row">
                <button type="button" class="save-btn" id="saveBtn"><i class="fa-regular fa-floppy-disk"></i> 저장하기</button>
                <button type="button" class="cancel-btn" onclick="location.href='${pageContext.request.contextPath}${baseSettingsPath}'">취소</button>
            </div>

            <div class="danger-wrap">
                <div class="danger-title">계정 삭제</div>
                <div class="danger-desc">계정을 삭제하면 모든 데이터가 영구적으로 삭제되며 복구할 수 없습니다.</div>
                <button type="button" class="danger-btn" id="deleteBtn">계정 삭제 신청</button>
            </div>
        </div>
    </div>
</div>

<script>
(function () {
    var saveBtn = document.getElementById('saveBtn');
    var phoneInput = document.getElementById('phone');
    var emailInput = document.getElementById('email');
    var currentPwdInput = document.getElementById('currentPwd');
    var newPwdInput = document.getElementById('newPwd');
    var confirmPwdInput = document.getElementById('confirmPwd');
    var toastSuccess = document.getElementById('toastSuccess');
    var toastError = document.getElementById('toastError');
    var deleteBtn = document.getElementById('deleteBtn');

    function showToast(target, message) {
        target.textContent = message;
        target.classList.add('show');
        setTimeout(function () {
            target.classList.remove('show');
        }, 2500);
    }

    function postForm(url, body) {
        return fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'Accept': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: body,
            credentials: 'same-origin'
        }).then(function (res) {
            return res.text().then(function (text) {
                var data = null;

                try {
                    data = text ? JSON.parse(text) : {};
                } catch (e) {
                    throw {
                        message: '서버가 JSON이 아닌 응답을 반환했습니다. 컨트롤러 메서드와 서버 로그를 확인해주세요.',
                        raw: text,
                        status: res.status
                    };
                }

                if (!res.ok) {
                    throw data;
                }
                return data;
            });
        });
    }

    saveBtn.addEventListener('click', function () {
        var phone = phoneInput.value.trim();
        var email = emailInput.value.trim();
        var currentPwd = currentPwdInput.value.trim();
        var newPwd = newPwdInput.value.trim();
        var confirmPwd = confirmPwdInput.value.trim();

        postForm('${pageContext.request.contextPath}${baseSettingsPath}/info/modify',
            'phone=' + encodeURIComponent(phone) + '&email=' + encodeURIComponent(email)
        )
        .then(function (data) {
            if (!newPwd && !confirmPwd && !currentPwd) {
                showToast(toastSuccess, data.message || '계정정보가 저장되었습니다.');
                return;
            }

            return postForm('${pageContext.request.contextPath}${baseSettingsPath}/info/password',
                'currentPwd=' + encodeURIComponent(currentPwd) +
                '&newPwd=' + encodeURIComponent(newPwd) +
                '&confirmPwd=' + encodeURIComponent(confirmPwd)
            ).then(function (pwdData) {
                currentPwdInput.value = '';
                newPwdInput.value = '';
                confirmPwdInput.value = '';
                showToast(toastSuccess, pwdData.message || '비밀번호가 변경되었습니다.');
            });
        })
        .catch(function (err) {
            showToast(toastError, err && err.message ? err.message : '처리 중 오류가 발생했습니다.');
        });
    });

    deleteBtn.addEventListener('click', function () {
        if (!confirm('정말 계정을 삭제하시겠습니까? 삭제 후에는 복구할 수 없습니다.')) {
            return;
        }

        postForm('${pageContext.request.contextPath}${baseSettingsPath}/info/delete', '')
            .then(function (data) {
                showToast(toastSuccess, data.message || '계정 삭제가 완료되었습니다.');
                setTimeout(function () {
                    location.href = '${pageContext.request.contextPath}/auth/logout';
                }, 800);
            })
            .catch(function (err) {
                showToast(toastError, err && err.message ? err.message : '계정 삭제 처리 중 오류가 발생했습니다.');
            });
    });
})();
</script>
