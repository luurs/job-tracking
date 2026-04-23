package ru.vk.education.job.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.vk.education.job.service.FileService;

import java.util.List;
import java.util.Scanner;

@Component
public class CliRunner implements CommandLineRunner {

    private final List<CommandHandler> handlers;
    private final FileService fileService;

    public CliRunner(List<CommandHandler> handlers, FileService fileService) {
        this.handlers = handlers;
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) {
        for (String savedCommand : fileService.getHistory()) {
            if (savedCommand.startsWith("user ") || savedCommand.startsWith("job ")) {
                dispatch(savedCommand, false);
            }
        }

        Thread repl = new Thread(this::repLoop, "cli-repl");
        repl.setDaemon(true);
        repl.start();
    }

    // Read–Eval–Print Loop
    private void repLoop() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            dispatch(line, true);
        }
    }

    private void dispatch(String line, boolean saveToHistory) {
        for (CommandHandler handler : handlers) {
            if (handler.supports(line)) {
                handler.handle(line);
                if (saveToHistory) {
                    fileService.saveCommand(line);
                }
                break;
            }
        }
    }
}
