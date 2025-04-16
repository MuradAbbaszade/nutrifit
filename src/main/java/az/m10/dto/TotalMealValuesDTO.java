package az.m10.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalMealValuesDTO {
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalCal;
    private Double totalFat;
    private Double totalSugar;
}
