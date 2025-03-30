package az.m10.dto;

import az.m10.config.Constants;
import az.m10.domain.Meal;
import az.m10.domain.enums.MealType;
import az.m10.domain.enums.UnitType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDTO extends BaseDTO<Meal> {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal cal;
    @NotNull
    private BigDecimal protein;
    @NotNull
    private BigDecimal fat;
    @NotNull
    private BigDecimal sugar;
    @NotNull
    private BigDecimal carbs;
    private String description;
    @NotNull
    private MealType type;
    @NotNull
    private UnitType unitType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;

    public MealDTO(Meal meal) {
        this.id = meal.getId();
        this.cal = meal.getCal();
        this.carbs = meal.getCarbs();
        this.fat = meal.getFat();
        this.protein = meal.getProtein();
        this.sugar = meal.getSugar();
        this.description = meal.getDescription();
        this.type = meal.getType();
        this.unitType = meal.getUnitType();
        this.image = meal.getImage() != null ?
                Constants.UPLOAD_PATH + "meal-images/".concat(meal.getImage().substring(meal.getImage().lastIndexOf("/") + 1)) : null;
        this.name = meal.getName();
    }

    @Override
    public Meal toEntity(Optional<Meal> existingEntity) {
        Meal entity = existingEntity.orElseGet(Meal::new);
        entity.setName(this.name);
        entity.setCal(this.cal);
        entity.setProtein(this.protein);
        entity.setFat(this.fat);
        entity.setSugar(this.sugar);
        entity.setCarbs(this.carbs);
        entity.setDescription(this.description);
        entity.setImage(this.image);
        entity.setType(this.type);
        entity.setUnitType(this.unitType);
        return entity;
    }
}
