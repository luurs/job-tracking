package ru.vk.education.job.cli;

import org.springframework.stereotype.Component;
import ru.vk.education.job.service.StatisticService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StatisticCommandHandler implements CommandHandler {

    private static final Pattern MAIN_PATTERN = Pattern.compile("^stat\\s+(\\S+)(.*)$");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("--(\\w+[\\w-]*)\\s+(\\d+)");

    private final StatisticService statisticService;

    public StatisticCommandHandler(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Override
    public boolean supports(String line) {
        return MAIN_PATTERN.matcher(line.trim()).matches();
    }

    @Override
    public void handle(String line) {
        Matcher matcher = ARGUMENT_PATTERN.matcher(line);
        if (!matcher.find()) {
            return;
        }

        String flag = matcher.group(1);
        int value = Integer.parseInt(matcher.group(2));

        switch (flag) {
            case "match" -> statisticService.matchingUsers(value).forEach(System.out::println);
            case "top-skills" -> statisticService.topSkills(value).forEach(System.out::println);
            case "exp" -> statisticService.jobsByMinExperience(value).forEach(System.out::println);
            default -> { }
        }
    }
}
