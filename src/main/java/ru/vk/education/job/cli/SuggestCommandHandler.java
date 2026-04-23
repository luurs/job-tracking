package ru.vk.education.job.cli;

import org.springframework.stereotype.Component;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.SuggestService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SuggestCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^suggest\\s+(\\S+)\\s*$");

    private final SuggestService suggestService;

    public SuggestCommandHandler(SuggestService suggestService) {
        this.suggestService = suggestService;
    }

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            return;
        }

        String userName = matcher.group(1);

        List<Job> suggestedJobs = suggestService.suggest(userName, 2);
        for (Job job : suggestedJobs) {
            System.out.println(job.toString());
        }
    }
}
