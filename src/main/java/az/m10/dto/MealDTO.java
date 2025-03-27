package az.m10.dto;

import az.m10.config.Constants;
import az.m10.domain.Meal;
import az.m10.domain.enums.MealType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDTO extends BaseDTO<Meal> {
    private Long id;
    private String name;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal sugar;
    private BigDecimal carbs;
    private String description;
    private MealType type;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;

    public MealDTO(Meal meal) {
        this.id = meal.getId();
        this.carbs = meal.getCarbs();
        this.fat = meal.getFat();
        this.protein = meal.getProtein();
        this.sugar = meal.getSugar();
        this.description = meal.getDescription();
        this.image = meal.getImage() != null ?
                Constants.UPLOAD_PATH + "meal-images/".concat(meal.getImage().substring(meal.getImage().lastIndexOf("\\") + 1)) : null;
        this.name = meal.getName();
    }

    @Override
    public Meal toEntity(Optional<Meal> existingEntity) {
        Meal entity = existingEntity.orElseGet(Meal::new);
        entity.setName(this.name);
        entity.setProtein(this.protein);
        entity.setFat(this.fat);
        entity.setSugar(this.sugar);
        entity.setCarbs(this.carbs);
        entity.setDescription(this.description);
        entity.setImage(this.image);
        return entity;
    }
}
