package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.hit.CreateHitRequest;
import ru.practicum.hit.HitDto;
import ru.practicum.stats.StatDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

public class StatClient {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate rest;

    public StatClient(String baseUrl) {
        rest = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofMillis(1000L))
                .setReadTimeout(Duration.ofMillis(1000L))
                .build();
    }

    public HitDto makeHit(String app, String uri, String ip, LocalDateTime timestamp) {
        CreateHitRequest request = CreateHitRequest.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(formatter.format(timestamp))
                .build();

        return rest.postForObject("/hit", request, HitDto.class);
    }

    public List<StatDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        StringJoiner queryString = new StringJoiner("&");
        queryString.add("start=" + formatter.format(start));
        queryString.add("end=" + formatter.format(end));

        if (uris != null && !uris.isEmpty()) {
            queryString.add("uris=" + String.join("uris=", uris));
        }
        queryString.add("unique=" + unique);

        ResponseEntity<List<StatDto>> response = rest
                .exchange("/stats?" + queryString, HttpMethod.GET, null, new ParameterizedTypeReference<List<StatDto>>() {});
        return response.getBody();
    }
}
