(function() {
    const intro = document.getElementById('intro');
    const introCount = document.getElementById('introCount');
    const fileInput = document.getElementById('profileFile');
    const profilePreview = document.getElementById('profilePreview');
    const genderInputs = document.querySelectorAll('.gender-btn input[type="radio"]');

    function updateCount() {
        if (!intro || !introCount) return;
        introCount.textContent = intro.value.length;
    }

    function bindGenderButtons() {
        genderInputs.forEach(function(input) {
            input.addEventListener('change', function() {
                document.querySelectorAll('.gender-btn').forEach(function(btn) {
                    btn.classList.remove('active');
                });
                if (input.checked) {
                    input.closest('.gender-btn').classList.add('active');
                }
            });
        });
    }

    function previewProfileImage(file) {
        if (!file || !file.type.startsWith('image/')) {
            return;
        }

        const maxSize = 5 * 1024 * 1024;
        if (file.size > maxSize) {
            alert('프로필 이미지는 5MB 이하만 업로드할 수 있습니다.');
            fileInput.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = function(e) {
            profilePreview.innerHTML = '<img src="' + e.target.result + '" alt="프로필 미리보기">';
        };
        reader.readAsDataURL(file);
    }

    if (intro) {
        intro.addEventListener('input', updateCount);
        updateCount();
    }

    if (fileInput) {
        fileInput.addEventListener('change', function() {
            if (fileInput.files && fileInput.files[0]) {
                previewProfileImage(fileInput.files[0]);
            }
        });
    }

    bindGenderButtons();
})();
