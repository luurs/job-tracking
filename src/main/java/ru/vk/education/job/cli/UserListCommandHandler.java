package ru.vk.education.job.cli;

import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserListCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^user-list\\s*$");

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        storage.getUsers().forEach(user -> System.out.println(user.toString()));
    }
}

