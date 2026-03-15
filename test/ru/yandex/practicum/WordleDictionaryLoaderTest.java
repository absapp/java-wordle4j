package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    @TempDir
    Path tempDir;

    private Path dictionaryPath;
    private WordleLogger testLogger;

    @BeforeEach
    void setUp() throws IOException {
        dictionaryPath = tempDir.resolve("test-words.txt");
        testLogger = new WordleLogger(tempDir.resolve("loader-test.log"));
    }

    @Test
    @DisplayName("Загрузка слов из файла")
    void testLoadFromFile() throws IOException {
        // Создаём тестовый файл
        List<String> testWords = List.of("кошка", "мышка", "книга", "ручка");
        Files.write(dictionaryPath, testWords);

        // Загружаем словарь
        WordleDictionaryLoader loader = new WordleDictionaryLoader(dictionaryPath, testLogger);
        WordleDictionary dictionary = loader.loadFiveLetterWords();

        // Проверяем, что слова загрузились
        assertEquals(4, dictionary.getWords().size());
        assertTrue(dictionary.contains("кошка"));
    }

    @Test
    @DisplayName("Фильтрация только 5-буквенных слов")
    void testFilterFiveLetterWords() throws IOException {
        // Создаём файл со словами разной длины
        List<String> mixedWords = List.of(
                "кот",      // 3 буквы
                "кошка",    // 5 букв
                "собака",   // 6 букв
                "ручка"     // 5 букв
        );
        Files.write(dictionaryPath, mixedWords);

        // Загружаем словарь
        WordleDictionaryLoader loader = new WordleDictionaryLoader(dictionaryPath, testLogger);
        WordleDictionary dictionary = loader.loadFiveLetterWords();

        // Должны загрузиться только слова из 5 букв
        assertEquals(2, dictionary.getWords().size());
    }

    @Test
    @DisplayName("Обработка отсутствующего файла")
    void testFileNotFound() {
        Path missingPath = tempDir.resolve("missing.txt");
        WordleDictionaryLoader loader = new WordleDictionaryLoader(missingPath, testLogger);

        // Проверяем, что выбрасывается IOException
        try {
            loader.loadFiveLetterWords();
            fail("Должно быть выброшено IOException");
        } catch (IOException e) {
            // Ожидаемое исключение
            assertTrue(e.getMessage().contains("Файл не найден"));
        }
    }

    @Test
    @DisplayName("Обработка пустого файла")
    void testEmptyFile() throws IOException {
        // Создаём пустой файл
        Files.createFile(dictionaryPath);

        WordleDictionaryLoader loader = new WordleDictionaryLoader(dictionaryPath, testLogger);

        // Проверяем, что выбрасывается IllegalStateException
        try {
            loader.loadFiveLetterWords();
            fail("Должно быть выброшено IllegalStateException");
        } catch (IllegalStateException e) {
            // Ожидаемое исключение
            assertEquals("Словарь пуст!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Быстрая загрузка через quickLoad")
    void testQuickLoad() throws IOException {
        // Создаём тестовый файл
        List<String> testWords = List.of("кошка", "мышка");
        Files.write(dictionaryPath, testWords);

        // Используем quickLoad
        WordleDictionary dictionary = WordleDictionaryLoader.quickLoad(dictionaryPath);

        assertNotNull(dictionary);
        assertEquals(2, dictionary.getWords().size());
    }
}