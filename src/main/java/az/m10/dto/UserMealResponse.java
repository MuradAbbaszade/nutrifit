package az.m10.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMealResponse {
    private Page<UserMealDTO> userMealDTOS;
    private TotalMealValuesDTO totalMealValuesDTO;
}
