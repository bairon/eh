function showCreateGamePopup() {
    $('#create-game-popup').show();
    $('#overlay').show();
}

function showJoinGamePopup() {
    $('#join-game-popup').show();
    $('#overlay').show();
    fetchAvailableGames(); // Fetch available games from the server

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
        hidePopups();
    });
}

// Function to update the Lobby Panel
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
        if (gameSession.gameState.ancientOne) {
            $selectedAncientOne.text(gameSession.gameState.ancientOne.name);
        } else {
            $selectedAncientOne.text('N/A');
        }

        // Update the list of players
        $playerList.empty(); // Clear the list
        gameSession.players.forEach(player => {
            const $li = $('<li>').text(`${player.name} (${player.investigatorName || 'N/A'})`);
            $playerList.append($li);
        });
    }
}

function updateControlPanel() {
    const isAuth = isAuthenticated();
    const inGame = !!gameSession;
    const isMaster = inGame && gameSession.player && gameSession.player.master;
    const isAllInvestigatorsChosen = gameSession && gameSession.players.every(player =>
        player.investigatorName && player.investigatorName.trim() !== ""
    );
    const elements = {
        '#create-game-btn': !inGame && isAuth,
        '#join-game-btn': !inGame && isAuth,
        '#select-ancient-one-btn': inGame && isAuth,
        '#select-investigator-btn': inGame && isAuth,
        '#start-game-btn': inGame && isAuth && isMaster && isAllInvestigatorsChosen,
        '#leave-game-btn': inGame && isAuth,
        '#chat-input': isAuth,
        '#chat-send': isAuth
    };

    Object.entries(elements).forEach(([selector, enabled]) => {
        $(selector).prop('disabled', !enabled);
    });
}

// Hide Popups
function hidePopups() {
    $('#create-game-popup').hide();
    $('#join-game-popup').hide();
    $('#auth-popup').hide();
    $('#overlay').hide();
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
    const $lobbyGameName = $('#lobby-game-name');
    const $selectedAncientOne = $('#selected-ancient-one');
    const $playerList = $('#player-list');
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
    $lobbyPanel.find('h2').text(staticData.lobbyPanelTitle || 'Lobby:');
    $lobbyGameName.text(staticData.lobbyGameName || 'Unnamed');
    $selectedAncientOne.text(staticData.selectedAncientOne || 'Not Selected');
    $playerList.find('strong').text(staticData.playersLabel || 'Players:');

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

