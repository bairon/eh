package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class QuizMessageTest {

    @Test
    void should_set_and_get_message_type() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        QuizMessage.MessageType expectedType = QuizMessage.MessageType.JOIN;

        // when
        quizMessage.setType(expectedType);
        QuizMessage.MessageType actualType = quizMessage.getType();

        // then
        assertThat(actualType).isEqualTo(expectedType);
    }

    @Test
    void should_set_and_get_content() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        String expectedContent = "Welcome to the quiz!";

        // when
        quizMessage.setContent(expectedContent);
        String actualContent = quizMessage.getContent();

        // then
        assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    void should_set_and_get_player() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        QuizPlayer expectedPlayer = new QuizPlayer("player1", "John", 0, true);

        // when
        quizMessage.setPlayer(expectedPlayer);
        QuizPlayer actualPlayer = quizMessage.getPlayer();

        // then
        assertThat(actualPlayer).isEqualTo(expectedPlayer);
    }

    @Test
    void should_set_and_get_question() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        QuizQuestion expectedQuestion = new QuizQuestion(
            "What is 2+2?",
            new ArrayList<>(java.util.Arrays.asList("3", "4", "5")),
            1
        );

        // when
        quizMessage.setQuestion(expectedQuestion);
        QuizQuestion actualQuestion = quizMessage.getQuestion();

        // then
        assertThat(actualQuestion).isEqualTo(expectedQuestion);
    }

    @Test
    void should_set_and_get_selected_answer() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        Integer expectedAnswer = 2;

        // when
        quizMessage.setSelectedAnswer(expectedAnswer);
        Integer actualAnswer = quizMessage.getSelectedAnswer();

        // then
        assertThat(actualAnswer).isEqualTo(expectedAnswer);
    }

    @Test
    void should_set_and_get_correct_status() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        Boolean expectedCorrect = true;

        // when
        quizMessage.setCorrect(expectedCorrect);
        Boolean actualCorrect = quizMessage.getCorrect();

        // then
        assertThat(actualCorrect).isEqualTo(expectedCorrect);
    }

    @Test
    void should_set_and_get_winner() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        QuizPlayer expectedWinner = new QuizPlayer("winner1", "Alice", 100, true);

        // when
        quizMessage.setWinner(expectedWinner);
        QuizPlayer actualWinner = quizMessage.getWinner();

        // then
        assertThat(actualWinner).isEqualTo(expectedWinner);
    }

    @Test
    void should_set_and_get_players_list() {
        // given
        QuizMessage quizMessage = new QuizMessage();
        ArrayList<QuizPlayer> expectedPlayers = new ArrayList<>();
        expectedPlayers.add(new QuizPlayer("p1", "Player1", 10, true));
        expectedPlayers.add(new QuizPlayer("p2", "Player2", 20, true));

        // when
        quizMessage.setPlayers(expectedPlayers);
        ArrayList<QuizPlayer> actualPlayers = quizMessage.getPlayers();

        // then
        assertThat(actualPlayers).isEqualTo(expectedPlayers);
    }

    @Test
    void should_handle_null_values_for_all_fields() {
        // given
        QuizMessage quizMessage = new QuizMessage();

        // when
        quizMessage.setType(null);
        quizMessage.setContent(null);
        quizMessage.setPlayer(null);
        quizMessage.setQuestion(null);
        quizMessage.setSelectedAnswer(null);
        quizMessage.setCorrect(null);
        quizMessage.setWinner(null);
        quizMessage.setPlayers(null);

        // then
        assertThat(quizMessage.getType()).isNull();
        assertThat(quizMessage.getContent()).isNull();
        assertThat(quizMessage.getPlayer()).isNull();
        assertThat(quizMessage.getQuestion()).isNull();
        assertThat(quizMessage.getSelectedAnswer()).isNull();
        assertThat(quizMessage.getCorrect()).isNull();
        assertThat(quizMessage.getWinner()).isNull();
        assertThat(quizMessage.getPlayers()).isNull();
    }
}
