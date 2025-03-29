function showCreateGamePopup() {
    showPopup('#create-game-popup');
}

function showJoinGamePopup() {
    showPopup('#join-game-popup');
    fetchAvailableGames();

    // Handle Join button click
    $('#join-game-join').off('click').on('click', function () {
        const selectedGame = $('#game-list .game-item.selected');
        if (selectedGame.length > 0) {
            const sessionId = selectedGame.data('session-id');
            joinGame(sessionId);
        } else {
            alert('Please select a game to join.');
        }
    });

    // Handle Cancel button click
    $('#join-game-cancel').off('click').on('click', function () {
        hideAllPopups();
    });
}

function updateLobbyPanel(gameSession) {
    const $lobbyPanel = $('#lobby-panel');
    if (gameSession === undefined || gameSession.gameStatus !== 'LOBBY') {
        $lobbyPanel.hide();
    } else {
        $lobbyPanel.show();
        const $gameNameElement = $('#lobby-game-name');
        const $selectedAncientOne = $('#selected-ancient-one');
        const $playerList = $('#player-list');

        // Update the game name
        $gameNameElement.text(gameSession.gameName);

        // Update the selected Ancient One
        if (gameSession.gameState.ancientId) {
            $selectedAncientOne.text(staticData[gameSession.gameState.ancientId + '.name']);
        } else {
            $selectedAncientOne.text('N/A');
        }

        // Update the list of players
        $playerList.empty(); // Clear the list
        gameSession.players.forEach(player => {
            const investigatorName = player.investigatorId ? staticData[player.investigatorId + '.name'] : 'N/A';
            const $li = $('<li>').addClass('player-item');

            // Player info container
            const $playerInfo = $('<div>').addClass('player-info').text(`${player.name} (${investigatorName})`);
            $li.append($playerInfo);

            // Add kick button if current user is master and it's not themselves
            if (gameSession.player && gameSession.player.master && player.id !== gameSession.player.id) {
                const $kickBtn = $('<button>')
                    .addClass('kick-btn')
                    .text('Kick')
                    .click(() => kickPlayer(player.id));
                $li.append($kickBtn);
            }

            $playerList.append($li);
        });
    }
}

async function kickPlayer(playerId) {
    if (!confirm('Are you sure you want to kick this player?')) {
        return;
    }

    try {
        const response = await fetch(`/api/game/kick`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            data: JSON.stringify({playerId: playerId}),
        });

        if (!response.ok) {
            throw new Error('Failed to kick player');
        }

        // Refresh the lobby panel after successful kick
        fetchGameSession();
    } catch (error) {
        console.error('Error kicking player:', error);
        alert('Failed to kick player');
    }
}

function updateControlPanel() {
    const isAuth = isAuthenticated();
    const inGame = !!gameSession;
    const ongoing = gameSession && gameSession.gameStatus === 'ONGOING';
    const isMaster = inGame && gameSession.player && gameSession.player.master;
    const isAllInvestigatorsChosen = gameSession && gameSession.players.every(player =>
        player.investigatorId && player.investigatorId.trim() !== ''
    );
    const enabling = {
        '#create-game-btn': !inGame && isAuth,
        '#join-game-btn': !inGame && isAuth,
        '#select-ancient-one-btn': inGame && isAuth,
        '#select-investigator-btn': inGame && isAuth,
        '#start-game-btn': inGame && isAuth && isMaster && isAllInvestigatorsChosen,
        '#leave-game-btn': inGame && isAuth,
        '#chat-input': inGame,
        '#chat-send': inGame
    };

    Object.entries(enabling).forEach(([selector, enabled]) => {
        $(selector).prop('disabled', !enabled);
        if (ongoing ) {
            $(selector).hide();
        } else {
            $(selector).show();
        }
    });
    const visibility = {
        '#create-game-btn': !ongoing,
        '#join-game-btn': !ongoing,
        '#select-ancient-one-btn': !ongoing,
        '#select-investigator-btn': !ongoing,
        '#start-game-btn': !ongoing,
        '#leave-game-btn': !ongoing,
        '#chat-input': inGame,
        '#chat-send': inGame
    };
    Object.entries(visibility).forEach(([selector, visible]) => {
        if (visible ) {
            $(selector).show();
        } else {
            $(selector).hide();
        }
    });


}

