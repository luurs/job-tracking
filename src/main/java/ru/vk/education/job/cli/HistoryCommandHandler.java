package ru.vk.education.job.cli;

import ru.vk.education.job.service.FileService;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.List;

public class HistoryCommandHandler implements CommandHandler {

    private final FileService fileService;

    public HistoryCommandHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public boolean supports(String line) {
        return line.equals("history");
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        List<String> history = fileService.getHistory();
        if (history.isEmpty()) {
            System.out.println("No commands in history.");
        } else {
            history.forEach(System.out::println);
        }
    }
}
