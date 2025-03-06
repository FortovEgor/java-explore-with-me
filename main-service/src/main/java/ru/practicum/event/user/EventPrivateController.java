package ru.practicum.event.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.*;
import ru.practicum.participation.model.Participation;
import ru.practicum.participation.ParticipationMapper;
import ru.practicum.participation.ParticipationService;
import ru.practicum.participation.dto.ParticipationRequest;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService service;
    private final EventMapper mapper;
    private final ParticipationService participationService;
    private final ParticipationMapper participationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody CreateEventRequest request,
                                @PathVariable Long userId) {
        Event event = service.createEvent(request, userId);
        return mapper.toEventDto(event);
    }

    @GetMapping
    public List<EventForCompilationDto> getUsers(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") Long from,
                                                 @RequestParam(defaultValue = "10") Long size) {
        List<Event> events = service.getEventsByUser(userId, from, size);
        return mapper.toEventForCompilationDto(events);
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        Event event = service.getEvent(userId, eventId);
        return mapper.toEventDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long userId,
                                @PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventUserRequest request) {
        Event event = service.updateEventByUser(userId, eventId, request);
        return mapper.toEventDto(event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequest> getParticipationsOfEvent(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        List<Participation> participations = participationService.getParticipationsOfEvent(userId, eventId);
        return participationMapper.toDto(participations);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationStatus(@PathVariable Long userId,
                                                                    @PathVariable Long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest request) {
        return  participationService.updateParticipationStatus(userId, eventId, request);
    }
}
