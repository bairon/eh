package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {
    private final SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
    private final String testLobbyId = "test-lobby";

    @Test
    void should_add_player_when_nickname_provided() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        String testNickname = "testPlayer";

        // when
        QuizPlayer result = quizService.addPlayer(testNickname);

        // then
        assertNotNull(result);
        assertEquals(testNickname, result.getNickname());
        assertEquals(0, result.getScore());
        assertTrue(result.isActive());
        assertEquals(1, quizService.getPlayers().size());
    }

    @Test
    void should_return_player_when_valid_id_provided() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        QuizPlayer addedPlayer = quizService.addPlayer("testPlayer");
        String playerId = addedPlayer.getId();

        // when
        QuizPlayer result = quizService.getPlayer(playerId);

        // then
        assertNotNull(result);
        assertEquals(addedPlayer, result);
    }

    @Test
    void should_return_null_when_invalid_id_provided() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        quizService.addPlayer("testPlayer");
        String invalidId = "invalid-id";

        // when
        QuizPlayer result = quizService.getPlayer(invalidId);

        // then
        assertNull(result);
    }

    @Test
    void should_return_all_active_players() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        quizService.addPlayer("player1");
        quizService.addPlayer("player2");

        // when
        ArrayList<QuizPlayer> result = quizService.getActivePlayers();

        // then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(QuizPlayer::isActive));
    }

    @Test
    void should_start_quiz_when_minimum_players_joined() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        quizService.addPlayer("player1");

        // when
        boolean result = quizService.isQuizRunning();

        // then
        assertTrue(result);
    }

    @Test
    void should_not_process_answer_when_quiz_not_running() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        QuizPlayer player = quizService.addPlayer("player1");
        quizService.startQuiz(); // Ensure quiz is running
        quizService.endQuiz(player); // End quiz to make it not running
        int answerIndex = 1;

        // when
        quizService.processAnswer(player.getId(), answerIndex);

        // then
        assertEquals(0, player.getScore()); // Score should not change
    }

    @Test
    void should_process_correct_answer_and_increment_score() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        QuizPlayer player = quizService.addPlayer("player1");
        quizService.startQuiz();

        // Wait for question to be set
        QuizQuestion question = null;
        for (int i = 0; i < 10; i++) { // Try for up to 1 second
            question = quizService.getCurrentQuestion();
            System.out.println(question);
            if (question != null) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        assertNotNull(question, "Question should be available after quiz start");

        int correctAnswerIndex = question.getCorrectOption();

        // when
        quizService.processAnswer(player.getId(), correctAnswerIndex);

        // then
        assertEquals(1, player.getScore());
    }

    @Test
    void should_not_process_multiple_answers_from_same_player() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        QuizPlayer player = quizService.addPlayer("player1");
        quizService.startQuiz();

        // Wait briefly to ensure currentQuestion is set
        try {
            Thread.sleep(100); // Small delay to let quiz thread initialize
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        QuizQuestion question = quizService.getCurrentQuestion();
        assertNotNull(question, "Current question should not be null");
        int answerIndex = question.getCorrectOption();

        // when
        quizService.processAnswer(player.getId(), answerIndex); // First answer
        quizService.processAnswer(player.getId(), answerIndex); // Second answer

        // then
        assertEquals(1, player.getScore()); // Only first answer should count
    }

    @Test
    void should_end_quiz_when_player_reaches_win_score() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        QuizPlayer player = quizService.addPlayer("player1");
        quizService.startQuiz();

        // Wait for question to be set
        QuizQuestion question = null;
        for (int i = 0; i < 10; i++) { // Try for up to 1 second
            question = quizService.getCurrentQuestion();
            if (question != null) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        assertNotNull(question, "Question should be available after quiz start");

        // Simulate winning by directly setting score
        player.setScore(QuizService.QUIZ_WIN_SCORE - 1);
        quizService.processAnswer(player.getId(), question.getCorrectOption());

        // when
        boolean isQuizRunning = quizService.isQuizRunning();

        // then
        assertFalse(isQuizRunning);
        assertEquals(0, quizService.getPlayers().size()); // Players should be cleared
    }

    @Test
    void should_return_current_question_when_quiz_is_running() {
        // given
        QuizService quizService = new QuizService(messagingTemplate, testLobbyId);
        quizService.addPlayer("player1");

        // Wait for question to be set
        QuizQuestion result = null;
        for (int i = 0; i < 10; i++) { // Try for up to 1 second
            result = quizService.getCurrentQuestion();
            if (result != null) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // then
        assertNotNull(result);
        assertFalse(result.getText().isEmpty());
        assertFalse(result.getOptions().isEmpty());
        assertTrue(result.getCorrectOption() >= 0);
    }

    @Test
    void should_return_lobby_id() {
        // given
        String expectedLobbyId = "test-lobby-123";
        QuizService quizService = new QuizService(messagingTemplate, expectedLobbyId);

        // when
        String result = quizService.getLobbyId();

        // then
        assertEquals(expectedLobbyId, result);
    }
}