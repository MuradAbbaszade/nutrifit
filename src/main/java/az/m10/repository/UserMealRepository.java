package az.m10.repository;

import az.m10.domain.UserMeal;
import az.m10.domain.enums.MealType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserMealRepository extends JpaRepository<UserMeal, Long> {
    void deleteByUserIdAndMealIdAndDate(Long userId, Long mealId, LocalDate date);

    Page<UserMeal> findByUserIdAndDate(Long userId, LocalDate date, Pageable pageable);

    @Query("SELECT um FROM UserMeal um " +
            "JOIN Meal m ON um.mealId = m.id " +
            "WHERE um.userId = :userId " +
            "AND um.date = :date " +
            "AND m.type = :type")
    Page<UserMeal> findByUserIdAndDateAndMealType(@Param("userId") Long userId,
                                                  @Param("date") LocalDate date,
                                                  @Param("type") MealType type,
                                                  Pageable pageable);


    Page<UserMeal> findByUserId(Long userId, Pageable pageable);
}