async function updateStaticUI() {
    if (!staticData) {
        return;
    }

    // Update UI elements with static data
    const $lobbyPanel = $('#lobby-panel');
    const $caption = $('#caption');
    const $createGameBtn = $('#create-game-btn');
    const $joinGameBtn = $('#join-game-btn');
    const $selectAncientOneBtn = $('#select-ancient-one-btn');
    const $selectInvestigatorBtn = $('#select-investigator-btn');
    const $startGameBtn = $('#start-game-btn');
    const $leaveGameBtn = $('#leave-game-btn');
    const $chatInput = $('#chat-input');
    const $chatSend = $('#chat-send');
    const $lobbyNameCaption = $('#lobby-name-caption');
    const $lobbyGameName = $('#lobby-game-name');
    const $ancientOneCaption = $('#ancient-one-caption');
    const $selectedAncientOne = $('#selected-ancient-one');
    const $lobbyPlayersCaption = $('#lobby-players-caption');
    const $authLinkLogin = $('#auth-link-login');
    const $authLinkLogout = $('#auth-link-logout');
    const $loginInputLabel = $('#login-input-disabled-label');
    const $languageSelectLabel = $('#language-select-label');
    const $nicknameInputLabel = $('#nickname-input-label');
    const $preferencesPopupCaption = $('#preferences-popup-caption');

    // Update button texts
    $caption.text(staticData.gameName || 'Eldritch Horror');
    $createGameBtn.text(staticData.createGameBtn || 'Create Game');
    $joinGameBtn.text(staticData.joinGameBtn || 'Join Game');
    $selectAncientOneBtn.text(staticData.selectAncientOneBtn || 'Select Ancient One');
    $selectInvestigatorBtn.text(staticData.selectInvestigatorBtn || 'Select Investigator');
    $startGameBtn.text(staticData.startGameBtn || 'Start Game');
    $leaveGameBtn.text(staticData.leaveGameBtn || 'Leave Game');
    $chatInput.attr('placeholder', staticData.chatInputPlaceholder || 'Chat...');
    $chatSend.text(staticData.chatSendBtn || 'Send');

    // Update lobby panel
    $lobbyNameCaption.text(staticData.lobbyNameCaption || 'Lobby:');
    $lobbyGameName.text(staticData.lobbyGameName || 'Unnamed');
    $ancientOneCaption.text(staticData.ancientOneCaption || 'Ancient One');
    $selectedAncientOne.text(staticData.selectedAncientOne || 'Not Selected');
    $lobbyPlayersCaption.text(staticData.playersLabel || 'Players:');

    // Update auth link
    $authLinkLogin.text(staticData.authLinkLogin || 'Login');
    $authLinkLogout.text(staticData.authLinkLogout || 'Logout');

    // Update nickname display
    $loginInputLabel.text(staticData.loginInputLabel || 'Your Login:');
    $languageSelectLabel.text(staticData.languageSelectLabel || 'Language:')
    $nicknameInputLabel.text(staticData.nicknameDisplay || 'Nickname:');
    $preferencesPopupCaption.text(staticData.preferencesPopupCaption || 'Preferences:');

    // Update other static texts as needed
    console.log("Static UI updated successfully.");
}


function log(message) {
    const $gameOutput = $('#game-output');
    $gameOutput.append($('<p>').text(message));
    $gameOutput.scrollTop($gameOutput[0].scrollHeight);
}

// Show any popup with overlay
function showPopup(popupId) {
    $(popupId).show();
    $('#overlay').show();

    // Prevent clicks on popup from closing it
    $(popupId).off('click').on('click', function(e) {
        e.stopPropagation();
    });
}

// Hide all popups
function hideAllPopups() {
    $('.popup').hide();
    $('#overlay').hide();
}


