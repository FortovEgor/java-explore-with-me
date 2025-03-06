package ru.practicum.event.user;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventSearchSort;
import ru.practicum.event.dto.EventForCompilationDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService service;
    private final EventMapper mapper;

    @GetMapping
    public List<EventForCompilationDto> searchEvents(@RequestParam(required = false) String text,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                     @RequestParam(defaultValue = "EVENT_DATE") EventSearchSort sort,
                                                     @RequestParam(defaultValue = "0") long from,
                                                     @RequestParam(defaultValue = "10") long size) {
        List<Event> events = service.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return mapper.toEventForCompilationDto(events);
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(@PathVariable Long eventId) {
        Event event = service.getPublishedEventById(eventId);
        return mapper.toEventDto(event);
    }
}
