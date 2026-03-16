package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public static final int WORD_LENGTH = 5;
    private final Path dictionaryPath;
    private final WordleLogger logger;  // делаем final

    public WordleDictionaryLoader(Path dictionaryPath) {
        this(dictionaryPath, null);  // вызываем основной конструктор
    }

    public WordleDictionaryLoader(Path dictionaryPath, WordleLogger logger) {
        this.dictionaryPath = dictionaryPath;
        this.logger = logger;
    }

    public WordleDictionary loadFiveLetterWords() throws IOException {
        if (!Files.exists(dictionaryPath)) {
            throw new IOException("Файл не найден: " + dictionaryPath);
        }
        List<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase().replaceAll("ё", "е").trim();
                if (line.length() == WORD_LENGTH && !line.isBlank()) {
                    dictionary.add(line);
                }
            }
        }
        if (dictionary.isEmpty()) {
            throw new IllegalStateException("Словарь пуст!");
        }
        return new WordleDictionary(dictionary, logger);
    }

    public static WordleDictionary quickLoad(Path path) throws IOException {
        return new WordleDictionaryLoader(path).loadFiveLetterWords();
    }
}
