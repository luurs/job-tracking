package ru.vk.education.job.repository;

import org.springframework.stereotype.Repository;
import ru.vk.education.job.domain.Job;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JobRepository {

    private final Map<String, Job> jobs = new ConcurrentHashMap<>();

    public void save(Job job) {
        jobs.putIfAbsent(job.getTitle(), job);
    }

    public Job findByTitle(String title) {
        return jobs.get(title);
    }

    public Collection<Job> findAll() {
        return jobs.values();
    }

    public void delete(String title) {
        jobs.remove(title);
    }
}
