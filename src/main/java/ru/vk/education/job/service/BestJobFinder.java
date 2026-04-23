package ru.vk.education.job.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.UserRepository;

import java.util.List;

@Component
public class BestJobFinder implements Runnable {

    private final UserRepository userRepository;
    private final SuggestService suggestService;

    public BestJobFinder(UserRepository userRepository, SuggestService suggestService) {
        this.userRepository = userRepository;
        this.suggestService = suggestService;
    }

    @Override
    @Scheduled(fixedRate = 60_000, initialDelay = 60_000)
    public void run() {
        for (User user : userRepository.findAll()) {
            List<Job> bestJobs = suggestService.suggest(user.getName(), 1);
            if (!bestJobs.isEmpty()) {
                System.out.println(user.getName() + ", лучшее предложение — " + bestJobs.get(0));
            }
        }
    }
}
