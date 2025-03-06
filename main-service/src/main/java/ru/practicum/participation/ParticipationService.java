package ru.practicum.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventService;
import ru.practicum.event.model.EventState;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.exception.IncorrectActionException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.participation.model.Participation;
import ru.practicum.participation.model.ParticipationStatus;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.util.List;

import static ru.practicum.participation.model.ParticipationStatus.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationService {

    private final UserService userService;
    private final EventService eventService;
    private final ParticipationRepository repo;
    private final ParticipationMapper mapper;

    @Transactional
    public Participation createParticipation(long userId, long eventId) {
        log.info("creating participation for event with id {} by user with id {}", eventId, userId);

        User user = userService.getUsersById(userId);
        Event event = eventService.getFullEventDataById(eventId);

        if (event.getInitiator().equals(user)) {
            throw new IncorrectActionException("Нельзя отправлять заявку на участие в собственном событии");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new IncorrectActionException("Нельзя участвовать в неопубликованном событии");
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new IncorrectActionException("Достигнут лимит участников события");
        }

        ParticipationStatus status = PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = CONFIRMED;
        }

        Participation participation = Participation.builder()
                .event(event)
                .requester(user)
                .status(status)
                .build();

        log.info("saving participation {} to DB", participation);
        return repo.save(participation);
    }

    public List<Participation> getUserParticipations(long userId) {
        log.info("getting user participations, userId: {}", userId);
        User user = userService.getUsersById(userId);
        return repo.findByRequester(user);
    }

    @Transactional
    public Participation cancelParticipation(long userId, long requestId) {
        log.info("cancel participation {} by user {}", requestId, userId);

        User user = userService.getUsersById(userId);
        Participation participation = getById(requestId);

        if (!participation.getRequester().equals(user)) {
            throw new NotFoundException("Заявка с id = " + requestId + " не найдена");
        }

        participation.setStatus(CANCELED);

        return repo.save(participation);
    }

    public List<Participation> getParticipationsOfEvent(long userId, long eventId) {
        User user = userService.getUsersById(userId);
        Event event = eventService.getEventById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        return repo.findByEvent(event);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateParticipationStatus(long userId, long eventId, EventRequestStatusUpdateRequest request) {
        log.info("updating participation for event with id = {} by user with id = {} (request = {})", eventId, userId, request);

        User user = userService.getUsersById(userId);
        Event event = eventService.getFullEventDataById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие с id = " + eventId + " не найдено.");
        }

        if (event.getParticipantLimit() != 0 && event.getRequestModeration()) {
            List<Participation> participations = repo.findAllById(request.getRequestIds());

            if (request.getStatus() == CONFIRMED && event.getConfirmedRequests() + participations.size() > event.getParticipantLimit()) {
                throw new IncorrectActionException("Достигнут лимит по заявкам на данное событие");
            }

            if (participations.stream().anyMatch(p -> p.getStatus() != PENDING)) {
                throw new IncorrectActionException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }

            switch (request.getStatus()) {
                case REJECTED -> {
                    participations.forEach(p -> p.setStatus(REJECTED));
                    repo.saveAll(participations);
                }
                case CONFIRMED -> {
                    participations.stream()
                            .limit(event.getParticipantLimit() - event.getConfirmedRequests())
                            .forEach(p -> p.setStatus(CONFIRMED));

                    log.info("saving participations {} to DB", participations);
                    repo.saveAll(participations);

                    // др заявки отклоняем
                    if (event.getConfirmedRequests() + participations.size() > event.getParticipantLimit()) {
                        repo.changeStatusForEventRequests(PENDING, REJECTED, event);
                    }
                }
            }
        }

        List<Participation> confirmedParticipations = repo.findByEventAndStatus(event, CONFIRMED);
        List<Participation> rejectedParticipations = repo.findByEventAndStatus(event, REJECTED);

        return new EventRequestStatusUpdateResult(
                mapper.toDto(confirmedParticipations),
                mapper.toDto(rejectedParticipations)
        );
    }

    public Participation getById(long id) {
        log.info("getting participation with id = {}", id);
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка на участие с id = " + id + " не найдена."));
    }
}
