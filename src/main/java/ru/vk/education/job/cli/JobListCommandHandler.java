package ru.vk.education.job.cli;

import org.springframework.stereotype.Component;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.JobService;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JobListCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^job-list\\s*$");

    private final JobService jobService;

    public JobListCommandHandler(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line) {
        jobService.list().stream()
                .sorted(Comparator.comparing(Job::getTitle, String.CASE_INSENSITIVE_ORDER))
                .forEach(System.out::println);
    }
}
