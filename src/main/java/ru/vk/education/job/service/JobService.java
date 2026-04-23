package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.repository.JobRepository;

import java.util.Collection;

@Service
public class JobService {

    private final JobRepository repository;

    public JobService(JobRepository repository) {
        this.repository = repository;
    }

    public void create(Job job) {
        repository.save(job);
    }

    public Collection<Job> list() {
        return repository.findAll();
    }
}
