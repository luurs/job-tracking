package ru.vk.education.job.repository;

import org.springframework.stereotype.Repository;
import ru.vk.education.job.domain.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void save(User user) {
        users.putIfAbsent(user.getName(), user);
    }

    public User findByName(String name) {
        return users.get(name);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public void delete(String name) {
        users.remove(name);
    }
}
