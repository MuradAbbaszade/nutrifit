package az.m10.repository;

import az.m10.domain.UserMeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserMealRepository extends JpaRepository<UserMeal, Long> {
    void deleteByUserIdAndMealIdAndDate(Long userId, Long mealId, LocalDate date);

    Page<UserMeal> findByUserIdAndDate(Long userId, LocalDate date, Pageable pageable);

    Page<UserMeal> findByUserId(Long userId, Pageable pageable);
}

