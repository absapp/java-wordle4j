package ru.yandex.practicum;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;
    private int steps = 0;
    private final int maxSteps;
    private final WordleDictionary dictionary;
    private final Map<String, String> guessTry = new LinkedHashMap<>();
    private final WordleLogger logger;

    // конструктор для тестов
    WordleGame(String predefinedAnswer, int maxSteps, WordleDictionary dictionary, WordleLogger logger) {
        this.answer = predefinedAnswer;
        this.maxSteps = maxSteps;
        this.dictionary = dictionary;
        this.logger = logger;
    }

    public WordleGame(int maxSteps, Path dictionaryPath, WordleLogger logger) throws IOException {
        validateInput(maxSteps, dictionaryPath);

        this.logger = logger;
        this.maxSteps = maxSteps;
        dictionary = WordleDictionaryLoader.quickLoad(dictionaryPath);
        answer = dictionary.getRandomWord();
    }

    private void validateInput(int maxSteps, Path dictionaryPath) {
        if (maxSteps <= 0) {
            throw new IllegalArgumentException("maxAttempts должен быть положительным");
        }
        if (dictionaryPath == null) {
            throw new NullPointerException("dictionaryPath не может быть null");
        }
    }

    private GameStatus makeGuess(String guess) {
        if (guess.equals(answer)) {
            return GameStatus.WIN;
        } else if (steps == maxSteps) {
            return GameStatus.LOSE;
        } else if (guess.isEmpty()) {
            steps++;
            return GameStatus.HELP;
        } else if (guess.length() < 5 || guess.length() > 5) {
            return GameStatus.WRONG_LENGTH;
        } else if (!dictionary.contains(guess)) {
            return GameStatus.NOT_IN_DICTIONARY;
        } else {
            steps++;
            return GameStatus.INCORRECT;
        }
    }

    public String playGame(String guess) {
        guess = normalizeWord(guess);
        GameStatus guessStatus = makeGuess(guess);

        String message = switch (guessStatus) {
            case LOSE -> String.format(
                    "Вы проиграли. Загаданное слово: '%s'",
                    answer
            );
            case WIN -> String.format(
                    "Победа! Вы отгадали слово '%s' за %d попыток!",
                    answer, steps
            );
            case HELP -> {
                String hintWord;
                if (guessTry.isEmpty()) {
                    hintWord = dictionary.getRandomWord();
                } else {
                    hintWord = dictionary.getHintWord(guessTry);
                }
                if (hintWord.equals(answer)) {
                    yield String.format("Победа! Вы отгадали слово '%s' за %d попыток!", answer, steps);
                }

                yield String.format("Подсказка: \n%s\n%s", hintWord, analyzeGuess(hintWord));
            }
            case WRONG_LENGTH -> throw new WrongNumberOfLetters(answer.length(), guess.length());
            case NOT_IN_DICTIONARY -> throw new WordNotFoundInDictionary(guess);
            case INCORRECT -> guess + "\n" + analyzeGuess(guess) + " Осталось " + (maxSteps - steps) + " попыток";
        };

        return message;
    }

    private String analyzeGuess(String guess) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < answer.length(); i++) {
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);

            if (guessChar == answerChar) {
                result.append("+");
            } else if (answer.indexOf(guessChar) >= 0) {
                result.append("^");
            } else {
                result.append("-");
            }
        }

        guessTry.put(guess, result.toString());
        return result.toString();
    }

    private String normalizeWord(String guess) {
        if (guess.isBlank()) {
            return "";
        }
        return guess.toLowerCase().replaceAll("ё", "е").trim();
    }

    public String getAnswer() {
        return answer;
    }
}
