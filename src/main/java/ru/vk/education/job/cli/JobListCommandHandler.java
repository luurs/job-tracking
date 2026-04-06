package ru.vk.education.job.cli;

import ru.vk.education.job.model.Job;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobListCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^job-list\\s*$");

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        storage.getJobs().stream()
                .sorted(Comparator.comparing(Job::getTitle, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);
    }
}

