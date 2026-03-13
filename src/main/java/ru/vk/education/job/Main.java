package ru.vk.education.job;

import ru.vk.education.job.cli.CommandHandler;
import ru.vk.education.job.cli.ExitCommandHandler;
import ru.vk.education.job.cli.JobCommandHandler;
import ru.vk.education.job.cli.JobListCommandHandler;
import ru.vk.education.job.cli.SuggestCommandHandler;
import ru.vk.education.job.cli.UserCommandHandler;
import ru.vk.education.job.cli.UserListCommandHandler;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage();
        SuggestService suggestService = new SuggestService();

        List<CommandHandler> handlers = List.of(
                new UserCommandHandler(),
                new UserListCommandHandler(),
                new JobCommandHandler(),
                new JobListCommandHandler(),
                new SuggestCommandHandler(),
                new ExitCommandHandler()
        );

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            for (CommandHandler handler : handlers) {
                if (handler.supports(line)) {
                    handler.handle(line, storage, suggestService);
                    break;
                }
            }
        }
    }
}