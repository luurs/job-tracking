package ru.vk.education.job.cli;

import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExitCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^exit\\s*$");

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        System.exit(0);
    }
}

