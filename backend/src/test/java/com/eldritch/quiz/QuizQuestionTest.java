package com.eldritch.quiz;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class QuizQuestionTest {

    @Test
    void should_return_correct_text_when_getText_is_called() {
        // given
        String expectedText = "What is the capital of France?";
        List<String> options = Arrays.asList("London", "Paris", "Berlin", "Madrid");
        QuizQuestion quizQuestion = new QuizQuestion(expectedText, options, 1);

        // when
        String actualText = quizQuestion.getText();

        // then
        assertThat(actualText).isEqualTo(expectedText);
    }

    @Test
    void should_set_new_text_when_setText_is_called() {
        // given
        String initialText = "Initial question";
        String newText = "Updated question";
        List<String> options = Arrays.asList("Option 1", "Option 2");
        QuizQuestion quizQuestion = new QuizQuestion(initialText, options, 0);

        // when
        quizQuestion.setText(newText);
        String actualText = quizQuestion.getText();

        // then
        assertThat(actualText).isEqualTo(newText);
    }

    @Test
    void should_return_correct_options_when_getOptions_is_called() {
        // given
        String text = "Sample question";
        List<String> expectedOptions = Arrays.asList("A", "B", "C", "D");
        QuizQuestion quizQuestion = new QuizQuestion(text, expectedOptions, 2);

        // when
        List<String> actualOptions = quizQuestion.getOptions();

        // then
        assertThat(actualOptions).isEqualTo(expectedOptions);
    }

    @Test
    void should_set_new_options_when_setOptions_is_called() {
        // given
        String text = "Sample question";
        List<String> initialOptions = Arrays.asList("X", "Y");
        List<String> newOptions = Arrays.asList("P", "Q", "R");
        QuizQuestion quizQuestion = new QuizQuestion(text, initialOptions, 0);

        // when
        quizQuestion.setOptions(newOptions);
        List<String> actualOptions = quizQuestion.getOptions();

        // then
        assertThat(actualOptions).isEqualTo(newOptions);
    }

    @Test
    void should_return_correct_option_index_when_getCorrectOption_is_called() {
        // given
        String text = "Multiple choice question";
        List<String> options = Arrays.asList("Choice 1", "Choice 2", "Choice 3");
        int expectedCorrectOption = 2;
        QuizQuestion quizQuestion = new QuizQuestion(text, options, expectedCorrectOption);

        // when
        int actualCorrectOption = quizQuestion.getCorrectOption();

        // then
        assertThat(actualCorrectOption).isEqualTo(expectedCorrectOption);
    }

    @Test
    void should_set_new_correct_option_when_setCorrectOption_is_called() {
        // given
        String text = "True or False";
        List<String> options = Arrays.asList("True", "False");
        int initialCorrectOption = 0;
        int newCorrectOption = 1;
        QuizQuestion quizQuestion = new QuizQuestion(text, options, initialCorrectOption);

        // when
        quizQuestion.setCorrectOption(newCorrectOption);
        int actualCorrectOption = quizQuestion.getCorrectOption();

        // then
        assertThat(actualCorrectOption).isEqualTo(newCorrectOption);
    }

    @Test
    void should_initialize_correctly_when_constructor_is_called() {
        // given
        String expectedText = "Constructor test";
        List<String> expectedOptions = Arrays.asList("Opt1", "Opt2", "Opt3");
        int expectedCorrectOption = 1;

        // when
        QuizQuestion quizQuestion = new QuizQuestion(expectedText, expectedOptions, expectedCorrectOption);

        // then
        assertThat(quizQuestion.getText()).isEqualTo(expectedText);
        assertThat(quizQuestion.getOptions()).isEqualTo(expectedOptions);
        assertThat(quizQuestion.getCorrectOption()).isEqualTo(expectedCorrectOption);
    }
}
