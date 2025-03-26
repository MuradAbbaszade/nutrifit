package az.m10.repository;

import az.m10.domain.Meal;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends BaseJpaRepository<Meal, Long> {
}
