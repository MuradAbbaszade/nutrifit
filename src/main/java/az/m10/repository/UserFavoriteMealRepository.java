package az.m10.repository;

import az.m10.domain.UserFavoriteMeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFavoriteMealRepository extends JpaRepository<UserFavoriteMeal, Long> {
    Optional<UserFavoriteMeal> findByUserIdAndMealId(Long userId, Long mealId);

    void deleteByUserIdAndMealId(Long userId, Long mealId);

    Page<UserFavoriteMeal> findByUserId(Long userId, Pageable pageable);
}
