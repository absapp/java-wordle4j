package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class WordleLogger {
    private final Path logPath;

    public WordleLogger(Path logPath) throws IOException {
        this.logPath = logPath;
        if (logPath.getParent() != null) {
            Files.createDirectories(logPath.getParent());
        }
    }

    public void log(String message) {
        try (FileWriter writer = new FileWriter(this.logPath.toFile(), StandardCharsets.UTF_8, true)) {
            writer.write(LocalDateTime.now() + ": " + message + "\n");
        } catch (IOException e) {
            System.err.println("Не удалось записать в лог: " + e.getMessage());
        }
    }
}
