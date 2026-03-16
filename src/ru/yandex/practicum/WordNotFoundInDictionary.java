package ru.yandex.practicum;

public class WordNotFoundInDictionary extends WordleGameException {

    private final String word;

    public WordNotFoundInDictionary(String word) {
        super(String.format("Слово '%s' не найдено", word));
        this.word = word;
    }

    @Override
    public String getUserMessage() {
        return String.format("Слово «%s» отсутствует в словаре", word);
    }
}

