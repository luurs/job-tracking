package ru.vk.education.job.cli;

import org.springframework.stereotype.Component;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.JobService;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JobCommandHandler implements CommandHandler {

    private static final Pattern MAIN_PATTERN = Pattern.compile("^job\\s+(\\S+)(.*)$");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("--(company|tags|exp)=([^\\s]+)");

    private final JobService jobService;

    public JobCommandHandler(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public boolean supports(String line) {
        Matcher matcher = MAIN_PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line) {
        Matcher mainMatcher = MAIN_PATTERN.matcher(line);
        if (!mainMatcher.matches()) {
            return;
        }

        String jobTitle = mainMatcher.group(1);
        String argumentsPart = mainMatcher.group(2);

        Matcher argumentMatcher = ARGUMENT_PATTERN.matcher(argumentsPart);

        String company = null;
        Set<String> tags = new LinkedHashSet<>();
        Integer requiredExperience = null;

        while (argumentMatcher.find()) {
            String key = argumentMatcher.group(1);
            String value = argumentMatcher.group(2);

            if ("company".equals(key)) {
                company = value;
            } else if ("tags".equals(key)) {
                String[] rawTags = value.split(",");
                for (String rawTag : rawTags) {
                    if (!rawTag.isEmpty()) {
                        tags.add(rawTag);
                    }
                }
            } else if ("exp".equals(key)) {
                requiredExperience = Integer.parseInt(value);
            }
        }

        if (company == null || requiredExperience == null) {
            return;
        }

        Job job = new Job(jobTitle, company, tags, requiredExperience);
        jobService.create(job);
    }
}
