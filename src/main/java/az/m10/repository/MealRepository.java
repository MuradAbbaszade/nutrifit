package az.m10.repository;

import az.m10.domain.Meal;
import az.m10.domain.enums.MealType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends BaseJpaRepository<Meal, Long> {
    Page<Meal> findAll(Pageable pageable);

    Page<Meal> findByType(MealType type, Pageable pageable);

    @Query("SELECT m FROM Meal m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Meal> findByName(@Param("name") String name, Pageable pageable);
}
