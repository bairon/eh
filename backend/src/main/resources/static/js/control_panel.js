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
        const $gameNameElement = $('#game-name');
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
function updateControlPanel(gameSession) {
    if (gameSession) {
        $('#create-game-btn').prop('disabled', true);
        $('#join-game-btn').prop('disabled', true);
        $('#select-ancient-one-btn').prop('disabled', false);
        $('#select-investigator-btn').prop('disabled', false);
        if (gameSession && gameSession.player && gameSession.player.master) {
            $('#start-game-btn').prop('disabled', false);
        } else {
            $('#start-game-btn').prop('disabled', true);
        }
        $('#leave-game-btn').prop('disabled', false);
    } else {
        $('#create-game-btn').prop('disabled', false);
        $('#join-game-btn').prop('disabled', false);
        $('#select-ancient-one-btn').prop('disabled', true);
        $('#select-investigator-btn').prop('disabled', true);
        $('#start-game-btn').prop('disabled', true);
        $('#leave-game-btn').prop('disabled', true);
    }
}
// Hide Popups
function hidePopups() {
    $('#create-game-popup').hide();
    $('#join-game-popup').hide();
    $('#auth-popup').hide();
    $('#overlay').hide();
}
function log(message) {
    const $gameOutput = $('#game-output');
    $gameOutput.append($('<p>').text(message));
    $gameOutput.scrollTop($gameOutput[0].scrollHeight);
}

