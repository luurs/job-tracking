package ru.vk.education.job.cli;

import org.springframework.stereotype.Component;
import ru.vk.education.job.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserListCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^user-list\\s*$");

    private final UserService userService;

    public UserListCommandHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line) {
        userService.list().forEach(user -> System.out.println(user.toString()));
    }
}
