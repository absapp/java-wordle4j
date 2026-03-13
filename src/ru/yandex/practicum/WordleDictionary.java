package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;
    private final Random random = new Random();
    private final List<String> possibleWords;
    private final WordleLogger logger;

    public WordleDictionary(List<String> words, WordleLogger logger) {
        this.words = List.copyOf(words);
        this.logger = logger;
        possibleWords = new ArrayList<>(words);

        if (logger != null) {
            logger.log("Словарь загружен. Всего слов: " + words.size());
        }
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
        return List.copyOf(words);
    }

    private List<String> hintList(Map<String, String> guessTry) {

        for (Map.Entry<String, String> entry : guessTry.entrySet()) {
            String targetWord = entry.getKey();
            String analyze = entry.getValue();

            for (int i = 0; i < analyze.length(); i++) {
                char hintChar = analyze.charAt(i);
                char targetChar = targetWord.charAt(i);

                if (hintChar == '+') {
                    int position = i;
                    possibleWords.removeIf(word -> word.charAt(position) != targetChar);
                } else if (hintChar == '^') {
                    int position = i;
                    possibleWords.removeIf(word -> word.charAt(position) == targetChar ||
                            word.indexOf(targetChar) == -1);
                } else if (hintChar == '-') {
                    possibleWords.removeIf(word -> word.indexOf(targetChar) >= 0);
                }
            }
        }
        return possibleWords;
    }

    public String getHintWord(Map<String, String> guessTry) {
        List<String> hintList = hintList(guessTry);
        return hintList.get(random.nextInt(hintList.size()));
    }
}

