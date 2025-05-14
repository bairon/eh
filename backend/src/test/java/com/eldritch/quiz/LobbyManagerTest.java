package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LobbyManagerTest {

    @Test
    void should_return_available_lobby_when_called_first_time() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // When
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        Lobby result = lobbyManager.getAvailableLobby();

        // Then
        assertThat(result).isNotNull();
        verify(messagingTemplate, never()).convertAndSend(anyString(), (Object) any());
    }

    @Test
    void should_return_same_lobby_when_called_multiple_times() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);

        // When
        Lobby result1 = lobbyManager.getAvailableLobby();
        Lobby result2 = lobbyManager.getAvailableLobby();

        // Then
        assertThat(result1).isEqualTo(result2);
    }

    @Test
    void should_return_lobby_by_id_when_available() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        Lobby lobby = lobbyManager.getAvailableLobby();
        String lobbyId = lobby.getId();

        // When
        Lobby result = lobbyManager.getLobby(lobbyId);

        // Then
        assertThat(result).isEqualTo(lobby);
    }

    @Test
    void should_return_lobby_by_id_when_active() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        Lobby lobby = lobbyManager.getAvailableLobby();
        String lobbyId = lobby.getId();
        lobbyManager.lobbyStartedGame();

        // When
        Lobby result = lobbyManager.getLobby(lobbyId);

        // Then
        assertThat(result).isEqualTo(lobby);
    }

    @Test
    void should_move_lobby_to_active_when_game_starts() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        Lobby initialLobby = lobbyManager.getAvailableLobby();

        // When
        lobbyManager.lobbyStartedGame();

        // Then
        Lobby newLobby = lobbyManager.getAvailableLobby();
        assertThat(newLobby).isNotEqualTo(initialLobby);
        assertThat(lobbyManager.getLobby(initialLobby.getId())).isEqualTo(initialLobby);
    }

    @Test
    void should_remove_lobby_when_cleanup_called() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        Lobby lobby = lobbyManager.getAvailableLobby();
        String lobbyId = lobby.getId();
        lobbyManager.lobbyStartedGame();

        // When
        lobbyManager.cleanupLobby(lobbyId);

        // Then
        assertThat(lobbyManager.getLobby(lobbyId)).isNull();
    }

    @Test
    void should_add_player_to_available_lobby_when_joining() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        String nickname = "test-player";

        // When
        Lobby result = lobbyManager.join(nickname);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLastJoinedPlayer()).isNotNull();
        assertThat(result.getLastJoinedPlayer().getNickname()).isEqualTo(nickname);
    }

    @Test
    void should_move_lobby_to_active_when_full_after_join() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        LobbyManager lobbyManager = new LobbyManager(messagingTemplate);
        String nickname = "test-player";

        // Mock the QuizService to report quiz as running
        Lobby initialLobby = lobbyManager.getAvailableLobby();
        QuizService mockQuizService = mock(QuizService.class);
        when(mockQuizService.isQuizRunning()).thenReturn(true);
        initialLobby.getGameInstance().startQuiz();

        // When
        lobbyManager.join(nickname);

        // Then
        Lobby newLobby = lobbyManager.getAvailableLobby();
        assertThat(newLobby).isNotEqualTo(initialLobby);
        assertThat(lobbyManager.getLobby(initialLobby.getId())).isEqualTo(initialLobby);
    }
}