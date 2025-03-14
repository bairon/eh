function showCreateGamePopup() {
    $('#create-game-popup').show();
    $('#overlay').show();
}

function showJoinGamePopup() {
    $('#join-game-popup').show();
    $('#overlay').show();
    fetchAvailableGames(); // Fetch available games from the server
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

// Hide Popups
function hidePopups() {
    $('#create-game-popup').hide();
    $('#join-game-popup').hide();
    $('#overlay').hide();
}
function log(message) {
    const $gameOutput = $('#game-output');
    $gameOutput.append($('<p>').text(message));
    $gameOutput.scrollTop($gameOutput[0].scrollHeight);
}

