package az.m10.domain;

import az.m10.config.Constants;
import az.m10.domain.enums.MealType;
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
    private BigDecimal cal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal sugar;
    private BigDecimal carbs;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private MealType type;

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
                .image(this.image != null ?
                        Constants.UPLOAD_PATH + "meal-images/".concat(this.image.substring(this.image.lastIndexOf("/") + 1)) : null)
                .build();
    }

}
