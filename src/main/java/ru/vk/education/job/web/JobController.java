package ru.vk.education.job.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.service.JobService;
import ru.vk.education.job.web.dto.JobDto;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public JobDto create(@RequestBody JobDto dto) {
        jobService.create(dto.toDomain());
        return dto;
    }

    @GetMapping
    public List<JobDto> list() {
        return jobService.list().stream()
                .map(JobDto::fromDomain)
                .toList();
    }
}
