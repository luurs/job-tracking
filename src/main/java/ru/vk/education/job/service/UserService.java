package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.UserRepository;

import java.util.Collection;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void create(User user) {
        repository.save(user);
    }

    public Collection<User> list() {
        return repository.findAll();
    }
}
