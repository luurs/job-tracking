package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public StatisticService(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public List<User> matchingUsers(int n) {
        return userRepository.findAll().stream()
                .filter(user -> countMatches(user) >= n)
                .sorted(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<String> topSkills(int n) {
        return userRepository.findAll().stream()
                .flatMap(user -> user.getSkills().stream())
                .collect(Collectors.groupingBy(skill -> skill, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey, String.CASE_INSENSITIVE_ORDER))
                .limit(n)
                .map(Map.Entry::getKey)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public List<Job> jobsByMinExperience(int n) {
        return jobRepository.findAll().stream()
                .filter(job -> job.getRequiredExperience() >= n)
                .sorted(Comparator.comparing(Job::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    private long countMatches(User user) {
        return jobRepository.findAll().stream()
                .filter(job -> hasMatch(user, job))
                .count();
    }

    private boolean hasMatch(User user, Job job) {
        return user.getSkills().stream().anyMatch(job.getTags()::contains);
    }
}
