package ru.vk.education.job.cli;

import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.storage.Storage;

public interface CommandHandler {

    boolean supports(String line);

    void handle(String line, Storage storage, SuggestService suggestService);
}
