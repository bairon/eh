package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class QuizControllerTest {

    @Test
    void should_return_player_and_lobby_info_when_joining_quiz() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);
        Lobby mockLobby = mock(Lobby.class);
        QuizPlayer mockPlayer = new QuizPlayer("player1", "testUser", 0, true);

        when(lobbyManager.join(anyString())).thenReturn(mockLobby);
        when(mockLobby.getLastJoinedPlayer()).thenReturn(mockPlayer);
        when(mockLobby.getId()).thenReturn("lobby123");
        when(mockLobby.getPlayers()).thenReturn(new ArrayList<>());

        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        ResponseEntity<Map<String, Object>> response = controller.joinQuiz("testUser");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("lobby123", responseBody.get("lobbyId"));
        assertEquals(mockPlayer, responseBody.get("player"));

        ArgumentCaptor<QuizMessage> messageCaptor = ArgumentCaptor.forClass(QuizMessage.class);
        verify(messagingTemplate).convertAndSend(eq("/topic/quiz/lobby123"), messageCaptor.capture());

        QuizMessage sentMessage = messageCaptor.getValue();
        assertEquals(QuizMessage.MessageType.JOIN, sentMessage.getType());
        assertEquals("testUser joined the quiz", sentMessage.getContent());
    }

    @Test
    void should_return_bad_request_when_join_fails() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);

        when(lobbyManager.join(anyString())).thenThrow(new RuntimeException("Test exception"));

        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        ResponseEntity<Map<String, Object>> response = controller.joinQuiz("testUser");

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messagingTemplate, never()).convertAndSend(anyString(), (Object) any());
    }

    @Test
    void should_return_lobby_state_when_requested() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);
        Lobby mockLobby = mock(Lobby.class);
        ArrayList<QuizPlayer> players = new ArrayList<>();
        players.add(new QuizPlayer("player1", "user1", 0, true));

        when(lobbyManager.getLobby("lobby123")).thenReturn(mockLobby);
        when(mockLobby.getPlayers()).thenReturn(players);

        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        QuizMessage response = controller.sendLobbyState("lobby123");

        // Then
        assertNotNull(response);
        assertEquals(QuizMessage.MessageType.UPDATE, response.getType());
        assertEquals("Lobby state update", response.getContent());
        assertEquals(1, response.getPlayers().size());
        assertEquals("user1", response.getPlayers().get(0).getNickname());
    }

    @Test
    void should_return_null_when_lobby_not_found_for_state_request() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);

        when(lobbyManager.getLobby("invalid")).thenReturn(null);

        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        QuizMessage response = controller.sendLobbyState("invalid");

        // Then
        assertNull(response);
    }

    @Test
    void should_handle_reconnect_with_question_when_player_is_current() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);
        Lobby mockLobby = mock(Lobby.class);
        QuizService mockGame = mock(QuizService.class);
        QuizPlayer mockPlayer = new QuizPlayer("player1", "user1", 0, true);
        QuizQuestion mockQuestion = new QuizQuestion("Test?", List.of("A", "B"), 0);
        ArrayList<QuizPlayer> players = new ArrayList<>();
        players.add(mockPlayer);

        when(lobbyManager.getLobby("lobby123")).thenReturn(mockLobby);
        when(mockLobby.getGameInstance()).thenReturn(mockGame);
        when(mockGame.getPlayer("player1")).thenReturn(mockPlayer);
        when(mockLobby.getPlayers()).thenReturn(players);
        when(mockGame.isQuizRunning()).thenReturn(true);
        when(mockGame.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockGame.getCurrentQuestion()).thenReturn(mockQuestion);

        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        QuizMessage response = controller.handleRejoin("lobby123", "player1");

        // Then
        assertNotNull(response);
        assertEquals(QuizMessage.MessageType.REJOIN, response.getType());
        assertEquals("user1 rejoined", response.getContent());
        assertEquals(mockQuestion, response.getQuestion());
        assertEquals(1, response.getPlayers().size());
    }

    @Test
    void should_return_initial_state_message() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);
        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        QuizMessage response = controller.sendInitialState();

        // Then
        assertNotNull(response);
        assertEquals(QuizMessage.MessageType.UPDATE, response.getType());
        assertEquals("Please join a specific lobby", response.getContent());
    }

    @Test
    void should_process_answer_and_send_update() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = mock(LobbyManager.class);
        Lobby mockLobby = mock(Lobby.class);
        QuizService mockGame = mock(QuizService.class);

        when(lobbyManager.getLobby("lobby123")).thenReturn(mockLobby);
        when(mockLobby.getGameInstance()).thenReturn(mockGame);
        when(mockLobby.getPlayers()).thenReturn(new ArrayList<>());

        Map<String, Object> payload = new HashMap<>();
        payload.put("playerId", "player1");
        payload.put("answerIndex", 1);

        QuizController controller = new QuizController(lobbyManager, messagingTemplate);

        // When
        controller.processAnswer(payload, "lobby123");

        // Then
        verify(mockGame).processAnswer("player1", 1);
        verify(messagingTemplate).convertAndSend(eq("/topic/quiz/lobby123"), any(QuizMessage.class));
    }
}
