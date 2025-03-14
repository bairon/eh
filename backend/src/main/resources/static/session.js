let gameSession = undefined, connected = false;

function updateGame(session) {
    if (session === undefined) {
        gameSession = undefined;
    } else {
        gameSession = session;
        if (!connected) {
            connectToWebSocket(gameSession.sessionId);
        }
    }
    const sessionLog = document.getElementById('game-session');
    if (gameSession === undefined) {
        sessionLog.innerText = '';
    } else {
        sessionLog.innerText = JSON.stringify(gameSession);
    }
    updateLobbyPanel(gameSession);
}

// Check and start a game session on page load
window.addEventListener('load', checkGame);

async function checkGame() {
    const response = await fetch('/api/game/check')
    if (!response.ok) {
        alert(await response.text());
    } else {
        try {
            let json = await response.json();
            updateGame(json);
        } catch (error) {
            console.log("No active game found.");
            updateGame();
        }
    }
}
// Handle Join Game Selection
async function joinGame(sessionId) {
    const playerName = prompt('Enter your name:'); // Prompt for player name
    if (playerName) {
        const response = await fetch('/api/game/join', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({playerName: playerName, sessionId: sessionId})
        });
        if (!response.ok) {
            alert(await response.text());
        } else {
            updateGame(await response.json());
        }
    }
    hidePopups();
}


// Fetch Available Games
async function fetchAvailableGames() {
    try {
        const response = await fetch('/api/game/list');
        const games = await response.json();
        const gameList = document.getElementById('game-list');
        gameList.innerHTML = ''; // Clear the list
        games.forEach(game => {
            const gameItem = document.createElement('div');
            gameItem.className = 'game-item';
            gameItem.textContent = game.gameName;
            gameItem.addEventListener('click', () => joinGame(game.sessionId));
            gameList.appendChild(gameItem);
        });
    } catch (error) {
        console.error(error);
        alert('Error fetching games.');
    }
}

// Function to create a new game session
async function createGameSession() {
    const playerName = prompt('Enter your name:'); // Prompt for player name
    if (playerName) {
        const gameName = document.getElementById('game-name').value.trim();
        if (gameName) {
            const response = await fetch('/api/game/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({gameName: gameName, playerName: playerName}),
            });
            if (!response.ok) {
                prompt(await response.text());
            } else {
                updateGame(await response.json());
            }
        }
    }
    hidePopups();
}

async function leaveGame() {
    if (confirm('Are you sure to Leave Game?')) {
        const response = await fetch('/api/game/leave', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (!response.ok) {
            alert(await response.text());
        } else {
            updateGame();
        }
    }
    hidePopups();
}

// Function to connect to the WebSocket server
function connectToWebSocket(sessionId) {
    const socket = new SockJS('/eldritch-websocket');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame)
        connected = true;
        // Subscribe to the game state updates for this session
        stompClient.subscribe(`/topic/gameSession/${sessionId}`, function (message) {
            const gameSession = JSON.parse(message.body);
            updateGame(gameSession);
        });
    });
}