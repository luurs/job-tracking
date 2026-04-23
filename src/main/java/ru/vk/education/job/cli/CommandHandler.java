package ru.vk.education.job.cli;

public interface CommandHandler {

    boolean supports(String line);

    void handle(String line);
}
