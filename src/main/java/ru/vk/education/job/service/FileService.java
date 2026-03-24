package ru.vk.education.job.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

public class FileService {

    private final Path filePath;

    public FileService(String fileName) {
        this.filePath = Path.of(fileName);
    }

    public void saveCommand(String command) {
        try {
            Files.writeString(filePath, command + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to save command to file: " + e.getMessage());
        }
    }

    public List<String> getHistory() {
        if (!Files.exists(filePath)) {
            return Collections.emptyList();
        }
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            System.err.println("Failed to read history from file: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}