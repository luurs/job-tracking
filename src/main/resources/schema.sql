CREATE TABLE IF NOT EXISTS users (
    name VARCHAR(255) PRIMARY KEY,
    experience INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS user_skills (
    user_name VARCHAR(255) NOT NULL REFERENCES users(name) ON DELETE CASCADE,
    skill VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_name, skill)
);

CREATE TABLE IF NOT EXISTS jobs (
    title VARCHAR(255) PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    required_experience INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS job_tags (
    job_title VARCHAR(255) NOT NULL REFERENCES jobs(title) ON DELETE CASCADE,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (job_title, tag)
);
