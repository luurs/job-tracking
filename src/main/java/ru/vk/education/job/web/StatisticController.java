package ru.vk.education.job.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.service.StatisticService;
import ru.vk.education.job.web.dto.JobDto;
import ru.vk.education.job.web.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/match")
    public List<UserDto> matchingUsers(@RequestParam int n) {
        return statisticService.matchingUsers(n).stream()
                .map(UserDto::fromDomain)
                .toList();
    }

    @GetMapping("/top-skills")
    public List<String> topSkills(@RequestParam int n) {
        return statisticService.topSkills(n);
    }

    @GetMapping("/exp")
    public List<JobDto> jobsByMinExperience(@RequestParam int n) {
        return statisticService.jobsByMinExperience(n).stream()
                .map(JobDto::fromDomain)
                .toList();
    }
}
