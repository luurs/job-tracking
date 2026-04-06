package ru.vk.education.job.cli;

import ru.vk.education.job.model.Job;
import ru.vk.education.job.model.User;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.Comparator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatisticCommandHandler implements CommandHandler {

    private static final Pattern MAIN_PATTERN = Pattern.compile("^stat\\s+(\\S+)(.*)$");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("--(\\w+[\\w-]*)\\s+(\\d+)");

    @Override
    public boolean supports(String line) {
        return MAIN_PATTERN.matcher(line.trim()).matches();
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        Matcher matcher = ARGUMENT_PATTERN.matcher(line);
        if (!matcher.find()) {
            return;
        }

        String flag = matcher.group(1);
        int value = Integer.parseInt(matcher.group(2));

        switch (flag) {
            case "match" -> handleMatch(storage, value);
            case "top-skills" -> handleTopSkills(storage, value);
            case "exp" -> handleExp(storage, value);
            default -> { }
        }
    }

    private void handleMatch(Storage storage, int n) {
        storage.getUsers().stream()
                .filter(user -> countMatches(user, storage) >= n)
                .sorted(Comparator.comparing(User::getName))
                .forEach(System.out::println);
    }

    private long countMatches(User user, Storage storage) {
        return storage.getJobs().stream()
                .filter(job -> hasMatch(user, job))
                .count();
    }

    private boolean hasMatch(User user, Job job) {
        return user.getSkills().stream().anyMatch(job.getTags()::contains);
    }

    private void handleTopSkills(Storage storage, int n) {
        storage.getUsers().stream()
                .flatMap(user -> user.getSkills().stream())
                .collect(Collectors.groupingBy(skill -> skill, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .limit(n)
                .map(Map.Entry::getKey)
                .sorted()
                .forEach(System.out::println);
    }

    private void handleExp(Storage storage, int n) {
        storage.getJobs().stream()
                .filter(job -> job.getRequiredExperience() >= n)
                .sorted(Comparator.comparing(Job::getTitle))
                .forEach(System.out::println);
    }
}
