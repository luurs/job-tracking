package ru.vk.education.job.model;

import java.util.Set;
import java.util.stream.Collectors;

public class User {

     private final String name;
     private final Set<String> skills;
     private final Integer experience;

    public User(String name, Set<String> skills, Integer experience) {
        this.name = name;
        this.skills = skills;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public Integer getExperience() {
        return experience;
    }

    @Override
    public String toString() {
        String skillsAsString = skills.stream()
                .sorted()
                .collect(Collectors.joining(","));
        return name + " " + skillsAsString + " " + experience;
    }
}
