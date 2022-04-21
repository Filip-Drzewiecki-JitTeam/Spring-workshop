package team.jit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Profile("local")
@RestController
@RequestMapping("/configs")
public class ConfigController {

    private final Environment environment;

    public ConfigController(Environment environment) {
        this.environment = environment;
        log.info("Creating config controller");
    }

    @GetMapping
    public Map<String, Object> getConfig() {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());

        Map<String, Object> configs = new HashMap<>();
        configs.put("local", true);
        if (profiles.contains("dev")) {
            configs.put("dev", true);
        }
        return configs;
    }
}
