package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuizEventTest {

    @Test
    void testzzzz() {
        Object source = new Object();
        QuizMessage expectedMessage = new QuizMessage();
        expectedMessage.setType(QuizMessage.MessageType.QUESTION);
        expectedMessage.setContent("Test content");

        // when
        QuizEvent quizEvent = new QuizEvent(source, expectedMessage);
        QuizMessage actualMessage = quizEvent.getMessage();

        // then
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualMessage.getType()).isEqualTo(QuizMessage.MessageType.QUESTION);
        assertThat(actualMessage.getContent()).isEqualTo("Test content");
    }

    @Test
    void should_return_correct_source_when_created() {
        // given
        Object expectedSource = new Object();
        QuizMessage message = new QuizMessage();

        // when
        QuizEvent quizEvent = new QuizEvent(expectedSource, message);
        Object actualSource = quizEvent.getSource();

        // then
        assertThat(actualSource).isEqualTo(expectedSource);
    }

    @Test
    void should_contain_complete_message_data_when_created_with_full_message() {
        // given
        Object source = new Object();
        QuizPlayer player = new QuizPlayer("player1", "Test Player", 10, true);
        QuizQuestion question = new QuizQuestion("Question text",
            List.of("Option 1", "Option 2"), 0);
        ArrayList<QuizPlayer> players = new ArrayList<>(List.of(player));

        QuizMessage expectedMessage = new QuizMessage();
        expectedMessage.setType(QuizMessage.MessageType.ANSWER);
        expectedMessage.setPlayer(player);
        expectedMessage.setQuestion(question);
        expectedMessage.setSelectedAnswer(1);
        expectedMessage.setCorrect(false);
        expectedMessage.setPlayers(players);

        // when
        QuizEvent quizEvent = new QuizEvent(source, expectedMessage);
        QuizMessage actualMessage = quizEvent.getMessage();

        // then
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualMessage.getType()).isEqualTo(QuizMessage.MessageType.ANSWER);
        assertThat(actualMessage.getPlayer()).isEqualTo(player);
        assertThat(actualMessage.getQuestion()).isEqualTo(question);
        assertThat(actualMessage.getSelectedAnswer()).isEqualTo(1);
        assertThat(actualMessage.getCorrect()).isFalse();
        assertThat(actualMessage.getPlayers()).hasSize(1).contains(player);
    }
}
