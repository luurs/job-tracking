package ru.vk.education.job.service;

import ru.vk.education.job.model.Job;
import ru.vk.education.job.model.User;
import ru.vk.education.job.storage.Storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuggestService {

    public List<Job> suggest(Storage storage, String userName, int limit) {
        User user = storage.findUser(userName);
        if (user == null) {
            return List.of();
        }

        List<JobMatch> jobMatches = new ArrayList<>();
        for (Job job : storage.getAllJobs()) {
            double score = calculateMatchScore(user, job);
            if (score > 0) {
                jobMatches.add(new JobMatch(job, score));
            }
        }

        jobMatches.sort((first, second) -> Double.compare(second.score, first.score));

        List<Job> result = new ArrayList<>();
        for (int index = 0; index < jobMatches.size() && index < limit; index++) {
            result.add(jobMatches.get(index).job);
        }
        return result;
    }

    private double calculateMatchScore(User user, Job job) {
        Set<String> userSkills = new HashSet<>(user.getSkills());
        int overlap = 0;
        for (String tag : job.getTags()) {
            if (userSkills.contains(tag)) {
                overlap++;
            }
        }

        if (overlap == 0) {
            return 0;
        }

        if (user.getExperience() < job.getRequiredExperience()) {
            return overlap / 2.0;
        }

        return overlap;
    }

    private static class JobMatch {
        private final Job job;
        private final double score;

        private JobMatch(Job job, double score) {
            this.job = job;
            this.score = score;
        }
    }
}
