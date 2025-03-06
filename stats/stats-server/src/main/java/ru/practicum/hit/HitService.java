package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.hit.dao.HitRepository;
import ru.practicum.hit.model.Hit;
import ru.practicum.stats.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository repo;
    private final HitMapper mapper;

    public Hit save(CreateHitRequest request) {
        return repo.save(mapper.toEntity(request));
    }

    public List<Stat> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("getting statistics request(start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);
        if (start.isAfter(end)) {
            throw new IncorrectRequestException("Начальное время start не может быть больше конечного end");
        }

        if (uris == null || uris.isEmpty()) {
            return unique ? repo.getStatsWithUniqueIpFromStartToEnd(start, end) :
                    repo.getStatisticsFromStartToEnd(start, end);
        }
        return unique ? repo.getStatsByUrisWithUniqueIpFromStartToEndWithUris(start, end, uris) :
                repo.getStatisticsFromStartToEndWithUris(start, end, uris);
    }
}
