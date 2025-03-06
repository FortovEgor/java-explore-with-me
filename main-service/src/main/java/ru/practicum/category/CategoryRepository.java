package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            SELECT c
            FROM Category c
            ORDER BY id
            LIMIT :size
            OFFSET :from""")
    List<Category> getCategoriesWithFromAndOfSize(Long from, Long size);
}
