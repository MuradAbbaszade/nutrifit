package az.m10.repository;

import az.m10.domain.UserMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserMealRepository extends JpaRepository<UserMeal, Long> {
    void deleteByUserIdAndMealIdAndDate(Long userId, Long mealId, LocalDate date);

    List<UserMeal> findByUserIdAndDate(Long userId, LocalDate date);

    List<UserMeal> findByUserId(Long userId);
}

