package ru.vk.education.job.domain;

import java.util.Set;

public class Job {

    private final String title;
    private final String company;
    private final Set<String> tags;
    private final Integer requiredExperience;

    public Job(String title, String company, Set<String> tags, Integer requiredExperience) {
        this.title = title;
        this.company = company;
        this.tags = tags;
        this.requiredExperience = requiredExperience;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Integer getRequiredExperience() {
        return requiredExperience;
    }

    @Override
    public String toString() {
        return title + " at " + company;
    }
}
