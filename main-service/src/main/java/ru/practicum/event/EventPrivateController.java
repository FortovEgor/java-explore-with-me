package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.participation.Participation;
import ru.practicum.participation.ParticipationMapper;
import ru.practicum.participation.ParticipationService;
import ru.practicum.participation.dto.ParticipationRequestDto;

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
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto dto,
                                                    @PathVariable Long userId) {

        Event event = service.createEvent(dto, userId);
        return mapper.toFullDto(event);
    }

    @GetMapping
    public List<EventShortDto> getUsers(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") Long from,
                                                        @RequestParam(defaultValue = "10") Long size) {

        List<Event> events = service.getEventsByUser(userId, from, size);
        return mapper.toShortDto(events);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {

        Event event = service.getEvent(userId, eventId);
        return mapper.toFullDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest request) {

        Event event = service.updateEventByUser(userId, eventId, request);
        return mapper.toFullDto(event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationsOfEvent(@PathVariable Long userId,
                                                                                  @PathVariable Long eventId) {

        List<Participation> participations = participationService.getParticipationsOfEvent(userId, eventId);
        return participationMapper.toDto(participations);

    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationStatus(@PathVariable Long userId,
                                                                                    @PathVariable Long eventId,
                                                                                    @RequestBody EventRequestStatusUpdateRequest request) {

        EventRequestStatusUpdateResult result = participationService.updateParticipationStatus(userId, eventId, request);
        return result;
    }
}
