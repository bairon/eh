package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class LobbyTest {

    @Test
    public void should_return_correct_lobby_id() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        Lobby lobby = new Lobby(messagingTemplate);
        String lobbyId = lobby.getId();

        // then
        assertThat(lobbyId).isNotNull().isNotEmpty();
    }

    @Test
    public void should_add_player_and_return_player_instance() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        Lobby lobby = new Lobby(messagingTemplate);

        // when
        QuizPlayer result = lobby.addPlayer("testUser");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo("testUser");
        assertThat(lobby.getPlayers()).containsExactly(result);
        assertThat(lobby.getLastJoinedPlayer()).isEqualTo(result);
    }

    @Test
    public void should_remove_player_successfully() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        Lobby lobby = new Lobby(messagingTemplate);
        QuizPlayer player = lobby.addPlayer("testUser");

        // when
        lobby.removePlayer(player.getId());

        // then
        assertThat(lobby.getPlayers()).isEmpty();
    }

    @Test
    public void should_return_correct_game_instance() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        Lobby lobby = new Lobby(messagingTemplate);
        QuizService result = lobby.getGameInstance();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getLobbyId()).isEqualTo(lobby.getId());
    }

    @Test
    public void should_return_empty_players_when_no_players_added() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        Lobby lobby = new Lobby(messagingTemplate);
        Collection<QuizPlayer> players = lobby.getPlayers();

        // then
        assertThat(players).isEmpty();
    }

    @Test
    public void should_return_null_for_last_joined_player_when_no_players_added() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        Lobby lobby = new Lobby(messagingTemplate);
        QuizPlayer result = lobby.getLastJoinedPlayer();

        // then
        assertThat(result).isNull();
    }

    @Test
    public void should_return_false_for_isFull_when_quiz_not_running() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);

        // when
        Lobby lobby = new Lobby(messagingTemplate);
        boolean result = lobby.isFull();

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void should_return_true_for_isFull_when_quiz_is_running() {
        // given
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        Lobby lobby = new Lobby(messagingTemplate);
        lobby.getGameInstance().startQuiz();

        // when
        boolean result = lobby.isFull();

        // then
        assertThat(result).isTrue();
    }
}