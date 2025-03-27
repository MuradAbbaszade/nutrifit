package az.m10.dto;

import az.m10.domain.UserFavoriteMeal;
import az.m10.domain.UserMeal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoriteMealDTO extends BaseDTO<UserFavoriteMeal> {
    private Long id;
    private Long userId;
    private Long mealId;
    private LocalDate date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MealDTO meal;

    @Override
    public UserFavoriteMeal toEntity(Optional<UserFavoriteMeal> existingEntity) {
        UserFavoriteMeal entity = existingEntity.orElseGet(UserFavoriteMeal::new);
        entity.setUserId(this.userId);
        entity.setMealId(this.mealId);
        return entity;
    }
}
