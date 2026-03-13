package ru.vk.education.job.cli;

import ru.vk.education.job.model.User;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCommandHandler implements CommandHandler {

    private static final Pattern MAIN_PATTERN = Pattern.compile("^user\\s+(\\S+)(.*)$");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("--(skills|exp)=([^\\s]+)");

    @Override
    public boolean supports(String line) {
        Matcher matcher = MAIN_PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        Matcher mainMatcher = MAIN_PATTERN.matcher(line);
        if (!mainMatcher.matches()) {
            return;
        }

        String userName = mainMatcher.group(1);
        String argumentsPart = mainMatcher.group(2);

        Matcher argumentMatcher = ARGUMENT_PATTERN.matcher(argumentsPart);

        Set<String> skills = new LinkedHashSet<>();
        Integer experience = null;

        while (argumentMatcher.find()) {
            String key = argumentMatcher.group(1);
            String value = argumentMatcher.group(2);

            if ("skills".equals(key)) {
                String[] rawSkills = value.split(",");
                for (String rawSkill : rawSkills) {
                    if (!rawSkill.isEmpty()) {
                        skills.add(rawSkill);
                    }
                }
            } else if ("exp".equals(key)) {
                experience = Integer.parseInt(value);
            }
        }

        if (experience == null) {
            return;
        }

        User user = new User(userName, skills, experience);
        storage.addUser(user);
    }
}

