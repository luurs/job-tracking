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
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;
import ru.vk.education.job.service.BestJobFinder;
import ru.vk.education.job.service.FileService;
import ru.vk.education.job.service.JobService;
import ru.vk.education.job.service.StatisticService;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        JobRepository jobRepository = new JobRepository();

        UserService userService = new UserService(userRepository);
        JobService jobService = new JobService(jobRepository);
        SuggestService suggestService = new SuggestService(userRepository, jobRepository);
        StatisticService statisticService = new StatisticService(userRepository, jobRepository);
        FileService fileService = new FileService("commands.txt");

        List<CommandHandler> handlers = List.of(
                new UserCommandHandler(userService),
                new UserListCommandHandler(userService),
                new JobCommandHandler(jobService),
                new JobListCommandHandler(jobService),
                new SuggestCommandHandler(suggestService),
                new StatisticCommandHandler(statisticService),
                new HistoryCommandHandler(fileService),
                new ExitCommandHandler(null)
        );

        for (String savedCommand : fileService.getHistory()) {
            if (savedCommand.startsWith("user ") || savedCommand.startsWith("job ")) {
                for (CommandHandler handler : handlers) {
                    if (handler.supports(savedCommand)) {
                        handler.handle(savedCommand);
                        break;
                    }
                }
            }
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        BestJobFinder bestJobFinder = new BestJobFinder(userRepository, suggestService);
        scheduler.scheduleAtFixedRate(bestJobFinder, 1, 1, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                    if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println("Scheduler did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            for (CommandHandler handler : handlers) {
                if (handler.supports(line)) {
                    handler.handle(line);
                    fileService.saveCommand(line);
                    break;
                }
            }
        }
    }
}
