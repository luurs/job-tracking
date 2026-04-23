package ru.vk.education.job.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.web.dto.JobDto;

import java.util.List;

@RestController
@RequestMapping("/users")
public class SuggestionController {

    private final SuggestService suggestService;

    public SuggestionController(SuggestService suggestService) {
        this.suggestService = suggestService;
    }

    @GetMapping("/{name}/suggestions")
    public List<JobDto> suggestions(@PathVariable String name,
                                    @RequestParam(defaultValue = "2") int limit) {
        return suggestService.suggest(name, limit).stream()
                .map(JobDto::fromDomain)
                .toList();
    }
}
