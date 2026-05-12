package ru.vk.education.job.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.education.job.domain.Job;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Repository
public class JobRepository {

    private static final String SELECT_BASE =
            "SELECT j.title, j.company, j.required_experience, t.tag " +
            "FROM jobs j LEFT JOIN job_tags t ON t.job_title = j.title";

    private final NamedParameterJdbcTemplate jdbc;

    public JobRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public void save(Job job) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", job.getTitle())
                .addValue("company", job.getCompany())
                .addValue("requiredExperience", job.getRequiredExperience());
        int inserted = jdbc.update(
                "INSERT INTO jobs(title, company, required_experience) " +
                "VALUES (:title, :company, :requiredExperience) " +
                "ON CONFLICT (title) DO NOTHING",
                params);
        if (inserted == 0) {
            return;
        }
        Set<String> tags = job.getTags();
        if (tags == null || tags.isEmpty()) {
            return;
        }
        SqlParameterSource[] batch = tags.stream()
                .map(tag -> new MapSqlParameterSource()
                        .addValue("jobTitle", job.getTitle())
                        .addValue("tag", tag))
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(
                "INSERT INTO job_tags(job_title, tag) VALUES (:jobTitle, :tag) " +
                "ON CONFLICT DO NOTHING",
                batch);
    }

    public Job findByTitle(String title) {
        List<Job> result = jdbc.query(
                SELECT_BASE + " WHERE j.title = :title",
                new MapSqlParameterSource("title", title),
                JOBS_EXTRACTOR);
        return result.isEmpty() ? null : result.get(0);
    }

    public Collection<Job> findAll() {
        return jdbc.query(SELECT_BASE + " ORDER BY j.title", JOBS_EXTRACTOR);
    }

    @Transactional
    public void delete(String title) {
        jdbc.update(
                "DELETE FROM jobs WHERE title = :title",
                new MapSqlParameterSource("title", title));
    }

    private static final ResultSetExtractor<List<Job>> JOBS_EXTRACTOR = (ResultSet rs) -> {
        record Acc(String company, int requiredExperience, Set<String> tags) {}
        LinkedHashMap<String, Acc> byTitle = new LinkedHashMap<>();
        while (rs.next()) {
            String title = rs.getString("title");
            String company = rs.getString("company");
            int requiredExperience = rs.getInt("required_experience");
            Acc acc = byTitle.computeIfAbsent(title,
                    t -> new Acc(company, requiredExperience, new HashSet<>()));
            String tag = rs.getString("tag");
            if (tag != null) {
                acc.tags().add(tag);
            }
        }
        List<Job> jobs = new ArrayList<>(byTitle.size());
        byTitle.forEach((title, acc) ->
                jobs.add(new Job(title, acc.company(), acc.tags(), acc.requiredExperience())));
        return jobs;
    };
}
