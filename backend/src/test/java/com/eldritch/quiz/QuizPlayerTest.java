package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuizPlayerTest {

    @Test
    void should_create_player_with_default_constructor() {
        // given
        QuizPlayer player = new QuizPlayer();

        // when
        String actualId = player.getId();
        String actualNickname = player.getNickname();
        int actualScore = player.getScore();
        boolean actualActive = player.isActive();

        // then
        assertNull(actualId);
        assertNull(actualNickname);
        assertEquals(0, actualScore);
        assertFalse(actualActive);
    }

    @Test
    void should_create_player_with_parameterized_constructor() {
        // given
        String expectedId = "player123";
        String expectedNickname = "QuizMaster";
        int expectedScore = 1500;
        boolean expectedActive = true;

        // when
        QuizPlayer player = new QuizPlayer(expectedId, expectedNickname, expectedScore, expectedActive);

        // then
        assertEquals(expectedId, player.getId());
        assertEquals(expectedNickname, player.getNickname());
        assertEquals(expectedScore, player.getScore());
        assertEquals(expectedActive, player.isActive());
    }

    @Test
    void should_set_and_get_id_correctly() {
        // given
        QuizPlayer player = new QuizPlayer();
        String expectedId = "newPlayer456";

        // when
        player.setId(expectedId);
        String actualId = player.getId();

        // then
        assertEquals(expectedId, actualId);
    }

    @Test
    void should_set_and_get_nickname_correctly() {
        // given
        QuizPlayer player = new QuizPlayer();
        String expectedNickname = "TriviaExpert";

        // when
        player.setNickname(expectedNickname);
        String actualNickname = player.getNickname();

        // then
        assertEquals(expectedNickname, actualNickname);
    }

    @Test
    void should_set_and_get_score_correctly() {
        // given
        QuizPlayer player = new QuizPlayer();
        int expectedScore = 2000;

        // when
        player.setScore(expectedScore);
        int actualScore = player.getScore();

        // then
        assertEquals(expectedScore, actualScore);
    }

    @Test
    void should_set_and_get_active_status_correctly() {
        // given
        QuizPlayer player = new QuizPlayer();
        boolean expectedActive = true;

        // when
        player.setActive(expectedActive);
        boolean actualActive = player.isActive();

        // then
        assertEquals(expectedActive, actualActive);
    }

    @Test
    void should_generate_correct_string_representation() {
        // given
        String id = "player789";
        String nickname = "Brainiac";
        int score = 3000;
        boolean active = false;
        QuizPlayer player = new QuizPlayer(id, nickname, score, active);
        String expectedString = String.format("Player[id=%s, nickname=%s, score=%d, active=%b]",
            id, nickname, score, active);

        // when
        String actualString = player.toString();

        // then
        assertEquals(expectedString, actualString);
    }
}
