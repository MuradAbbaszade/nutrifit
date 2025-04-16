package az.m10.domain;

import az.m10.config.Constants;
import az.m10.domain.enums.MealType;
import az.m10.domain.enums.UnitType;
import az.m10.dto.MealDTO;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@Table(name = "meals")
public class Meal extends BaseEntity<MealDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double cal;
    private Double protein;
    private Double fat;
    private Double sugar;
    private Double carbs;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private MealType type;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @Override
    public MealDTO toDto() {
        return MealDTO.builder()
                .id(this.id)
                .name(this.name)
                .cal(this.cal)
                .protein(this.protein)
                .fat(this.fat)
                .sugar(this.sugar)
                .carbs(this.carbs)
                .description(this.description)
                .type(this.type)
                .unitType(this.unitType)
                .image(this.image != null ?
                        Constants.UPLOAD_PATH + "meal-images/".concat(this.image.substring(this.image.lastIndexOf("/") + 1)) : null)
                .build();
    }

}
