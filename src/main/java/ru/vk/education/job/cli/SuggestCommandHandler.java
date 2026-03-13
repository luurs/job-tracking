package ru.vk.education.job.cli;

import ru.vk.education.job.model.Job;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuggestCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^suggest\\s+(\\S+)\\s*$");

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line, Storage storage, SuggestService suggestService) {
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            return;
        }

        String userName = matcher.group(1);

        List<Job> suggestedJobs = suggestService.suggest(storage, userName, 2);
        for (Job job : suggestedJobs) {
            System.out.println(job.toString());
        }
    }
}

