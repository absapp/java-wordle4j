package ru.yandex.practicum;

import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;
    private final Random random = new Random();

    public WordleDictionary(List<String> words) {
        this.words = List.copyOf(words);
    }

    public String getRandomWord() throws IllegalArgumentException {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст!");
        }
        return words.get(random.nextInt(words.size()));
    }

    public boolean contains(String word) {
        return words.contains(word.toLowerCase().replaceAll("ё", "е").trim());
    }

    public List<String> getWords() {
        return words;
    }
}

