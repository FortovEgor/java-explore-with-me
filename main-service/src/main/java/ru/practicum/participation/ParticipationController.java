package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation.dto.ParticipationRequest;
import ru.practicum.participation.model.Participation;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationController {

    private final ParticipationService service;
    private final ParticipationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequest createParticipation(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        Participation participation = service.createParticipation(userId, eventId);
        return mapper.toDto(participation);
    }

    @GetMapping
    public List<ParticipationRequest> getUserParticipations(@PathVariable Long userId) {
        List<Participation> participations = service.getUserParticipations(userId);
        return mapper.toDto(participations);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequest cancelParticipation(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        Participation participation = service.cancelParticipation(userId, requestId);
        return mapper.toDto(participation);
    }
}
