package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private WordleLogger testLogger;
    private List<String> testWords;

    @BeforeEach
    void setUp() throws IOException {
        testLogger = new WordleLogger(Path.of("build/test-logs/dictionary-test.log"));
        testWords = List.of("кошка", "мышка", "книга", "ручка", "парта");
        dictionary = new WordleDictionary(testWords, testLogger);
    }

    @Test
    @DisplayName("Проверка получения случайного слова из словаря")
    void testGetRandomWord() {
        String randomWord = dictionary.getRandomWord();

        assertNotNull(randomWord);

        boolean found = false;
        for (String word : testWords) {
            if (word.equals(randomWord)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Случайное слово должно быть из словаря");
    }

    @Test
    @DisplayName("Проверка наличия слова в словаре")
    void testContains() {
        // Существующее слово
        assertTrue(dictionary.contains("кошка"));

        // Несуществующее слово
        assertFalse(dictionary.contains("компьютер"));

        // Слово с пробелами (должно работать)
        assertTrue(dictionary.contains("  кошка  "));

        // Слово в верхнем регистре (должно работать)
        assertTrue(dictionary.contains("КОШКА"));

        // Слово с другим регистром
        assertTrue(dictionary.contains("КоШкА"));
    }

    @Test
    @DisplayName("Проверка получения неизменяемого списка слов")
    void testGetWords() {
        List<String> wordsCopy = dictionary.getWords();

        // Проверяем размер
        assertEquals(testWords.size(), wordsCopy.size());

        // Проверяем, что список действительно неизменяемый
        assertThrows(UnsupportedOperationException.class, () -> {
            wordsCopy.add("новое");
        });
    }

    @Test
    @DisplayName("Проверка генерации подсказки")
    void testGetHintWord() {
        // Создаём карту с одной попыткой
        Map<String, String> guessTry = new LinkedHashMap<>();
        guessTry.put("кошка", "+----");

        // Получаем подсказку
        String hintWord = dictionary.getHintWord(guessTry);

        // Проверяем, что подсказка не null и имеет правильную длину
        assertNotNull(hintWord);
        assertEquals(5, hintWord.length());
    }
}