package ru.yandex.practicum;

public abstract class WordleGameException extends RuntimeException {

    public WordleGameException(String message) {
        super(message);
    }

    public abstract String getUserMessage();

}






