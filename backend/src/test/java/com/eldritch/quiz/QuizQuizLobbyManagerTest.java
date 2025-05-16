package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuizQuizLobbyManagerTest {

    @Test
    void should_return_available_lobby_when_called_first_time() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // When
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        QuizLobby result = quizLobbyManager.getAvailableLobby();

        // Then
        assertThat(result).isNotNull();
        verify(messagingTemplate, never()).convertAndSend(anyString(), (Object) any());
    }

    @Test
    void should_return_same_lobby_when_called_multiple_times() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);

        // When
        QuizLobby result1 = quizLobbyManager.getAvailableLobby();
        QuizLobby result2 = quizLobbyManager.getAvailableLobby();

        // Then
        assertThat(result1).isEqualTo(result2);
    }

    @Test
    void should_return_lobby_by_id_when_available() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        QuizLobby quizLobby = quizLobbyManager.getAvailableLobby();
        String lobbyId = quizLobby.getId();

        // When
        QuizLobby result = quizLobbyManager.getLobby(lobbyId);

        // Then
        assertThat(result).isEqualTo(quizLobby);
    }

    @Test
    void should_return_lobby_by_id_when_active() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        QuizLobby quizLobby = quizLobbyManager.getAvailableLobby();
        String lobbyId = quizLobby.getId();
        quizLobbyManager.lobbyStartedGame();

        // When
        QuizLobby result = quizLobbyManager.getLobby(lobbyId);

        // Then
        assertThat(result).isEqualTo(quizLobby);
    }

    @Test
    void should_move_lobby_to_active_when_game_starts() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        QuizLobby initialQuizLobby = quizLobbyManager.getAvailableLobby();

        // When
        quizLobbyManager.lobbyStartedGame();

        // Then
        QuizLobby newQuizLobby = quizLobbyManager.getAvailableLobby();
        assertThat(newQuizLobby).isNotEqualTo(initialQuizLobby);
        assertThat(quizLobbyManager.getLobby(initialQuizLobby.getId())).isEqualTo(initialQuizLobby);
    }

    @Test
    void should_remove_lobby_when_cleanup_called() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        QuizLobby quizLobby = quizLobbyManager.getAvailableLobby();
        String lobbyId = quizLobby.getId();
        quizLobbyManager.lobbyStartedGame();

        // When
        quizLobbyManager.cleanupLobby(lobbyId);

        // Then
        assertThat(quizLobbyManager.getLobby(lobbyId)).isNull();
    }

    @Test
    void should_add_player_to_available_lobby_when_joining() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        String nickname = "test-player";

        // When
        QuizLobby result = quizLobbyManager.join(nickname);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLastJoinedPlayer()).isNotNull();
        assertThat(result.getLastJoinedPlayer().getNickname()).isEqualTo(nickname);
    }

    @Test
    void should_move_lobby_to_active_when_full_after_join() {
        // Given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobbyManager quizLobbyManager = new QuizLobbyManager(messagingTemplate);
        String nickname = "test-player";

        // Mock the QuizService to report quiz as running
        QuizLobby initialQuizLobby = quizLobbyManager.getAvailableLobby();
        QuizService mockQuizService = mock(QuizService.class);
        when(mockQuizService.isQuizRunning()).thenReturn(true);
        initialQuizLobby.getGameInstance().startQuiz();

        // When
        quizLobbyManager.join(nickname);

        // Then
        QuizLobby newQuizLobby = quizLobbyManager.getAvailableLobby();
        assertThat(newQuizLobby).isNotEqualTo(initialQuizLobby);
        assertThat(quizLobbyManager.getLobby(initialQuizLobby.getId())).isEqualTo(initialQuizLobby);
    }
}