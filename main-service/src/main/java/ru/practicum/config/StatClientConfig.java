package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatClient;

@Configuration
public class StatClientConfig {

    @Bean
    public StatClient getStatClient(@Value("${stat.url}") String statServerUrl) {
        return new StatClient(statServerUrl);
    }
}
