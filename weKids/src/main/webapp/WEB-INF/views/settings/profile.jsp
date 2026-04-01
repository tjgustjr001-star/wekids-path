<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/settings/profile.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<style>
    .toast-success,
    .toast-error {
        position: fixed;
        top: 24px;
        left: 50%;
        transform: translateX(-50%);
        z-index: 9999;
        min-width: 260px;
        max-width: 420px;
        padding: 14px 18px;
        border-radius: 14px;
        font-size: 14px;
        font-weight: 600;
        text-align: center;
        opacity: 0;
        transition: opacity 0.2s ease;
        box-shadow: 0 12px 30px rgba(15, 23, 42, 0.12);
    }
    .toast-success {
        background: #eaf8ee;
        color: #1f7a3d;
        border: 1px solid #b7e4c7;
    }
    .toast-error {
        background: #fff1f1;
        color: #c62828;
        border: 1px solid #f1b3b3;
    }
    .toast-show {
        opacity: 1;
    }
</style>

<div id="toastSuccess" class="toast-success"></div>
<div id="toastError" class="toast-error"></div>

<div class="profile-page">
    <div class="profile-wrap">
        <div class="top-back">
            <a href="${pageContext.request.contextPath}${baseSettingsPath}">
                <i class="fa-solid fa-arrow-left"></i> 설정으로 돌아가기
            </a>
        </div>

        <div class="profile-card">
            <div class="page-header">
                <h2>내 정보</h2>
                <p>프로필 사진과 이름을 변경합니다.</p>
            </div>

            <form id="profileForm"
                  method="post"
                  enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}${baseSettingsPath}/profile/modify"
                  onsubmit="return false;">

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-section">
                    <div class="section-label">프로필 사진</div>
                    <div class="profile-photo-row">
                        <div class="profile-photo-box">
                            <div class="profile-photo" id="profilePreview">
                                <c:choose>
                                    <c:when test="${not empty member.profile_image}">
                                        <c:choose>
                                            <c:when test="${fn:startsWith(member.profile_image, '/img/')}">
                                                <img id="profilePreviewImage"
                                                     src="${pageContext.request.contextPath}${member.profile_image}?t=<%= System.currentTimeMillis() %>"
                                                     alt="프로필 사진">
                                            </c:when>
                                            <c:otherwise>
                                                <img id="profilePreviewImage"
                                                     src="${pageContext.request.contextPath}/resources/upload/profile/${member.profile_image}?t=<%= System.currentTimeMillis() %>"
                                                     alt="프로필 사진">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <span id="profilePreviewText">
                                            <c:choose>
                                                <c:when test="${not empty member.name}">${fn:substring(member.name, 0, 1)}</c:when>
                                                <c:otherwise>유</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <label for="profileFile" class="camera-btn">
                                <i class="fa-solid fa-camera"></i>
                            </label>
                        </div>

                        <div class="photo-upload-box">
                            <label for="profileFile" class="upload-btn">사진 업로드</label>
                            <input type="file"
                                   id="profileFile"
                                   name="profileFile"
                                   accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
                                   hidden>
                            <p class="photo-guide">JPG, PNG, WEBP 파일 (최대 5MB)</p>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <label for="name" class="section-label">이름</label>
                    <input type="text"
                           id="name"
                           name="name"
                           class="text-input"
                           maxlength="20"
                           value="${member.name}"
                           placeholder="이름을 입력하세요">
                </div>

                <div class="grid-row form-section">
                    <div class="grid-col">
                        <label for="birth" class="section-label">생년월일</label>
                        <input type="date"
                               id="birth"
                               name="birth"
                               class="text-input"
                               value="<fmt:formatDate value='${member.birth}' pattern='yyyy-MM-dd'/>">
                    </div>

                    <div class="grid-col">
                        <div class="section-label">성별</div>
                        <div class="gender-group">
                            <label class="gender-btn ${member.gender eq 'M' ? 'active' : ''}">
                                <input type="radio" name="gender" value="M" ${member.gender eq 'M' ? 'checked' : ''}>
                                <span>남성</span>
                            </label>
                            <label class="gender-btn ${member.gender eq 'F' ? 'active' : ''}">
                                <input type="radio" name="gender" value="F" ${member.gender eq 'F' ? 'checked' : ''}>
                                <span>여성</span>
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <label for="intro" class="section-label">자기소개</label>
                    <textarea id="intro"
                              name="intro"
                              class="text-area"
                              maxlength="200"
                              placeholder="자기소개를 입력하세요.">${member.intro}</textarea>
                    <div class="text-count"><span id="introCount">0</span>/200자</div>
                </div>

                <div class="meta-box">
                    <div class="meta-row">
                        <span class="meta-label">사용자 유형</span>
                        <span class="role-badge">
                            <c:choose>
                                <c:when test="${role eq 'STUDENT' or role eq 'ROLE_STUDENT'}">학생</c:when>
                                <c:when test="${role eq 'PARENT' or role eq 'ROLE_PARENT'}">학부모</c:when>
                                <c:otherwise>선생님</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="meta-row">
                        <span class="meta-label">가입일</span>
                        <span class="meta-value">
                            <fmt:formatDate value="${member.created_at}" pattern="yyyy년 M월 d일"/>
                        </span>
                    </div>
                </div>

                <div class="btn-row">
                    <button type="button" class="save-btn" id="saveBtn">
                        <i class="fa-regular fa-floppy-disk"></i> 저장하기
                    </button>
                    <button type="button"
                            class="cancel-btn"
                            onclick="location.href='${pageContext.request.contextPath}${baseSettingsPath}'">취소</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
