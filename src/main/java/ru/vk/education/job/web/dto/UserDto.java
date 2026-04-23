package ru.vk.education.job.web.dto;

import ru.vk.education.job.domain.User;

import java.util.LinkedHashSet;
import java.util.Set;

public record UserDto(String name, Set<String> skills, Integer experience) {

    public User toDomain() {
        return new User(name, skills == null ? new LinkedHashSet<>() : skills, experience);
    }

    public static UserDto fromDomain(User user) {
        return new UserDto(user.getName(), user.getSkills(), user.getExperience());
    }
}
