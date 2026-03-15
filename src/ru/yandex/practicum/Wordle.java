package ru.yandex.practicum;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordleLogger logger = null;

        try {
            logger = new WordleLogger(Path.of("wordle.log"));
            logger.log("=== Новая игра запущена ===");

            WordleGame wordleGame = new WordleGame(6, Path.of("words_ru.txt"), logger);
            logger.log("Игра создана успешно");

            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("Попробуйте угадать слово из 5 букв");
            System.out.println("Пустая строка для подсказки");

            while (true) {
                System.out.print("\nВведите слово: ");
                String guess = scanner.nextLine();

                try {
                    String result = wordleGame.playGame(guess);
                    System.out.println(result);

                    if (result.contains("Победа") || result.contains("проиграли")) {
                        break;
                    }
                }catch (WordleGameException e) {
                    System.out.println(e.getUserMessage());
                    logger.log("Игровое исключение: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            logger.log("ОШИБКА: " + e.getMessage());
        } catch (IllegalArgumentException | NullPointerException | IllegalStateException e) {
            logger.log("ОШИБКА конфигурации: " + e.getMessage());
        }
    }
}