(function () {
    var form = document.getElementById("profileForm");
    var saveBtn = document.getElementById("saveBtn");
    var nameInput = document.getElementById("name");
    var birthInput = document.getElementById("birth");
    var introInput = document.getElementById("intro");
    var profileFileInput = document.getElementById("profileFile");
    var previewBox = document.getElementById("profilePreview");
    var introCount = document.getElementById("introCount");
    var toastSuccess = document.getElementById("toastSuccess");
    var toastError = document.getElementById("toastError");
    var genderInputs = document.querySelectorAll('input[name="gender"]');

    function showToast(el, message) {
        el.textContent = message;
        el.classList.add("toast-show");
        setTimeout(function () {
            el.classList.remove("toast-show");
        }, 2500);
    }

    function updateIntroCount() {
        introCount.textContent = introInput.value.length;
    }

    updateIntroCount();
    introInput.addEventListener("input", updateIntroCount);

    genderInputs.forEach(function (input) {
        input.addEventListener("change", function () {
            document.querySelectorAll(".gender-btn").forEach(function (btn) {
                btn.classList.remove("active");
            });
            this.closest(".gender-btn").classList.add("active");
        });
    });

    if (profileFileInput) {
        profileFileInput.addEventListener("change", function (e) {
            var file = e.target.files[0];
            if (!file) return;

            var allowed = ["image/jpeg", "image/png", "image/webp"];
            if (allowed.indexOf(file.type) === -1) {
                showToast(toastError, "JPG, PNG, WEBP 파일만 업로드할 수 있습니다.");
                this.value = "";
                return;
            }

            if (file.size > 5 * 1024 * 1024) {
                showToast(toastError, "파일 크기는 5MB 이하여야 합니다.");
                this.value = "";
                return;
            }

            var reader = new FileReader();
            reader.onload = function (ev) {
                previewBox.innerHTML = '<img id="profilePreviewImage" src="' + ev.target.result + '" alt="프로필 사진">';
            };
            reader.readAsDataURL(file);
        });
    }

    saveBtn.addEventListener("click", async function () {
        var name = (nameInput.value || "").trim();
        var intro = (introInput.value || "").trim();

        if (!name) {
            showToast(toastError, "이름을 입력해주세요.");
            nameInput.focus();
            return;
        }

        if (name.length > 20) {
            showToast(toastError, "이름은 20자 이하로 입력해주세요.");
            nameInput.focus();
            return;
        }

        if (intro.length > 200) {
            showToast(toastError, "자기소개는 200자 이하로 입력해주세요.");
            introInput.focus();
            return;
        }

        var formData = new FormData(form);

        saveBtn.disabled = true;

        try {
            var response = await fetch(form.action, {
                method: "POST",
                body: formData,
                headers: {
                    "Accept": "application/json"
                },
                credentials: "same-origin"
            });

            var text = await response.text();
            var data = null;

            try {
                data = JSON.parse(text);
            } catch (e) {
                console.error("JSON 파싱 실패. 서버 응답:", text);
                showToast(toastError, "서버가 JSON이 아닌 응답을 반환했습니다.");
                return;
            }

            if (!response.ok || !data.success) {
                showToast(toastError, data.message || "저장 중 오류가 발생했습니다.");
                return;
            }

            var member = data.member || {};

            nameInput.value = member.name || "";
            introInput.value = member.intro || "";
            birthInput.value = member.birth || "";

            if (profileFileInput) {
                profileFileInput.value = "";
            }

            if (member.gender === "M" || member.gender === "F") {
                genderInputs.forEach(function (input) {
                    var checked = input.value === member.gender;
                    input.checked = checked;
                    input.closest(".gender-btn").classList.toggle("active", checked);
                });
            }

            if (member.profile_image) {
                var imageSrc = "";

                if (member.profile_image.indexOf("/img/") === 0) {
                    imageSrc = "${pageContext.request.contextPath}" + member.profile_image;
                } else {
                    imageSrc = "${pageContext.request.contextPath}/resources/upload/profile/" + member.profile_image;
                }

                previewBox.innerHTML =
                    '<img id="profilePreviewImage" src="' + imageSrc + '?t=' + new Date().getTime() + '" alt="프로필 사진">';
            } else {
                var firstChar = member.name ? member.name.substring(0, 1) : "유";
                previewBox.innerHTML = "<span id='profilePreviewText'>" + firstChar + "</span>";
            }

            updateIntroCount();
            showToast(toastSuccess, data.message || "프로필이 성공적으로 저장되었습니다.");

        } catch (e) {
            console.error(e);
            showToast(toastError, "서버 요청 중 오류가 발생했습니다.");
        } finally {
            saveBtn.disabled = false;
        }
    });
})();
</script>
