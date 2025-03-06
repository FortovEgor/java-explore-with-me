package ru.practicum.event.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService service;
    private final EventMapper mapper;

    @GetMapping
    public List<EventDto> findEvents(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = "0") Long from,
                                     @RequestParam(defaultValue = "10") Long size) {
        List<Event> events = service.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        return mapper.toEventDto(events);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventAdminRequest request) {
        Event event = service.updateEventByAdmin(eventId, request);
        return mapper.toEventDto(event);
    }
}
