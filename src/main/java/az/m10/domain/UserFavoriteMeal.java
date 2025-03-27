package az.m10.domain;

import az.m10.dto.MealDTO;
import az.m10.dto.UserFavoriteMealDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_favorite_meals")
public class UserFavoriteMeal extends BaseEntity<UserFavoriteMealDTO> {

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

    @Override
    public UserFavoriteMealDTO toDto() {
        return UserFavoriteMealDTO.builder()
                .id(this.id)
                .userId(this.userId)
                .meal(Optional.ofNullable(this.meal).map(MealDTO::new).orElse(null))
                .mealId(this.mealId)
                .build();
    }
}
