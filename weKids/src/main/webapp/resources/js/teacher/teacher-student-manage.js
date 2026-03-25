window.addEventListener('DOMContentLoaded', function () {
	
	const contextPath = document.body.getAttribute('data-context-path') || '';
	let currentStudentRow = null;
    const inviteModal = document.getElementById('inviteModal');
    const studentDetailModal = document.getElementById('studentDetailModal');
    const openInviteModalBtn = document.getElementById('openInviteModalBtn');
    const studentSearchInput = document.getElementById('studentSearchInput');
    const studentRows = document.querySelectorAll('.teacher-student-row');

    const detailName = document.getElementById('detailName');
    const detailNumber = document.getElementById('detailNumber');
    const detailParentLinkedText = document.getElementById('detailParentLinkedText');
    const detailPhone = document.getElementById('detailPhone');
    const detailProgress = document.getElementById('detailProgress');
    const detailAssignments = document.getElementById('detailAssignments');
    const detailLastLogin = document.getElementById('detailLastLogin');
    const detailFeedbackSummary = document.getElementById('detailFeedbackSummary');
    const detailMemoText = document.getElementById('detailMemoText');
    const detailAvatar = document.getElementById('detailAvatar');
    const detailWarningBox = document.getElementById('detailWarningBox');
    const tagButtons = document.querySelectorAll('.tag-btn');

    function openModal(modal) {
        if (!modal) return;
        modal.classList.add('open');
    }

    function closeModal(modal) {
        if (!modal) return;
        modal.classList.remove('open');
    }

    if (openInviteModalBtn) {
        openInviteModalBtn.addEventListener('click', function () {
            openModal(inviteModal);
        });
    }

    document.querySelectorAll('[data-close-modal]').forEach(function (button) {
        button.addEventListener('click', function () {
            const modalId = button.getAttribute('data-close-modal');
            const modal = document.getElementById(modalId);
            closeModal(modal);
        });
    });

    document.querySelectorAll('.teacher-modal-backdrop').forEach(function (backdrop) {
        backdrop.addEventListener('click', function (e) {
            if (e.target === backdrop) {
                closeModal(backdrop);
            }
        });
    });

    document.querySelectorAll('.row-menu-toggle-btn').forEach(function (button) {
        button.addEventListener('click', function (e) {
            e.stopPropagation();

            const dropdown = button.parentElement.querySelector('.row-menu-dropdown');
            const isOpen = dropdown.classList.contains('open');

            document.querySelectorAll('.row-menu-dropdown').forEach(function (menu) {
                menu.classList.remove('open');
            });

            if (!isOpen) {
                dropdown.classList.add('open');
            }
        });
    });

    document.addEventListener('click', function () {
        document.querySelectorAll('.row-menu-dropdown').forEach(function (menu) {
            menu.classList.remove('open');
        });
    });

    function applySelectedTags(tagsCsv) {
        const tags = (tagsCsv || '')
            .split(',')
            .map(function (item) { return item.trim(); })
            .filter(function (item) { return item.length > 0; });

        tagButtons.forEach(function (btn) {
            const tag = btn.getAttribute('data-tag');
            btn.classList.remove('selected', 'danger');

            if (tags.includes(tag)) {
                btn.classList.add('selected');

                if (
                    tag.indexOf('주의') > -1 ||
                    tag.indexOf('미제출') > -1 ||
                    tag.indexOf('어려움') > -1
                ) {
                    btn.classList.add('danger');
                }
            }
        });
    }

    tagButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            btn.classList.toggle('selected');

            const tag = btn.getAttribute('data-tag') || '';
            if (
                tag.indexOf('주의') > -1 ||
                tag.indexOf('미제출') > -1 ||
                tag.indexOf('어려움') > -1
            ) {
                btn.classList.toggle('danger');
            }
        });
    });

    function openStudentDetailFromRow(row) {
		currentStudentRow = row;
        const name = row.dataset.studentName || '학생';
        const number = row.dataset.number || '';
        const parentLinked = row.dataset.parentLinked === '1';
        const phone = row.dataset.phone || '-';
        const lastLogin = row.dataset.lastLogin || '-';
        const progress = row.dataset.learningProgress || '0';
        const assignments = row.dataset.recentAssignments || '-';
        const feedbackSummary = row.dataset.feedbackSum || '-';
        const memo = row.dataset.memo || '';
        const tags = row.dataset.tags || '';

        detailName.textContent = name;
        detailNumber.textContent = number + '번';
        detailParentLinkedText.textContent = parentLinked ? '연동 완료' : '미연동';
        detailPhone.textContent = '연락처: ' + phone;
        detailProgress.textContent = progress + '%';
        detailAssignments.textContent = assignments;
        detailLastLogin.textContent = lastLogin;
        detailFeedbackSummary.textContent = feedbackSummary;
        detailMemoText.value = memo;
        detailAvatar.textContent = name.charAt(0);

        if (Number(progress) < 60) {
            detailWarningBox.classList.remove('safe');
            detailWarningBox.innerHTML =
                '<div class="warning-icon"></div>' +
                '<div>' +
                '<strong>수행 관리가 필요한 상태</strong>' +
                '<p>최근 접속이 뜸하거나 미제출 과제가 있어 학부모 안내가 필요할 수 있습니다.</p>' +
                '</div>';
        } else {
            detailWarningBox.classList.add('safe');
            detailWarningBox.innerHTML =
                '<div class="warning-icon"></div>' +
                '<div>' +
                '<strong>현재 안정적인 학습 상태</strong>' +
                '<p>현재 밀린 과제나 주의가 필요한 항목이 없습니다.</p>' +
                '</div>';
        }

        applySelectedTags(tags);
        openModal(studentDetailModal);
    }

    studentRows.forEach(function (row) {
        row.addEventListener('click', function () {
            openStudentDetailFromRow(row);
        });
    });

    document.querySelectorAll('.detail-open-btn').forEach(function (button) {
        button.addEventListener('click', function (e) {
            e.stopPropagation();
            const row = button.closest('.teacher-student-row');
            document.querySelectorAll('.row-menu-dropdown').forEach(function (menu) {
                menu.classList.remove('open');
            });
            openStudentDetailFromRow(row);
        });
    });

    if (studentSearchInput) {
        studentSearchInput.addEventListener('input', function () {
            const keyword = studentSearchInput.value.trim().toLowerCase();

            studentRows.forEach(function (row) {
                const name = (row.dataset.studentName || '').toLowerCase();
                row.style.display = name.indexOf(keyword) > -1 ? '' : 'none';
            });
        });
    }

    function copyText(text) {
        if (!text) return;
        navigator.clipboard.writeText(text);
    }

    const copyInviteCodeBtn = document.getElementById('copyInviteCodeBtn');
    const copyInviteLinkBtn = document.getElementById('copyInviteLinkBtn');
    const inviteCodeText = document.getElementById('inviteCodeText');
    const inviteLinkText = document.getElementById('inviteLinkText');

    if (copyInviteCodeBtn && inviteCodeText) {
        copyInviteCodeBtn.addEventListener('click', function () {
            copyText(inviteCodeText.textContent.trim());
            alert('초대 코드가 복사되었습니다.');
        });
    }

    if (copyInviteLinkBtn && inviteLinkText) {
        copyInviteLinkBtn.addEventListener('click', function () {
            copyText(inviteLinkText.value);
            alert('초대 링크가 복사되었습니다.');
        });
    }

	const saveMemoBtn = document.getElementById('saveMemoBtn');
	if (saveMemoBtn) {
	    saveMemoBtn.addEventListener('click', function () {
	        if (!currentStudentRow) {
	            alert('저장할 학생 정보를 찾을 수 없습니다.');
	            return;
	        }

	        const studentClassId = currentStudentRow.dataset.studentClassId;
	        const memo = detailMemoText.value || '';
	        const tagNameList = getSelectedTags();

	        fetch(contextPath + '/teacher/classes/students/observation', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify({
	                studentClassId: Number(studentClassId),
	                observationMemo: memo,
	                tagNameList: tagNameList
	            })
	        })
	        .then(function (response) {
	            if (!response.ok) {
	                throw new Error('저장에 실패했습니다.');
	            }
	            return response.json();
	        })
	        .then(function () {
	            currentStudentRow.dataset.memo = memo;
	            currentStudentRow.dataset.tags = tagNameList.join(',');

	            alert('지도 메모 및 관찰 태그가 저장되었습니다.');
	        })
	        .catch(function (error) {
	            alert(error.message || '저장 중 오류가 발생했습니다.');
	        });
	    });
	}
	
	function getSelectedTags() {
	    return Array.from(document.querySelectorAll('.tag-btn.selected'))
	        .map(function (btn) {
	            return (btn.getAttribute('data-tag') || '').trim();
	        })
	        .filter(function (tag) {
	            return tag.length > 0;
	        });
	}
	document.querySelectorAll('.remove-student-btn').forEach(function (button) {
	    button.addEventListener('click', function (e) {
	        e.stopPropagation();

	        const row = button.closest('.teacher-student-row');
	        if (!row) return;

	        const studentName = row.dataset.studentName || '학생';
	        const studentClassId = row.dataset.studentClassId;

	        if (!confirm(studentName + ' 학생을 이 클래스에서 제외할까요?')) {
	            return;
	        }

	        fetch(contextPath + '/teacher/classes/students/remove', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify({
	                studentClassId: Number(studentClassId)
	            })
	        })
	        .then(function (response) {
	            if (!response.ok) {
	                throw new Error('클래스 제외에 실패했습니다.');
	            }
	            return response.json();
	        })
	        .then(function () {
	            row.remove();
	            closeModal(studentDetailModal);
	            alert('학생이 클래스에서 제외되었습니다.');
	        })
	        .catch(function (error) {
	            alert(error.message || '처리 중 오류가 발생했습니다.');
	        });
	    });
	});
});