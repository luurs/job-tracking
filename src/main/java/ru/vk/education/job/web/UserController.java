package ru.vk.education.job.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.service.UserService;
import ru.vk.education.job.web.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) {
        userService.create(dto.toDomain());
        return dto;
    }

    @GetMapping
    public List<UserDto> list() {
        return userService.list().stream()
                .map(UserDto::fromDomain)
                .toList();
    }
}
