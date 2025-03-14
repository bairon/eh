
//const canvas = document.getElementById('game-canvas');
//const ctx = canvas.getContext('2d');

// Game Logic (same as before)
function log(message) {
    const gameOutput = document.getElementById('game-output');
    const p = document.createElement('p');
    p.textContent = message;
    gameOutput.appendChild(p);
    gameOutput.scrollTop = gameOutput.scrollHeight;
}

async function setupGame() {
    log('Game setup complete.');
}

async function changeOmen() {
    log('Omen changed!');
}

async function triggerReckoning() {
    log('Reckoning triggered!');
}

async function drawMythos() {
    log('Mythos card drawn!');
}

async function showCreateGamePopup() {
    document.getElementById('create-game-popup').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
}
async function showJoinGamePopup() {
    document.getElementById('join-game-popup').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
    fetchAvailableGames(); // Fetch available games from the server
}
// Hide Popups
function hidePopups() {
    document.getElementById('create-game-popup').style.display = 'none';
    document.getElementById('join-game-popup').style.display = 'none';
    document.getElementById('overlay').style.display = 'none';
}


// Function to update the Lobby Panel
function updateLobbyPanel(gameSession) {
    const lobbyPanel = document.getElementById('lobby-panel');
    if (gameSession === undefined || gameSession.gameStatus !== 'LOBBY') {
        lobbyPanel.style.display = 'none';
    } else {
        lobbyPanel.style.display = 'block';
        const gameNameElement = document.getElementById('game-name');
        const selectedAncientOne = document.getElementById('selected-ancient-one');
        const playerList = document.getElementById('player-list');

        // Update the game name
        gameNameElement.textContent = gameSession.gameName;

        // Update the selected Ancient One
        if (gameSession.gameState.ancientOne) {
            selectedAncientOne.textContent = gameSession.gameState.ancientOne.name;
        } else {
            selectedAncientOne.textContent = 'N/A';
        }

        // Update the list of players
        playerList.innerHTML = ''; // Clear the list
        gameSession.players.forEach(player => {
            const li = document.createElement('li');
            li.textContent = `${player.name} (${player.investigatorName || 'N/A'})`;
            playerList.appendChild(li);
        });
    }
}

