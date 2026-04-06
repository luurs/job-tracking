package ru.vk.education.job;

import ru.vk.education.job.cli.CommandHandler;
import ru.vk.education.job.cli.ExitCommandHandler;
import ru.vk.education.job.cli.HistoryCommandHandler;
import ru.vk.education.job.cli.JobCommandHandler;
import ru.vk.education.job.cli.JobListCommandHandler;
import ru.vk.education.job.cli.StatisticCommandHandler;
import ru.vk.education.job.cli.SuggestCommandHandler;
import ru.vk.education.job.cli.UserCommandHandler;
import ru.vk.education.job.cli.UserListCommandHandler;
import ru.vk.education.job.service.FileService;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage();
        SuggestService suggestService = new SuggestService();
        FileService fileService = new FileService("commands.txt");

        List<CommandHandler> handlers = List.of(
                new UserCommandHandler(),
                new UserListCommandHandler(),
                new JobCommandHandler(),
                new JobListCommandHandler(),
                new SuggestCommandHandler(),
                new StatisticCommandHandler(),
                new HistoryCommandHandler(fileService),
                new ExitCommandHandler()
        );

        for (String savedCommand : fileService.getHistory()) {
            if (savedCommand.startsWith("user ") || savedCommand.startsWith("job ")) {
                for (CommandHandler handler : handlers) {
                    if (handler.supports(savedCommand)) {
                        handler.handle(savedCommand, storage, suggestService);
                        break;
                    }
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            for (CommandHandler handler : handlers) {
                if (handler.supports(line)) {
                    handler.handle(line, storage, suggestService);
                    fileService.saveCommand(line);
                    break;
                }
            }
        }
    }
}