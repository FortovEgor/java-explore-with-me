package ru.practicum.ewm.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.model.EndpointHitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    public void saveHit(EndpointHitDto hit) {
        log.info("Saving hit %s", hit);
        statsRepository.save(EndpointHitMapper.toHit(hit));
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Getting statistics (start: %s; end: %s; uris: %s; unique: %b", start, end, uris, unique);
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong timestamp.");
        }
        List<ViewStats> result;
        if (unique) {
            if (uris != null) {
                result = statsRepository.findHitsWithUniqueIpWithUris(uris, start, end);
                log.info("Response: %s", result);
                return result;
            }
            result = statsRepository.findHitsWithUniqueIpWithoutUris(start, end);
            log.info("Response: %s", result);
            return result;
        }
        if (uris != null) {
            result = statsRepository.findAllHitsWithUris(uris, start, end);
            log.info("Response: %s", result);
            return result;
        }
        result = statsRepository.findAllHitsWithoutUris(start, end);
        log.info("Response: %s", result);
        return result;
    }
}
