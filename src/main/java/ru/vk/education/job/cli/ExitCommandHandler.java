package ru.vk.education.job.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExitCommandHandler implements CommandHandler {

    private static final Pattern PATTERN = Pattern.compile("^exit\\s*$");

    private final ConfigurableApplicationContext context;

    public ExitCommandHandler(@Autowired(required = false) ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean supports(String line) {
        Matcher matcher = PATTERN.matcher(line);
        return matcher.matches();
    }

    @Override
    public void handle(String line) {
        if (context != null) {
            int code = SpringApplication.exit(context);
            System.exit(code);
        } else {
            System.exit(0);
        }
    }
}
