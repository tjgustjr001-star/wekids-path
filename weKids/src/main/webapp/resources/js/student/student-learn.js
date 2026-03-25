window.addEventListener('DOMContentLoaded', function () {
    const tabButtons = document.querySelectorAll('.learning-tab-btn');
    const learningCards = document.querySelectorAll('.learning-card');

    const modalOverlay = document.getElementById('learningModalOverlay');
    const modalCloseBtn = document.getElementById('learningModalCloseBtn');

    const modalTitle = document.getElementById('modalLearningTitle');
    const modalTypeBadge = document.getElementById('modalTypeBadge');
    const modalRequiredBadge = document.getElementById('modalRequiredBadge');

    const videoContent = document.getElementById('learningVideoContent');
    const textContent = document.getElementById('learningTextContent');
    const linkContent = document.getElementById('learningLinkContent');
    const textTitle = document.getElementById('learningTextTitle');

    const currentProgressText = document.getElementById('learningCurrentProgressText');
    const sideProgressFill = document.getElementById('learningSideProgressFill');
	
	const learningLinkAnchor = document.getElementById('learningLinkAnchor');
	
    const fakeVideoPlayBtn = document.getElementById('fakeVideoPlayBtn');
    const fakeVideoProgressBar = document.getElementById('fakeVideoProgressBar');
    const fakeVideoProgressFill = document.getElementById('fakeVideoProgressFill');
    const fakeVideoTimeText = document.getElementById('fakeVideoTimeText');

    const textCompleteBtn = document.getElementById('learningTextCompleteBtn');
    const linkCompleteBtn = document.getElementById('learningLinkCompleteBtn');

    const difficultyIntroPanel = document.getElementById('difficultyIntroPanel');
    const difficultyFormPanel = document.getElementById('difficultyFormPanel');
    const difficultyOpenBtn = document.getElementById('difficultyOpenBtn');
    const difficultyBackBtn = document.getElementById('difficultyBackBtn');
    const difficultyReasonButtons = document.querySelectorAll('.difficulty-reason-btn');
    const difficultyMessageInput = document.getElementById('difficultyMessageInput');
    const difficultySubmitBtn = document.getElementById('difficultySubmitBtn');

    const saveBtn = document.getElementById('learningSaveBtn');
    const completeBtn = document.getElementById('learningCompleteBtn');
    const toastContainer = document.getElementById('learningToastContainer');

    let selectedCard = null;
    let currentLearning = null;
    let selectedDifficultyReason = '';
    let activeTab = 'weekly';

    function showToast(message, type) {
        if (!toastContainer) return;

        const toast = document.createElement('div');
        toast.className = 'learning-toast ' + (type || 'info');
        toast.textContent = message;
        toastContainer.appendChild(toast);

        setTimeout(function () {
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(10px)';
        }, 2400);

        setTimeout(function () {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 2800);
    }

    function applyTabFilter(tab) {
        activeTab = tab;

        learningCards.forEach(function (card, index) {
            if (tab === 'weekly') {
                card.style.display = '';
            } else {
                card.style.display = index < 2 ? '' : 'none';
            }
        });
    }

    function resetContentButtons() {
        textCompleteBtn.disabled = false;
        textCompleteBtn.textContent = '다 읽었어요';

        linkCompleteBtn.disabled = false;
        linkCompleteBtn.textContent = '자료를 확인했어요';
    }

	function openModalFromCard(card) {
	    selectedCard = card;

	    currentLearning = {
	        id: card.dataset.learningId,
	        title: card.dataset.title,
	        type: card.dataset.type,
	        required: card.dataset.required === 'true',
	        status: card.dataset.status,
	        deadline: card.dataset.deadline,
	        duration: card.dataset.duration,
	        progress: Number(card.dataset.progress || 0),
	        content: card.dataset.content || '',
	        textContent: card.dataset.textContent || '',
	        linkUrl: card.dataset.linkUrl || ''
	    };

	    modalTitle.textContent = currentLearning.title;
	    textTitle.textContent = currentLearning.title;

	    if (currentLearning.type === 'video') {
	        modalTypeBadge.textContent = '영상 학습';
	    } else if (currentLearning.type === 'text') {
	        modalTypeBadge.textContent = '텍스트 학습';
	    } else {
	        modalTypeBadge.textContent = '참고 링크';
	    }

	    modalRequiredBadge.style.display = currentLearning.required ? 'inline-block' : 'none';

	    videoContent.style.display = currentLearning.type === 'video' ? 'block' : 'none';
	    textContent.style.display = currentLearning.type === 'text' ? 'block' : 'none';
	    linkContent.style.display = currentLearning.type === 'link' ? 'block' : 'none';

	    if (currentLearning.type === 'text') {
	        const textParagraphs = textContent.querySelectorAll('p');
	        if (textParagraphs.length > 0) {
	            textParagraphs[0].textContent = currentLearning.textContent || currentLearning.content || '학습 내용이 없습니다.';
	        }
	    }

	    if (currentLearning.type === 'link' && learningLinkAnchor) {
	        learningLinkAnchor.href = currentLearning.linkUrl || '#';
	        learningLinkAnchor.textContent = currentLearning.linkUrl || '참고 자료 열기';
	    }

	    resetDifficultyPanel();
	    resetContentButtons();
	    updateProgressUI(currentLearning.progress);
	    updateFooterButtons();

	    if (currentLearning.progress >= 100) {
	        textCompleteBtn.disabled = true;
	        textCompleteBtn.textContent = '학습을 마쳤어요';
	        linkCompleteBtn.disabled = true;
	        linkCompleteBtn.textContent = '자료 확인 완료';
	    }

	    fakeVideoTimeText.textContent =
	        (currentLearning.progress === 100 ? currentLearning.duration : '00:00') +
	        ' / ' + currentLearning.duration;

	    modalOverlay.classList.add('open');
	    document.body.style.overflow = 'hidden';
	}

    function closeModal() {
        modalOverlay.classList.remove('open');
        document.body.style.overflow = '';
        selectedCard = null;
        currentLearning = null;
        resetDifficultyPanel();
        resetContentButtons();
    }

    function updateProgressUI(progress) {
        if (!currentLearning) return;

        currentLearning.progress = progress;
        currentProgressText.textContent = progress + '%';
        sideProgressFill.style.width = progress + '%';
        fakeVideoProgressFill.style.width = progress + '%';

        if (selectedCard) {
            selectedCard.dataset.progress = progress;

            if (progress >= 100) {
                selectedCard.dataset.status = '완료';
                syncCardStatus(selectedCard, '완료', progress);
            } else if (progress > 0) {
                selectedCard.dataset.status = '진행중';
                syncCardStatus(selectedCard, '진행중', progress);
            } else {
                selectedCard.dataset.status = '미시작';
                syncCardStatus(selectedCard, '미시작', progress);
            }
        }

        updateFooterButtons();
    }

    function updateFooterButtons() {
        if (!currentLearning) return;

        if (currentLearning.progress >= 100) {
            completeBtn.classList.add('show');
            saveBtn.classList.add('hide');
        } else {
            completeBtn.classList.remove('show');
            saveBtn.classList.remove('hide');
        }
    }

    function syncCardStatus(card, status, progress) {
        const statusBadge = card.querySelector('.learning-status-badge');
        const actionBtn = card.querySelector('.learning-action-btn');
        const typeIcon = card.querySelector('.learning-type-icon');
        const inlineProgress = card.querySelector('.learning-inline-progress');
        const infoBox = card.querySelector('.learning-info');

        statusBadge.classList.remove('done', 'progress', 'pending');
        typeIcon.classList.remove('done', 'progress', 'pending');

        if (status === '완료') {
            statusBadge.classList.add('done');
            typeIcon.classList.add('done');
            statusBadge.textContent = '완료';
            actionBtn.className = 'learning-action-btn done open-learning-btn';
            actionBtn.textContent = '학습완료';
        } else if (status === '진행중') {
            statusBadge.classList.add('progress');
            typeIcon.classList.add('progress');
            statusBadge.textContent = '진행중';
            actionBtn.className = 'learning-action-btn continue open-learning-btn';
            actionBtn.textContent = '이어보기';
        } else {
            statusBadge.classList.add('pending');
            typeIcon.classList.add('pending');
            statusBadge.textContent = '미시작';
            actionBtn.className = 'learning-action-btn start open-learning-btn';
            actionBtn.textContent = '학습 시작';
        }

        if (status === '진행중' && progress > 0) {
            if (!inlineProgress) {
                const progressWrap = document.createElement('div');
                progressWrap.className = 'learning-inline-progress';
                progressWrap.innerHTML =
                    '<div class="learning-inline-progress-bar">' +
                        '<div class="learning-inline-progress-fill" style="width:' + progress + '%;"></div>' +
                    '</div>' +
                    '<span class="learning-inline-progress-text">' + progress + '%</span>';

                infoBox.appendChild(progressWrap);
            } else {
                inlineProgress.querySelector('.learning-inline-progress-fill').style.width = progress + '%';
                inlineProgress.querySelector('.learning-inline-progress-text').textContent = progress + '%';
            }
        } else if (inlineProgress) {
            inlineProgress.remove();
        }
    }

    function resetDifficultyPanel() {
        selectedDifficultyReason = '';
        difficultyIntroPanel.style.display = 'block';
        difficultyFormPanel.classList.remove('open');
        difficultyReasonButtons.forEach(function (btn) {
            btn.classList.remove('active');
        });
        difficultyMessageInput.value = '';
        difficultyMessageInput.classList.remove('show');
    }

    tabButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            tabButtons.forEach(function (button) {
                button.classList.remove('active');
            });
            btn.classList.add('active');
            applyTabFilter(btn.dataset.tab);
        });
    });

    document.querySelectorAll('.learning-card').forEach(function (card) {
        card.addEventListener('click', function () {
            openModalFromCard(card);
        });
    });

    document.querySelectorAll('.open-learning-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const card = btn.closest('.learning-card');
            openModalFromCard(card);
        });
    });

    modalCloseBtn.addEventListener('click', closeModal);

    modalOverlay.addEventListener('click', function (e) {
        if (e.target === modalOverlay) {
            closeModal();
        }
    });

    fakeVideoPlayBtn.addEventListener('click', function () {
        if (!currentLearning) return;
        updateProgressUI(100);
        fakeVideoTimeText.textContent = currentLearning.duration + ' / ' + currentLearning.duration;
        showToast('동영상 시청을 완료했습니다.', 'success');
    });

    fakeVideoProgressBar.addEventListener('click', function () {
        if (!currentLearning) return;
        updateProgressUI(100);
        fakeVideoTimeText.textContent = currentLearning.duration + ' / ' + currentLearning.duration;
        showToast('동영상 시청을 완료했습니다.', 'success');
    });

    textCompleteBtn.addEventListener('click', function () {
        updateProgressUI(100);
        textCompleteBtn.textContent = '학습을 마쳤어요';
        textCompleteBtn.disabled = true;
        showToast('학습 진도가 100%로 업데이트되었습니다.', 'success');
    });

    linkCompleteBtn.addEventListener('click', function () {
        updateProgressUI(100);
        linkCompleteBtn.textContent = '자료 확인 완료';
        linkCompleteBtn.disabled = true;
        showToast('참고 자료 확인이 완료되었습니다.', 'success');
    });

    difficultyOpenBtn.addEventListener('click', function () {
        difficultyIntroPanel.style.display = 'none';
        difficultyFormPanel.classList.add('open');
    });

    difficultyBackBtn.addEventListener('click', function () {
        resetDifficultyPanel();
    });

    difficultyReasonButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            difficultyReasonButtons.forEach(function (button) {
                button.classList.remove('active');
            });

            btn.classList.add('active');
            selectedDifficultyReason = btn.dataset.reason || '';

            if (selectedDifficultyReason === '기타 (선생님께 남길 말)') {
                difficultyMessageInput.classList.add('show');
            } else {
                difficultyMessageInput.classList.remove('show');
                difficultyMessageInput.value = '';
            }
        });
    });

    difficultySubmitBtn.addEventListener('click', function () {
        if (!selectedDifficultyReason) {
            showToast('어려운 이유를 선택해주세요.', 'error');
            return;
        }

        if (selectedDifficultyReason === '기타 (선생님께 남길 말)' && !difficultyMessageInput.value.trim()) {
            showToast('선생님께 남길 말을 입력해주세요.', 'error');
            return;
        }

        showToast('선생님께 학습 어려움이 전달되었습니다.', 'success');
        resetDifficultyPanel();
    });

    saveBtn.addEventListener('click', function () {
        if (!currentLearning) return;

        if (currentLearning.progress === 0) {
            updateProgressUI(15);
        }

        showToast('학습 진행 상태가 안전하게 저장되었습니다.', 'success');
        closeModal();
    });

    completeBtn.addEventListener('click', function () {
        updateProgressUI(100);
        showToast('학습이 완료 처리되었습니다.', 'success');
        closeModal();
    });

    applyTabFilter(activeTab);
});