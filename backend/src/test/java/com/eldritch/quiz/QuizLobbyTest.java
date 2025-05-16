package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class QuizLobbyTest {

    @Test
    public void should_return_correct_lobby_id() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        String lobbyId = quizLobby.getId();

        // then
        assertThat(lobbyId).isNotNull().isNotEmpty();
    }

    @Test
    public void should_add_player_and_return_player_instance() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);

        // when
        QuizPlayer result = quizLobby.addPlayer("testUser");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo("testUser");
        assertThat(quizLobby.getPlayers()).containsExactly(result);
        assertThat(quizLobby.getLastJoinedPlayer()).isEqualTo(result);
    }

    @Test
    public void should_remove_player_successfully() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        QuizPlayer player = quizLobby.addPlayer("testUser");

        // when
        quizLobby.removePlayer(player.getId());

        // then
        assertThat(quizLobby.getPlayers()).isEmpty();
    }

    @Test
    public void should_return_correct_game_instance() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        QuizService result = quizLobby.getGameInstance();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getLobbyId()).isEqualTo(quizLobby.getId());
    }

    @Test
    public void should_return_empty_players_when_no_players_added() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        Collection<QuizPlayer> players = quizLobby.getPlayers();

        // then
        assertThat(players).isEmpty();
    }

    @Test
    public void should_return_null_for_last_joined_player_when_no_players_added() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        QuizPlayer result = quizLobby.getLastJoinedPlayer();

        // then
        assertThat(result).isNull();
    }

    @Test
    public void should_return_false_for_isFull_when_quiz_not_running() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        boolean result = quizLobby.isFull();

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void should_return_true_for_isFull_when_quiz_is_running() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        QuizLobby quizLobby = new QuizLobby(messagingTemplate);
        quizLobby.getGameInstance().startQuiz();

        // when
        boolean result = quizLobby.isFull();

        // then
        assertThat(result).isTrue();
    }
}