window.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('inviteCode');
    const form = document.querySelector('.class-join-form');

    if (input) {
        input.addEventListener('input', function () {
            this.value = this.value.toUpperCase().replace(/[^A-Z0-9\-]/g, '');
        });
    }

    if (form) {
        form.addEventListener('submit', function (e) {
            const value = input.value.trim();

            if (value.length < 6) {
                e.preventDefault();
                alert('올바른 초대 코드를 입력해주세요. 최소 6자리 이상이어야 합니다.');
            }
        });
    }
});