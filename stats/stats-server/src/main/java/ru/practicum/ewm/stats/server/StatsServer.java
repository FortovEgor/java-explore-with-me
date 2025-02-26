package ru.practicum.ewm.stats.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("ru.practicum.ewm.stats.server.*")
@ComponentScan(basePackages = { "ru.practicum.ewm.stats.server.*" })
@EntityScan("ru.practicum.ewm.stats.server.model.*")
@SpringBootApplication
public class StatsServer {

    public static void main(String[] args) {
        SpringApplication.run(StatsServer.class, args);
    }
}
