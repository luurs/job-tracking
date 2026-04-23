package ru.vk.education.job.web.dto;

import ru.vk.education.job.domain.Job;

import java.util.LinkedHashSet;
import java.util.Set;

public record JobDto(String title, String company, Set<String> tags, Integer requiredExperience) {

    public Job toDomain() {
        return new Job(title, company, tags == null ? new LinkedHashSet<>() : tags, requiredExperience);
    }

    public static JobDto fromDomain(Job job) {
        return new JobDto(job.getTitle(), job.getCompany(), job.getTags(), job.getRequiredExperience());
    }
}
