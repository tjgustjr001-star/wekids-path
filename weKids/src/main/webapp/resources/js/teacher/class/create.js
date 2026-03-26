window.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.class-create-form');

    if (!form) {
        return;
    }

    form.addEventListener('submit', function (e) {
        const className = document.getElementById('className').value.trim();
        const year = document.getElementById('year').value.trim();
        const semester = document.getElementById('semester').value.trim();
        const grade = document.getElementById('grade').value.trim();

        if (!className) {
            alert('클래스 이름을 입력해주세요.');
            e.preventDefault();
            return;
        }

        if (!year || !semester || !grade) {
            alert('학년도, 학기, 학년은 필수입니다.');
            e.preventDefault();
        }
    });
});