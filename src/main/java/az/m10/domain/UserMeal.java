package az.m10.domain;

import az.m10.dto.MealDTO;
import az.m10.dto.UserMealDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_meal")
public class UserMeal extends BaseEntity<UserMealDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "meal_id", nullable = false)
    private Long mealId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "meal_id", insertable = false, updatable = false)
    private Meal meal;

    @Column(nullable = false)
    private LocalDate date;

    private Float quantity;

    @Override
    public UserMealDTO toDto() {
        return UserMealDTO.builder()
                .id(this.id)
                .userId(this.userId)
                .meal(Optional.ofNullable(this.meal).map(MealDTO::new).orElse(null))
                .mealId(this.mealId)
                .date(this.date)
                .quantity(this.quantity)
                .build();
    }
}
