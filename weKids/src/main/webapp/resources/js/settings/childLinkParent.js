window.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("childModal");
    const openBtn = document.getElementById("openChildModalBtn");
    const openBtn2 = document.getElementById("openChildModalBtn2");
    const closeBtn = document.getElementById("closeChildModalBtn");
    const connectBtn = document.getElementById("connectChildBtn");
    const codeInput = document.getElementById("childConnectCode");
    const hiddenInput = document.getElementById("hiddenConnectCode");
    const connectForm = document.getElementById("connectChildForm");

    function openModal() {
        if (modal) {
            modal.classList.add("active");
        }
        if (codeInput) {
            codeInput.value = "";
            codeInput.focus();
        }
    }

    function closeModal() {
        if (modal) {
            modal.classList.remove("active");
        }
    }

    if (openBtn) {
        openBtn.addEventListener("click", openModal);
    }

    if (openBtn2) {
        openBtn2.addEventListener("click", openModal);
    }

    if (closeBtn) {
        closeBtn.addEventListener("click", closeModal);
    }

    if (modal) {
        modal.addEventListener("click", function (e) {
            if (e.target === modal) {
                closeModal();
            }
        });
    }

    if (codeInput) {
        codeInput.addEventListener("input", function () {
            this.value = this.value.toUpperCase().replace(/[^A-Z0-9]/g, "");
        });
    }

    if (connectBtn) {
        connectBtn.addEventListener("click", function () {
            const code = codeInput.value.trim();

            if (!code) {
                alert("연결 코드를 입력해주세요.");
                codeInput.focus();
                return;
            }

            hiddenInput.value = code;
            connectForm.submit();
        });
    }
});