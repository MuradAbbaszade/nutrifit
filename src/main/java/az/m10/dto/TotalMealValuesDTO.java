package az.m10.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalMealValuesDTO {
    private BigDecimal totalProtein;
    private BigDecimal totalCarbs;
    private BigDecimal totalCal;
    private BigDecimal totalFat;
    private BigDecimal totalSugar;
}
