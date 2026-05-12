package ru.vk.education.job.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.education.job.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepository {

    private static final String SELECT_BASE =
            "SELECT u.name, u.experience, s.skill " +
            "FROM users u LEFT JOIN user_skills s ON s.user_name = u.name";

    private final NamedParameterJdbcTemplate jdbc;

    public UserRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public void save(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("experience", user.getExperience());
        int inserted = jdbc.update(
                "INSERT INTO users(name, experience) VALUES (:name, :experience) " +
                "ON CONFLICT (name) DO NOTHING",
                params);
        if (inserted == 0) {
            return;
        }
        Set<String> skills = user.getSkills();
        if (skills == null || skills.isEmpty()) {
            return;
        }
        SqlParameterSource[] batch = skills.stream()
                .map(skill -> new MapSqlParameterSource()
                        .addValue("userName", user.getName())
                        .addValue("skill", skill))
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(
                "INSERT INTO user_skills(user_name, skill) VALUES (:userName, :skill) " +
                "ON CONFLICT DO NOTHING",
                batch);
    }

    public User findByName(String name) {
        List<User> result = jdbc.query(
                SELECT_BASE + " WHERE u.name = :name",
                new MapSqlParameterSource("name", name),
                USERS_EXTRACTOR);
        return result.isEmpty() ? null : result.get(0);
    }

    public Collection<User> findAll() {
        return jdbc.query(SELECT_BASE + " ORDER BY u.name", USERS_EXTRACTOR);
    }

    @Transactional
    public void delete(String name) {
        jdbc.update(
                "DELETE FROM users WHERE name = :name",
                new MapSqlParameterSource("name", name));
    }

    private static final ResultSetExtractor<List<User>> USERS_EXTRACTOR = (ResultSet rs) -> {
        record Acc(int experience, Set<String> skills) {}
        LinkedHashMap<String, Acc> byName = new LinkedHashMap<>();
        while (rs.next()) {
            String name = rs.getString("name");
            int experience = rs.getInt("experience");
            Acc acc = byName.computeIfAbsent(name, n -> new Acc(experience, new HashSet<>()));
            String skill = rs.getString("skill");
            if (skill != null) {
                acc.skills().add(skill);
            }
        }
        List<User> users = new ArrayList<>(byName.size());
        byName.forEach((name, acc) -> users.add(new User(name, acc.skills(), acc.experience())));
        return users;
    };
}
