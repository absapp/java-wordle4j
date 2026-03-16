package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleLogger testLogger;
    private WordleDictionary realDictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() throws IOException {
        testLogger = new WordleLogger(Path.of("build/test-logs/game-test.log"));
        WordleDictionaryLoader loader = new WordleDictionaryLoader(Path.of("words_ru.txt"), testLogger);
        realDictionary = loader.loadFiveLetterWords();
        game = new WordleGame(6, Path.of("words_ru.txt"), testLogger);
    }

    @Test
    @DisplayName("Проверка правильного угадывания слова")
    void testCorrectGuess() {
        String actualAnswer = game.getAnswer();
        String result = game.playGame(actualAnswer);

        assertTrue(result.contains("Победа"));
        assertTrue(result.contains(actualAnswer));
    }

    @Test
    @DisplayName("Проверка неправильного слова")
    void testWrongGuess() {
        String actualAnswer = game.getAnswer();

        // Берём другое слово из словаря (не ответ)
        String wrongWord = "мышка";

        if (wrongWord.equals(actualAnswer)) {
            wrongWord = "книга";
        }

        String result = game.playGame(wrongWord);

        // Не должно быть победы
        assertFalse(result.contains("Победа"));
        // Должен быть анализ
        assertTrue(result.contains("+") || result.contains("^") || result.contains("-"));
    }

    @Test
    @DisplayName("Проверка подсказки")
    void testHelp() {
        String result = game.playGame("");

        assertTrue(result.contains("Подсказка"));
    }

    @Test
    @DisplayName("Проверка слова не из словаря")
    void testWordNotInDictionary() {
        try {
            game.playGame("абвгд");
            fail("Ожидалось исключение WordNotFoundInDictionary");
        } catch (WordNotFoundInDictionary e) {
            // Тест пройден - исключение получено
            assertEquals("Слово «абвгд» отсутствует в словаре", e.getUserMessage());
        }
    }

    @Test
    @DisplayName("Поражение после всех попыток")
    void testLose() throws IOException {
        // Создаём игру с 1 попыткой
        WordleGame gameWithOneAttempt = new WordleGame(1, Path.of("words_ru.txt"), testLogger);
        String correctAnswer = gameWithOneAttempt.getAnswer();

        // Первая попытка - неправильное слово
        String wrongWord = "мышка";
        if (wrongWord.equals(correctAnswer)) {
            wrongWord = "книга";
        }

        gameWithOneAttempt.playGame(wrongWord);

        // Вторая попытка - должно быть поражение
        String result = gameWithOneAttempt.playGame(wrongWord);

        assertTrue(result.contains("проиграли"));
        assertTrue(result.contains(correctAnswer));
    }

    @Test
    @DisplayName("Слишком короткое слово")
    void testTooShortWord() {
        try {
            game.playGame("кот");
            fail("Должно быть выброшено WrongNumberOfLetters");
        } catch (WrongNumberOfLetters e) {
            assertEquals("⚠️ Слово должно быть из 5 букв", e.getUserMessage());
        }
    }

    @Test
    @DisplayName("Слишком длинное слово")
    void testTooLongWord() {
        try {
            game.playGame("компьютер");
            fail("Должно быть выброшено WrongNumberOfLetters");
        } catch (WrongNumberOfLetters e) {
            assertEquals("⚠️ Слово должно быть из 5 букв", e.getUserMessage());
        }
    }

    @Test
    @DisplayName("Нормализация слова (приведение к нижнему регистру)")
    void testNormalizeLowerCase() {
        String correctAnswer = game.getAnswer();

        // Вводим слово в верхнем регистре
        String result = game.playGame(correctAnswer.toUpperCase());

        assertTrue(result.contains("Победа"));
    }

    @Test
    @DisplayName("Нормализация слова (замена ё на е)")
    void testNormalizeYo() throws IOException {
        String guess = "котёл";
        String result = game.playGame(guess);

        assertTrue(result.contains("котел"));
    }
}
