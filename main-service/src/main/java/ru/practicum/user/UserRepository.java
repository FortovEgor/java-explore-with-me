package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u
            FROM User AS u
            ORDER BY id
            LIMIT :size
            OFFSET :from""")
    List<User> findUsersWithFromAndWithSize(long from, long size);

    @Query("""
            SELECT u
            FROM User AS u
            WHERE u.id IN (:ids)
            ORDER BY id
            LIMIT :size
            OFFSET :from""")
    List<User> findUsersWithIdsAndWithFromAndWithSize(List<Long> ids, long from, long size);
}
