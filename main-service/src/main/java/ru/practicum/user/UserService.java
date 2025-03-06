package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    @Transactional
    public User createUser(NewUserRequest request) {
        log.info("creating user with data = {}", request);
        User user = mapper.toUser(request);
        return repo.save(user);
    }

    public List<User> getUsers(List<Long> ids, long from, long size) {
        log.info("getting users with ids = {} from {}, size = {}", ids, from, size);
        if (ids == null || ids.isEmpty()) {
            return repo.findUsersWithFromAndWithSize(from, size);
        }
        return repo.findUsersWithIdsAndWithFromAndWithSize(ids, from, size);
    }

    @Transactional
    public void deleteUserById(long userId) {
        log.info("deleting user with id = {}", userId);
        User user = getUsersById(userId);
        repo.delete(user);
    }

    public User getUsersById(long userId) {
        log.info("getting user with id {}", userId);
        return repo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    public List<User> getUsersById(List<Long> ids) {
        log.info("getting all users with ids = {}", ids);
        return repo.findAllById(ids);
    }
}
