package ru.vk.education.job.storage;

import ru.vk.education.job.model.Job;
import ru.vk.education.job.model.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Storage {

    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Job> jobs = new LinkedHashMap<>();

    public void addUser(User user) {
        users.putIfAbsent(user.getName(), user);
    }

    public void addJob(Job job) {
        jobs.putIfAbsent(job.getTitle(), job);
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public Collection<Job> getJobs() {
        return jobs.values();
    }

    public User findUser(String name) {
        return users.get(name);
    }

    public Collection<Job> getAllJobs() {
        return jobs.values();
    }
}
