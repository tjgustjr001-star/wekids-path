window.addEventListener('DOMContentLoaded', function () {

    const modalOverlay = document.getElementById('learningModalOverlay');
    const modalCloseBtn = document.getElementById('learningModalCloseBtn');
    const modalTitle = document.getElementById('modalLearningTitle');
    const modalTypeBadge = document.getElementById('modalTypeBadge');
    const modalRequiredBadge = document.getElementById('modalRequiredBadge');

    const videoContent = document.getElementById('learningVideoContent');
    const textContent = document.getElementById('learningTextContent');
    const linkContent = document.getElementById('learningLinkContent');

    const textTitle = document.getElementById('learningTextTitle');
    const learningLinkAnchor = document.getElementById('learningLinkAnchor');

    const currentProgressText = document.getElementById('learningCurrentProgressText');
    const sideProgressFill = document.getElementById('learningSideProgressFill');

    const textCompleteBtn = document.getElementById('learningTextCompleteBtn');
    const linkCompleteBtn = document.getElementById('learningLinkCompleteBtn');
    const completeBtn = document.getElementById('learningCompleteBtn');
    const saveBtn = document.getElementById('learningSaveBtn');

    const difficultyIntroPanel = document.getElementById('difficultyIntroPanel');
    const difficultyFormPanel = document.getElementById('difficultyFormPanel');
    const difficultyOpenBtn = document.getElementById('difficultyOpenBtn');
    const difficultyBackBtn = document.getElementById('difficultyBackBtn');
    const difficultyReasonButtons = document.querySelectorAll('.difficulty-reason-btn');
    const difficultyMessageInput = document.getElementById('difficultyMessageInput');
    const difficultySubmitBtn = document.getElementById('difficultySubmitBtn');

    const toastContainer = document.getElementById('learningToastContainer');

    const contextPath = window.appContextPath || '';
    const classId = window.studentClassId || '';

	let textScrollHandler = null;
    let selectedCard = null;
    let currentLearning = null;
    let selectedDifficultyReason = '';
    let ytPlayer = null;
    let ytSaveTimer = null;
    let ytApiReady = false;
    let autoCompleted = false;

    function ensureYoutubeApi() {
        if (window.YT && window.YT.Player) {
            ytApiReady = true;
            return Promise.resolve();
        }

        return new Promise(function (resolve) {
            const existing = document.getElementById('youtube-iframe-api');

            if (!existing) {
                const tag = document.createElement('script');
                tag.id = 'youtube-iframe-api';
                tag.src = 'https://www.youtube.com/iframe_api';
                document.head.appendChild(tag);
            }

            const prev = window.onYouTubeIframeAPIReady;
            window.onYouTubeIframeAPIReady = function () {
                ytApiReady = true;
                if (typeof prev === 'function') {
                    prev();
                }
                resolve();
            };

            const check = setInterval(function () {
                if (window.YT && window.YT.Player) {
                    clearInterval(check);
                    ytApiReady = true;
                    resolve();
                }
            }, 300);
        });
    }

    function postForm(url, data) {
        const body = Object.keys(data || {})
            .map(function (key) {
                return encodeURIComponent(key) + '=' + encodeURIComponent(data[key] == null ? '' : data[key]);
            })
            .join('&');

        return fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: body
        }).then(function (res) {
            if (!res.ok) {
                throw new Error('request failed');
            }
            return res.text();
        });
    }

    function showToast(message, type) {
        if (!toastContainer) return;

        const toast = document.createElement('div');
        toast.className = 'learning-toast ' + (type || 'success');
        toast.textContent = message;
        toastContainer.appendChild(toast);

        setTimeout(function () {
            toast.classList.add('show');
        }, 10);

        setTimeout(function () {
            toast.classList.remove('show');
            setTimeout(function () {
                if (toast.parentNode) {
                    toast.parentNode.removeChild(toast);
                }
            }, 250);
        }, 2200);
    }

    function escapeHtml(text) {
        return String(text || '')
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    function extractYoutubeVideoId(url) {
        if (!url) return '';

        try {
            const parsed = new URL(url);

            if (parsed.hostname.includes('youtu.be')) {
                return parsed.pathname.replace('/', '');
            }

            if (parsed.hostname.includes('youtube.com')) {
                return parsed.searchParams.get('v') || '';
            }
        } catch (e) {
            return '';
        }

        return '';
    }

    function setProgress(progress) {
        const safe = Math.max(0, Math.min(100, Number(progress) || 0));

        if (currentProgressText) {
            currentProgressText.textContent = safe + '%';
        }

        if (sideProgressFill) {
            sideProgressFill.style.width = safe + '%';
        }

        if (currentLearning) {
            currentLearning.progress = safe;
        }
    }

    function updateCardUI(card, learning) {
        if (!card || !learning) return;

        card.dataset.status = learning.status;
        card.dataset.progress = String(learning.progress || 0);
        card.dataset.lastPosition = String(learning.lastPosition || '0');

        const statusBadge = card.querySelector('.learning-status-badge');
        const actionBtn = card.querySelector('.open-learning-btn');
        const progressFill = card.querySelector('.learning-inline-progress-fill');
        const progressText = card.querySelector('.learning-inline-progress-text');
        const progressWrap = card.querySelector('.learning-inline-progress');

        if (statusBadge) {
            statusBadge.textContent = learning.status;
            statusBadge.classList.remove('done', 'progress', 'pending');

            if (learning.status === '완료') {
                statusBadge.classList.add('done');
            } else if (learning.status === '진행중') {
                statusBadge.classList.add('progress');
            } else {
                statusBadge.classList.add('pending');
            }
        }

        if (actionBtn) {
            actionBtn.classList.remove('start', 'continue', 'done');

            if (learning.status === '완료') {
                actionBtn.textContent = '학습완료';
                actionBtn.classList.add('done');
            } else if (learning.status === '진행중') {
                actionBtn.textContent = '이어보기';
                actionBtn.classList.add('continue');
            } else {
                actionBtn.textContent = '학습 시작';
                actionBtn.classList.add('start');
            }
        }

        if (learning.progress > 0) {
            if (progressWrap) progressWrap.style.display = '';
            if (progressFill) progressFill.style.width = learning.progress + '%';
            if (progressText) progressText.textContent = learning.progress + '%';
        } else {
            if (progressWrap) progressWrap.style.display = 'none';
        }
    }

    function resetDifficultyPanel() {
        selectedDifficultyReason = '';

        difficultyReasonButtons.forEach(function (btn) {
            btn.classList.remove('active');
        });

        if (difficultyMessageInput) {
            difficultyMessageInput.value = '';
            difficultyMessageInput.style.display = 'none';
            difficultyMessageInput.classList.remove('show');
        }

        if (difficultyIntroPanel) {
            difficultyIntroPanel.style.display = 'block';
        }

        if (difficultyFormPanel) {
            difficultyFormPanel.style.display = 'none';
        }
    }

    function destroyYoutubePlayer() {
        if (ytSaveTimer) {
            clearInterval(ytSaveTimer);
            ytSaveTimer = null;
        }

        if (ytPlayer && typeof ytPlayer.destroy === 'function') {
            ytPlayer.destroy();
        }

        ytPlayer = null;
    }

    function openModal() {
        if (modalOverlay) {
            modalOverlay.classList.add('open');
            document.body.style.overflow = 'hidden';
        }
    }

    function closeModal() {
		
		const scrollBox = getTextScrollBox();
		if (scrollBox && textScrollHandler) {
		    scrollBox.removeEventListener('scroll', textScrollHandler);
		}
		textScrollHandler = null;
		
        destroyYoutubePlayer();

        if (modalOverlay) {
            modalOverlay.classList.remove('open');
        }

        document.body.style.overflow = '';
        selectedCard = null;
        currentLearning = null;
        autoCompleted = false;
        resetDifficultyPanel();
    }

    function renderTypeContent(learning) {
        if (videoContent) videoContent.style.display = learning.type === 'video' ? '' : 'none';
        if (textContent) textContent.style.display = learning.type === 'text' ? '' : 'none';
        if (linkContent) linkContent.style.display = learning.type === 'link' ? '' : 'none';

        if (learning.type === 'text') {
            if (textTitle) {
                textTitle.textContent = learning.title || '지문 학습';
            }

			const textBody = learning.content || learning.textContent || '';

			if (learningTextTitle) {
			    learningTextTitle.textContent = learning.title || '';
			}

			if (learningTextContent) {
			    learningTextContent.textContent = textBody;
			}
        }

        if (learning.type === 'link') {
            const firstTitle = linkContent ? linkContent.querySelector('h3') : null;
            const firstDesc = linkContent ? linkContent.querySelector('p') : null;

            if (firstTitle) firstTitle.textContent = learning.title || '링크 학습';
            if (firstDesc) firstDesc.textContent = learning.content || '외부 링크 학습입니다.';

            if (learningLinkAnchor) {
                learningLinkAnchor.href = learning.linkUrl || '#';
                learningLinkAnchor.textContent = learning.linkUrl || '링크 열기';
                learningLinkAnchor.target = '_blank';
                learningLinkAnchor.rel = 'noopener noreferrer';
            }
        }
    }

    function createYoutubePlayer(learning) {
        const videoId = extractYoutubeVideoId(learning.linkUrl);

        if (!videoId || !videoContent) {
            videoContent.innerHTML = '<div class="learning-video-empty">유효한 유튜브 주소가 아닙니다.</div>';
            return;
        }

        videoContent.innerHTML =
            '<div class="learning-video-wrap">' +
                '<div id="youtubeLearningPlayer"></div>' +
                '<div class="learning-video-desc">' +
                    '<h3>' + escapeHtml(learning.title || '') + '</h3>' +
                    '<p>' + escapeHtml(learning.content || '설명이 없습니다.') + '</p>' +
                '</div>' +
            '</div>';

        ensureYoutubeApi().then(function () {
            ytPlayer = new YT.Player('youtubeLearningPlayer', {
                videoId: videoId,
                playerVars: {
                    rel: 0,
                    modestbranding: 1
                },
                events: {
                    onReady: function (event) {
                        const lastPosition = Number(learning.lastPosition || 0);

                        if (lastPosition > 0) {
                            try {
                                event.target.seekTo(lastPosition, true);
                            } catch (e) {}
                        }
                    },
                    onStateChange: function (event) {
                        if (event.data === YT.PlayerState.PLAYING) {
                            startYoutubeProgressTimer();
                        } else if (
                            event.data === YT.PlayerState.PAUSED ||
                            event.data === YT.PlayerState.ENDED
                        ) {
                            saveYoutubeProgress();
                        }

                        if (event.data === YT.PlayerState.ENDED) {
                            completeLearning();
                        }
                    }
                }
            });
        });
    }

    function startYoutubeProgressTimer() {
        if (ytSaveTimer) {
            clearInterval(ytSaveTimer);
        }

        ytSaveTimer = setInterval(function () {
            saveYoutubeProgress();
        }, 10000);
    }

    function saveYoutubeProgress() {
        if (!ytPlayer || !currentLearning || currentLearning.type !== 'video') {
            return Promise.resolve();
        }

        const duration = Math.floor(ytPlayer.getDuration ? ytPlayer.getDuration() : 0);
        const current = Math.floor(ytPlayer.getCurrentTime ? ytPlayer.getCurrentTime() : 0);

        if (!duration || duration <= 0) {
            return Promise.resolve();
        }

        const progressRate = Math.min(100, Math.floor((current / duration) * 100));

        currentLearning.lastPosition = String(current);
        currentLearning.progress = progressRate;
        currentLearning.status = progressRate >= 90 ? '완료' : '진행중';

        setProgress(progressRate);
        updateCardUI(selectedCard, currentLearning);

        return postForm(
            contextPath + '/student/classes/' + classId + '/learns/' + currentLearning.id + '/video-progress',
            {
                currentSecond: current,
                durationSecond: duration,
                progressRate: progressRate
            }
        ).then(function () {
            if (progressRate >= 90 && !autoCompleted) {
                autoCompleted = true;
                currentLearning.status = '완료';
                currentLearning.progress = 100;
                setProgress(100);
                updateCardUI(selectedCard, currentLearning);
                showToast('영상 학습이 완료 처리되었습니다.', 'success');
            }
        }).catch(function () {
            showToast('영상 진행 저장 중 오류가 발생했습니다.', 'error');
        });
    }
	function getTextScrollBox() {
	    return textContent;
	}

	function calculateTextProgress() {
	    const scrollBox = getTextScrollBox();
	    if (!scrollBox) return 0;

	    const scrollTop = scrollBox.scrollTop || 0;
	    const scrollHeight = scrollBox.scrollHeight || 0;
	    const clientHeight = scrollBox.clientHeight || 0;

	    if (scrollHeight <= clientHeight) {
	        return 100;
	    }

	    const maxScroll = scrollHeight - clientHeight;
	    const progressRate = Math.floor((scrollTop / maxScroll) * 100);

	    return Math.max(0, Math.min(100, progressRate));
	}

	function restoreTextPosition() {
	    if (!currentLearning || currentLearning.type !== 'text') return;

	    const scrollBox = getTextScrollBox();
	    if (!scrollBox) return;

	    const saved = Number(currentLearning.lastPosition || 0);
	    if (saved > 0) {
	        scrollBox.scrollTop = saved;
	    } else {
	        scrollBox.scrollTop = 0;
	    }
	}

	function bindTextScrollTracking() {
	    const scrollBox = getTextScrollBox();
	    if (!scrollBox) return;

	    if (textScrollHandler) {
	        scrollBox.removeEventListener('scroll', textScrollHandler);
	    }

	    textScrollHandler = function () {
	        if (!currentLearning || currentLearning.type !== 'text') return;

	        const progressRate = calculateTextProgress();
	        currentLearning.progress = progressRate;
	        currentLearning.lastPosition = String(scrollBox.scrollTop || 0);

	        if (progressRate >= 100) {
	            currentLearning.status = '완료';
	        } else if (progressRate > 0) {
	            currentLearning.status = '진행중';
	        }

	        setProgress(progressRate);
	        updateCardUI(selectedCard, currentLearning);
	    };

	    scrollBox.addEventListener('scroll', textScrollHandler);
	}

	function saveTextProgress() {
	    if (!currentLearning || currentLearning.type !== 'text') {
	        return Promise.resolve();
	    }

	    const scrollBox = getTextScrollBox();
	    const scrollTop = scrollBox ? Math.floor(scrollBox.scrollTop || 0) : 0;
	    const progressRate = calculateTextProgress();

	    currentLearning.lastPosition = String(scrollTop);
	    currentLearning.progress = progressRate;
	    currentLearning.status = progressRate >= 100 ? '완료' : (progressRate > 0 ? '진행중' : '미시작');

	    setProgress(progressRate);
	    updateCardUI(selectedCard, currentLearning);

	    return postForm(
	        contextPath + '/student/classes/' + classId + '/learns/' + currentLearning.id + '/text-progress',
	        {
	            scrollTop: scrollTop,
	            progressRate: progressRate
	        }
	    ).catch(function () {
	        showToast('지문 읽기 진행 저장 중 오류가 발생했습니다.', 'error');
	    });
	}
	
	function openLearning(card) {
	    if (!card) return;

	    selectedCard = card;
	    autoCompleted = false;

	    currentLearning = {
	        id: card.dataset.learningId || '',
	        title: card.dataset.title || '',
	        type: card.dataset.type || 'link',
	        required: card.dataset.required === 'true',
	        status: card.dataset.status || '미시작',
	        deadline: card.dataset.deadline || '',
	        duration: card.dataset.duration || '',
	        progress: Number(card.dataset.progress || 0),
	        content: card.dataset.content || '',
	        textContent: card.dataset.textContent || '',
	        linkUrl: card.dataset.linkUrl || '',
	        lastPosition: card.dataset.lastPosition || '0'
	    };

	    if (modalTitle) {
	        modalTitle.textContent = currentLearning.title;
	    }

	    if (modalTypeBadge) {
	        modalTypeBadge.textContent =
	            currentLearning.type === 'video' ? '영상 학습' :
	            currentLearning.type === 'text' ? '지문 읽기' :
	            currentLearning.type === 'link' ? '링크 학습' :
	            '파일 학습';
	    }

	    if (modalRequiredBadge) {
	        modalRequiredBadge.textContent = currentLearning.required ? '필수' : '선택';
	    }

	    setProgress(currentLearning.progress || 0);
	    renderTypeContent(currentLearning);
	    resetDifficultyPanel();

	    postForm(contextPath + '/student/classes/' + classId + '/learns/' + currentLearning.id + '/start', {})
	        .then(function () {
	            if (currentLearning && currentLearning.status === '미시작') {
	                currentLearning.status = '진행중';
	                updateCardUI(selectedCard, currentLearning);
	            }
	        })
	        .catch(function () {});

	    openModal();

	    if (currentLearning.type === 'video') {
	        createYoutubePlayer(currentLearning);
	    } else if (currentLearning.type === 'text') {
	        setTimeout(function () {
	            restoreTextPosition();
	            bindTextScrollTracking();
	            setProgress(currentLearning.progress || 0);
	        }, 50);
	    }
	}

	function saveAndCloseLearning() {
	    if (!currentLearning) {
	        closeModal();
	        return;
	    }

	    let savePromise = Promise.resolve();

	    if (currentLearning.type === 'video') {
	        savePromise = saveYoutubeProgress();
	    } else if (currentLearning.type === 'text') {
	        savePromise = saveTextProgress();
	    }

	    savePromise
	        .then(function () {
	            if (currentLearning && currentLearning.status === '미시작') {
	                currentLearning.status = '진행중';
	            }

	            updateCardUI(selectedCard, currentLearning);
	            showToast('진행 상태를 저장하고 학습창을 닫았습니다.', 'success');

	            setTimeout(function () {
	                closeModal();
	            }, 150);
	        })
	        .catch(function () {
	            showToast('진행 상태 저장 중 오류가 발생했습니다.', 'error');
	        });
	}

    function completeLearning() {
        if (!currentLearning) return;

        postForm(contextPath + '/student/classes/' + classId + '/learns/' + currentLearning.id + '/complete', {})
            .then(function () {
                currentLearning.progress = 100;
                currentLearning.status = '완료';
                currentLearning.lastPosition = currentLearning.lastPosition || '0';
                setProgress(100);
                updateCardUI(selectedCard, currentLearning);
                showToast('학습이 완료되었습니다.', 'success');
                closeModal();
            })
            .catch(function () {
                showToast('학습 완료 처리 중 오류가 발생했습니다.', 'error');
            });
    }

    if (modalCloseBtn) {
        modalCloseBtn.addEventListener('click', function () {
            saveAndCloseLearning();
        });
    }

    if (modalOverlay) {
        modalOverlay.addEventListener('click', function (e) {
            if (e.target === modalOverlay) {
                saveAndCloseLearning();
            }
        });
    }

    document.querySelectorAll('.open-learning-btn').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            openLearning(btn.closest('.learning-card'));
        });
    });

    document.querySelectorAll('.learning-card').forEach(function (card) {
        card.addEventListener('click', function () {
            openLearning(card);
        });
    });

    if (saveBtn) {
        saveBtn.addEventListener('click', function () {
            saveAndCloseLearning();
        });
    }

    if (textCompleteBtn) {
        textCompleteBtn.addEventListener('click', completeLearning);
    }

    if (linkCompleteBtn) {
        linkCompleteBtn.addEventListener('click', completeLearning);
    }

    if (completeBtn) {
        completeBtn.addEventListener('click', completeLearning);
    }

    if (difficultyOpenBtn) {
        difficultyOpenBtn.addEventListener('click', function () {
            if (difficultyIntroPanel) {
                difficultyIntroPanel.style.display = 'none';
            }

            if (difficultyFormPanel) {
                difficultyFormPanel.style.display = 'block';

                setTimeout(function () {
                    difficultyFormPanel.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }, 50);
            }
        });
    }

    if (difficultyBackBtn) {
        difficultyBackBtn.addEventListener('click', function () {
            if (difficultyIntroPanel) {
                difficultyIntroPanel.style.display = 'block';
            }

            if (difficultyFormPanel) {
                difficultyFormPanel.style.display = 'none';
            }

            selectedDifficultyReason = '';

            difficultyReasonButtons.forEach(function (btn) {
                btn.classList.remove('active');
            });

            if (difficultyMessageInput) {
                difficultyMessageInput.value = '';
                difficultyMessageInput.style.display = 'none';
                difficultyMessageInput.classList.remove('show');
            }
        });
    }

    difficultyReasonButtons.forEach(function (btn) {
        btn.addEventListener('click', function () {
            difficultyReasonButtons.forEach(function (item) {
                item.classList.remove('active');
            });

            btn.classList.add('active');
            selectedDifficultyReason = btn.dataset.reason || btn.dataset.tag || '';

            if (difficultyMessageInput) {
                if (selectedDifficultyReason === '기타 (선생님께 남길 말)') {
                    difficultyMessageInput.style.display = 'block';
                    difficultyMessageInput.classList.add('show');
                } else {
                    difficultyMessageInput.style.display = 'none';
                    difficultyMessageInput.classList.remove('show');
                    difficultyMessageInput.value = '';
                }
            }
        });
    });

    if (difficultySubmitBtn) {
        difficultySubmitBtn.addEventListener('click', function () {
            if (!currentLearning) return;

            if (!selectedDifficultyReason) {
                showToast('어려운 이유를 선택해주세요.', 'error');
                return;
            }

            let feedbackContent = selectedDifficultyReason;

            if (selectedDifficultyReason === '기타 (선생님께 남길 말)') {
                if (!difficultyMessageInput || !difficultyMessageInput.value.trim()) {
                    showToast('선생님께 남길 말을 입력해주세요.', 'error');
                    return;
                }
                feedbackContent = difficultyMessageInput.value.trim();
            }

            postForm(
                contextPath + '/student/classes/' + classId + '/learns/' + currentLearning.id + '/difficulty',
                { feedbackContent: feedbackContent }
            ).then(function () {
                showToast('선생님께 학습 어려움이 전달되었습니다.', 'success');
                resetDifficultyPanel();
            }).catch(function () {
                showToast('어려움 전달 중 오류가 발생했습니다.', 'error');
            });
        });
    }

});