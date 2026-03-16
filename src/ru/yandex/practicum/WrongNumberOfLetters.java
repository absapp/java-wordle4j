package ru.yandex.practicum;

public class WrongNumberOfLetters extends WordleGameException {

    private final int expected;
    private final int actual;

    public WrongNumberOfLetters(int expected, int actual) {
        super(String.format("Ожидалось %d букв, получено %d", expected, actual));
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String getUserMessage() {
        return String.format("⚠️ Слово должно быть из %d букв", expected);
    }
}
