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
    const $sessionLog = $('#game-session');
    if (gameSession === undefined) {
        $sessionLog.text('');
    } else {
        $sessionLog.text(JSON.stringify(gameSession));
    }
    updateLobbyPanel(gameSession);
}

// Check and start a game session on page load
$(window).on('load', checkGame);

async function checkGame() {
    try {
        const response = await $.ajax({
            url: '/api/game/check',
            method: 'GET',
        });
        updateGame(response);
    } catch (error) {
        console.log("No active game found.");
        updateGame();
    }
}

// Handle Join Game Selection
async function joinGame(sessionId) {
    const playerName = prompt('Enter your name:'); // Prompt for player name
    if (playerName) {
        try {
            const response = await $.ajax({
                url: '/api/game/join',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({playerName, sessionId}),
            });
            updateGame(response);
        } catch (error) {
            alert(error.responseText);
        }
    }
    hidePopups();
}

// Fetch Available Games
async function fetchAvailableGames() {
    try {
        const games = await $.ajax({
            url: '/api/game/list',
            method: 'GET',
        });
        const $gameList = $('#game-list');
        $gameList.empty(); // Clear the list
        games.forEach(game => {
            const $gameItem = $('<div>').addClass('game-item').text(game.gameName)
                .on('click', () => joinGame(game.sessionId));
            $gameList.append($gameItem);
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
        const gameName = $('#game-name').val().trim();
        if (gameName) {
            try {
                const response = await $.ajax({
                    url: '/api/game/create',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({gameName, playerName}),
                });
                updateGame(response);
            } catch (error) {
                alert(error.responseText);
            }
        }
    }
    hidePopups();
}

async function leaveGame() {
    if (confirm('Are you sure to Leave Game?')) {
        try {
            await $.ajax({
                url: '/api/game/leave',
                method: 'POST',
            });
            updateGame();
        } catch (error) {
            alert(error.responseText);
        }
    }
    hidePopups();
}

async function startGame() {
    await $.ajax({
        url: '/api/game/start',
        method: 'POST',
    });
}

// Function to connect to the WebSocket server
function connectToWebSocket(sessionId) {
    const socket = new SockJS('/eldritch-websocket');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        connected = true;
        // Subscribe to the game state updates for this session
        stompClient.subscribe(`/topic/gameSession/${sessionId}`, function (message) {
            const gameSession = JSON.parse(message.body);
            updateGame(gameSession);
        });
    });
}