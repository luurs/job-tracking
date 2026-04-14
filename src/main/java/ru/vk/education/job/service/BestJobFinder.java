package ru.vk.education.job.service;

import ru.vk.education.job.model.Job;
import ru.vk.education.job.model.User;
import ru.vk.education.job.storage.Storage;

import java.util.List;

public class BestJobFinder implements Runnable {

    private final Storage storage;
    private final SuggestService suggestService;

    public BestJobFinder(Storage storage, SuggestService suggestService) {
        this.storage = storage;
        this.suggestService = suggestService;
    }

    @Override
    public void run() {
        for (User user : storage.getUsers()) {
            List<Job> bestJobs = suggestService.suggest(storage, user.getName(), 1);
            if (!bestJobs.isEmpty()) {
                System.out.println(user.getName() + ", лучшее предложение — " + bestJobs.get(0));
            }
        }
    }
}
