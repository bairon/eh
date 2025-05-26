// ChatMessage type
const ChatMessage = {
    nickname: '', // Sender's nickname
    text: '',     // Message text
};
// Global variable to store the chat WebSocket subscription
let chatSubscription = null;


// Function to initialize chat
function initializeChat(lobbyId) {
    if (stompClient && stompClient.connected) {
        // Subscribe to the chat topic
        if (!chatSubscription) {
            chatSubscription = stompClient.subscribe(`/topic/chat/${lobbyId}`, function (message) {
                //console.log('Received chat:' + message);
                const chatMessage = JSON.parse(message.body);
                appendMessageToChat(chatMessage);
            });
        }
    }
    $('#chat-input').on('keypress', function (e) {
        if (e.which === 13) { // Enter key
            sendChatMessage();
        }
    });

    $('#chat-send').on('click', sendChatMessage);
}

// Function to append a message to the chat history
function appendMessageToChat(message) {
    const $chatHistory = $('#chat-history');
    const $message = $('<p>').text(`${message.nickname}: ${message.text}`);
    $chatHistory.append($message);
    $chatHistory.scrollTop($chatHistory[0].scrollHeight); // Auto-scroll to the bottom
}
// Function to send a chat message
function sendChatMessage() {
    const messageText = $('#chat-input').val().trim();
    if (messageText) {
        const message = {
            nickname: userData.nickname || 'Player', // ToDo should be removed
            text: messageText,
        };
        stompClient.send(`/app/chat/${lobbyInfo.id}`, {}, JSON.stringify(message));
        $('#chat-input').val(''); // Clear the input field
    }
}

// Handle Enter key and Send button click
// Update chat visibility based on game session
function updateChatVisibility(lobbyInfo) {
    if (lobbyInfo) {
        $('#chat-container').addClass('active');
        initializeChat(lobbyInfo.id); // Initialize chat for the session
    } else {
        $('#chat-container').removeClass('active');
        if (chatSubscription) {
            chatSubscription.unsubscribe(); // Unsubscribe from chat topic
            chatSubscription = null;
        }
    }
}
