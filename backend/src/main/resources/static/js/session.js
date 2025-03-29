let gameSession = undefined;
let stompClient = null; // Store the stompClient globally


function updateGame(session) {
    if (!session) {
        gameSession = undefined;
    } else {
        gameSession = session;
        gameSession.player = gameSession.players.find(p => p.playerId === userData.id);
        if (!stompClient || !stompClient.connected) {
            connectToWebSocket(gameSession.sessionId);
        }
    }


    updateChatVisibility(gameSession);
    updateControlPanel(gameSession);
    updateLobbyPanel(gameSession);
    drawGame(gameSession);
}

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
    if (userData) {
        try {
            const response = await $.ajax({
                url: '/api/game/join',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({sessionId: sessionId}),
            });
            updateGame(response);
        } catch (error) {
            alert(error.responseText);
        }
    }
    hideAllPopups();
}

async function fetchAvailableGames() {
    try {
        const games = await $.ajax({
            url: '/api/game/list',
            method: 'GET',
        });
        const $gameList = $('#game-list');
        $gameList.empty(); // Clear the list
        games.forEach(game => {
            const $gameItem = $('<div>')
                .addClass('game-item')
                .text(game.gameName)
                .data('session-id', game.sessionId) // Store session ID for joining
                .on('click', function () {
                    // Highlight the selected game
                    $('.game-item').removeClass('selected');
                    $(this).addClass('selected');
                });
            $gameList.append($gameItem);
        });
    } catch (error) {
        console.error(error);
        alert('Error fetching games.');
    }
}
async function createGameSession() {
    const gameName = $('#game-name').val().trim();

    if (!gameName) {
        alert('Please enter a game name.');
        return;
    }

    try {
        const response = await $.ajax({
            url: '/api/game/create',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ gameName: gameName}),
        });
        updateGame(response);
    } catch (error) {
        console.error('Failed to create game session:', error);
        alert('Failed to create game session. Please try again.');
    } finally {
        hideAllPopups();
    }
}
async function leaveGame() {
    if (confirm('Are you sure to Leave Game?')) {
        try {
            await $.ajax({
                url: '/api/game/leave',
                method: 'POST',
            });

            // Disconnect from WebSocket if connected
            if (stompClient && stompClient.connected) {
                stompClient.disconnect(); // Disconnect from WebSocket
                console.log('Disconnected from WebSocket');
            }

            // Update the game state
            updateGame();
        } catch (error) {
            alert(error.responseText);
        }
    }
    hideAllPopups();
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
    stompClient = Stomp.over(socket); // Store stompClient globally

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Subscribe to the game state updates for this session
        stompClient.subscribe(`/topic/gameSession/${sessionId}`, function (message) {
            const gameSession = JSON.parse(message.body);
            updateGame(gameSession);
        });

        // Initialize chat after WebSocket connection is established
        initializeChat(sessionId);
    });
}


