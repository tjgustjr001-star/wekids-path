window.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('linkChildModal');
    const openBtn = document.getElementById('openLinkModalBtn');
    const closeBtn = document.getElementById('closeLinkModalBtn');
    const verifyBtn = document.getElementById('verifyLinkCodeBtn');
    const retryBtn = document.getElementById('retryLinkBtn');
    const confirmBtn = document.getElementById('confirmLinkBtn');
    const codeInput = document.getElementById('linkCodeInput');
    const inputStep = document.getElementById('linkInputStep');
    const confirmStep = document.getElementById('linkConfirmStep');
    const toastContainer = document.getElementById('parentToastContainer');

    const childSwitcherBtn = document.getElementById('childSwitcherBtn');
    const childSwitcherMenu = document.getElementById('childSwitcherMenu');

    function showToast(message, type) {
        if (!toastContainer) return;
        const toast = document.createElement('div');
        toast.className = 'parent-toast ' + (type || 'success');
        toast.textContent = message;
        toastContainer.appendChild(toast);

        setTimeout(function () {
            toast.style.opacity = '0';
        }, 2400);

        setTimeout(function () {
            if (toast.parentNode) toast.parentNode.removeChild(toast);
        }, 2800);
    }

    function resetModal() {
        if (!modal) return;
        modal.classList.remove('open');
        document.body.style.overflow = '';
        if (codeInput) codeInput.value = '';
        if (inputStep) inputStep.classList.remove('hidden');
        if (confirmStep) confirmStep.classList.add('hidden');
    }

    function openModal() {
        if (!modal) return;
        modal.classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    if (openBtn) openBtn.addEventListener('click', openModal);
    if (closeBtn) closeBtn.addEventListener('click', resetModal);

    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) {
                resetModal();
            }
        });
    }

    if (codeInput) {
        codeInput.addEventListener('input', function () {
            codeInput.value = codeInput.value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase().slice(0, 8);
        });
    }

    if (verifyBtn) {
        verifyBtn.addEventListener('click', function () {
            if (!codeInput || !codeInput.value.trim()) {
                showToast('연결 코드를 입력해주세요.', 'error');
                return;
            }
            if (codeInput.value.trim().length < 8) {
                showToast('연결 코드는 8자리입니다.', 'error');
                return;
            }

            if (inputStep) inputStep.classList.add('hidden');
            if (confirmStep) confirmStep.classList.remove('hidden');
            showToast('자녀 정보가 확인되었습니다.', 'success');
        });
    }

    if (retryBtn) {
        retryBtn.addEventListener('click', function () {
            if (inputStep) inputStep.classList.remove('hidden');
            if (confirmStep) confirmStep.classList.add('hidden');
        });
    }

    if (confirmBtn) {
        confirmBtn.addEventListener('click', function () {
            showToast('김민준 학생이 자녀 목록에 추가되었습니다.', 'success');
            resetModal();
        });
    }

    if (childSwitcherBtn && childSwitcherMenu) {
        childSwitcherBtn.addEventListener('click', function (e) {
            e.stopPropagation();
            childSwitcherMenu.classList.toggle('open');
        });

        document.addEventListener('click', function (e) {
            if (!childSwitcherMenu.contains(e.target) && !childSwitcherBtn.contains(e.target)) {
                childSwitcherMenu.classList.remove('open');
            }
        });
    }
<<<<<<< HEAD
=======
});
document.querySelectorAll('.unlink-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        const studentId = this.dataset.studentId;

        if (!confirm("정말 연동을 해제하시겠습니까?")) return;

        fetch('${pageContext.request.contextPath}/parent/settings/unlink', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                '${_csrf.headerName}': '${_csrf.token}'
            },
            body: 'studentId=' + encodeURIComponent(studentId)
        })
        .then(res => res.text())
        .then(result => {
            if (result === 'success') {
                alert('연동이 해제되었습니다.');
                location.reload();
            } else {
                alert('연동 해제 실패');
            }
        })
        .catch(err => {
            console.error(err);
            alert('오류가 발생했습니다.');
        });
    });
>>>>>>> refs/remotes/origin/brunch1
});