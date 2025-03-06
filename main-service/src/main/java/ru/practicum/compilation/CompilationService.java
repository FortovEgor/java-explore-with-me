package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CreateCompilationRequest;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventService;
import ru.practicum.exception.IncorrectInputDataException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationService {

    private final EventService eventService;
    private final CompilationRepository repo;

    @Transactional
    public Compilation createCompilation(CreateCompilationRequest request) {
        log.info("creating compilation: {}", request);

        List<Event> events = request.getEvents().isEmpty() ? List.of() : eventService.getAllFullById(request.getEvents());
        checkEvents(events, request.getEvents());

        Compilation compilation = Compilation.builder()
                .title(request.getTitle())
                .pinned(request.getPinned())
                .events(events)
                .build();

        log.info("saving compilation {} to DB...", compilation);
        return repo.save(compilation);
    }

    public List<Compilation> getCompilations(boolean pinned, long from, long size) {
        log.info("getting compilations, pinned: {}, from: {}, size: {}", pinned, from, size);
        if (size < 0 || from < 0) {
            throw new IncorrectInputDataException("size и from не могут быть меньше нуля");
        }

        return repo.findAllByPinnedWithFromAndOfSize(pinned, from, size);
    }

    public Compilation getCompilationById(Long id) {
        log.info("getting compilation with id = {}", id);
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id = " + id + " не найдена"));
    }

    @Transactional
    public Compilation updateCompilation(long compilationId, UpdateCompilationRequest request) {
        log.info("updating compilation with id = {}, request data {}", compilationId, request);

        Compilation compilation = getCompilationById(compilationId);

        if (request.getEvents() != null) {
            List<Event> events = request.getEvents().isEmpty() ? List.of() : eventService.getAllFullById(request.getEvents());
            checkEvents(events, request.getEvents());
            compilation.setEvents(events);
        }

        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }

        log.info("saving compilation {} to DB");
        return repo.save(compilation);
    }

    @Transactional
    public void deleteCompilationById(Long id) {
        log.info("deleting compilation with id = {}", id);
        Compilation compilation = getCompilationById(id);
        repo.delete(compilation);
    }

    private void checkEvents(List<Event> events, List<Long> inputEventIds) {
        if (!inputEventIds.isEmpty()) {
            List<Long> eventIds = events.stream().map(Event::getId).toList();
            List<Long> absentIds = inputEventIds.stream()
                    .filter(id -> !eventIds.contains(id)).toList();

            if (!absentIds.isEmpty()) {
                throw new NotFoundException("Можно делать подборки только из существующих событий (" + absentIds + ")");
            }
        }
    }
}
