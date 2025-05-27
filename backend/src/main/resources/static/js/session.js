let stompClient = null; // Store the stompClient globally
let lobbyInfo = null;
let lobbySubscription = null;
let gameSubscription = null;

function updateLobby(lobby) {
    if (!lobby) {
        // Cleanup when no lobby data
        unsubscribeFromLobby();
        lobbyInfo = undefined;
    } else {
        const previousLobbyId = lobbyInfo?.id;
        lobbyInfo = lobby;
        lobbyInfo.agent = lobbyInfo.agents.find(ag => ag.id === userData.id);

        // If user is no longer in this lobby, unsubscribe
        if (!lobbyInfo.agent) {
            unsubscribeFromLobby();
            alert("Kicked");
        }

        // Connect if not connected
        if ((!stompClient || !stompClient.connected) && lobbyInfo.agent) {
            connectToLobbyWebSocket(lobbyInfo.id);
        }
    }

    updateChatVisibility(lobbyInfo);
    updateControlPanel(lobbyInfo);
    updateLobbyPanel(lobbyInfo);
    drawGame(lobbyInfo);
}

function connectToLobbyWebSocket(lobbyId) {
    const socket = new SockJS('/eh-ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        // Store subscriptions to allow unsubscribing later
        lobbySubscription = stompClient.subscribe(`/topic/ehlobby/${lobbyId}`, function(message) {
            const lobby = JSON.parse(message.body);
            updateLobby(lobby);
        });

        gameSubscription = stompClient.subscribe(`/topic/ehgame/${lobbyId}`, function(message) {
            const state = JSON.parse(message.body);
            updateGame(state);
        });

        initializeChat(lobbyId);
    });
}

function unsubscribeFromLobby() {
    if (lobbySubscription) {
        lobbySubscription.unsubscribe();
        lobbySubscription = null;
    }
    if (gameSubscription) {
        gameSubscription.unsubscribe();
        gameSubscription = null;
    }

}


async function checkLobby() {
    try {
        const response = await $.ajax({
            url: '/api/lobby/check',
            method: 'POST'
        });
        updateLobby(response);
    } catch (error) {
        console.log("No active lobby found.");
        updateLobby();
    }
}


async function joinLobby(lobbyId) { // Renamed from joinGame
    try {
        const response = await $.ajax({
            url: '/api/lobby/join',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({lobbyId: lobbyId}),
        });
        updateLobby(response);
    } catch (error) {
        alert(error.responseText);
    }
    hideAllPopups();
}

async function fetchAvailableLobbies() { // Renamed from fetchAvailableGames
    try {
        const lobbies = await $.ajax({
            url: '/api/lobby/list',
            method: 'GET',
        });
        const $gameList = $('#game-list');
        $gameList.empty();
        lobbies.forEach(lobby => {
            const $lobbyItem = $('<div>')
                .addClass('game-item')
                .text(lobby.gameName || `Lobby ${lobby.id}`)
                .data('lobby-id', lobby.id)
                .on('click', function() {
                    $('.game-item').removeClass('selected');
                    $(this).addClass('selected');
                });
            $gameList.append($lobbyItem);
        });
    } catch (error) {
        console.error(error);
        alert('Error fetching lobbies.');
    }
}

async function createLobby() {
    const gameName = $('#game-name').val().trim();
    if (!gameName) {
        alert('Please enter a game name.');
        return;
    }

    try {
        const response = await $.ajax({
            url: '/api/lobby/create',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({gameName: gameName}),
        });
        updateLobby(response);
    } catch (error) {
        console.error('Failed to create lobby:', error);
        alert('Failed to create lobby. Please try again.');
    } finally {
        hideAllPopups();
    }
}

async function leaveLobby() { // Renamed from leaveGame
    if (confirm('Are you sure to Leave Lobby?')) {
        try {
            await $.ajax({
                url: '/api/lobby/leave',
                method: 'POST',
            });

            if (stompClient && stompClient.connected) {
                stompClient.disconnect();
                console.log('Disconnected from WebSocket');
            }

            updateLobby();
        } catch (error) {
            alert(error.responseText);
        }
    }
    hideAllPopups();
}

async function startGame() {
    await $.ajax({
        url: '/api/lobby/start',
        method: 'POST',
    });
}

function connectToLobbyWebSocket(lobbyId) { // Renamed from connectToWebSocket
    const socket = new SockJS('/eh-ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        lobbySubsctiption = stompClient.subscribe(`/topic/ehlobby/${lobbyId}`, function(message) {
            const lobby = JSON.parse(message.body);
            updateLobby(lobby);
        });
        gameSubscription = stompClient.subscribe(`/topic/ehgame/${lobbyId}`, function(message) {
            const state = JSON.parse(message.body);
            updateGame(state);
        });

        initializeChat(lobbyId);
    });
}
function unsubscribeFromLobby(lobbyId) {

}


