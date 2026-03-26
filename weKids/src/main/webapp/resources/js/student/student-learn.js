window.addEventListener('DOMContentLoaded', function () {
<<<<<<< HEAD
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

            const textPs = textContent ? textContent.querySelectorAll('p') : [];
            if (textPs.length > 0) {
                textPs[0].innerHTML = escapeHtml(learning.textContent || '').replace(/\n/g, '<br>');
            }
            if (textPs.length > 1) {
                textPs[1].innerHTML = escapeHtml(learning.content || '').replace(/\n/g, '<br>');
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

        if (currentLearning.type === 'video') {
            createYoutubePlayer(currentLearning);
        }

        openModal();
    }

    function saveAndCloseLearning() {
        if (!currentLearning) {
            closeModal();
            return;
        }

        saveYoutubeProgress()
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
=======
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
            progress: Number(card.dataset.progress || 0)
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
>>>>>>> refs/remotes/origin/brunch1
});