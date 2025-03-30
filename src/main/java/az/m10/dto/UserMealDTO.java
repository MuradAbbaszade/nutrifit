package az.m10.dto;

import az.m10.domain.UserMeal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMealDTO extends BaseDTO<UserMeal> {
    private Long id;
    private Long userId;
    private Long mealId;
    private LocalDate date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MealDTO meal;

    private BigDecimal quantity;

    @Override
    public UserMeal toEntity(Optional<UserMeal> existingEntity) {
        UserMeal entity = existingEntity.orElseGet(UserMeal::new);
        entity.setUserId(this.userId);
        entity.setMealId(this.mealId);
        entity.setDate(this.date);
        entity.setQuantity(this.quantity);
        return entity;
    }
}
