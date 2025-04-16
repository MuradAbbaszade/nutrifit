package az.m10.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionRequirement {
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double sugar;
}
