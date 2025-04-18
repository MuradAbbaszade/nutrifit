package az.m10.dto;

import az.m10.domain.User;
import az.m10.domain.enums.ActivityLevel;
import az.m10.domain.enums.Gender;
import az.m10.domain.enums.Goal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends BaseDTO<User> {

    private Long id;
    private String fullName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String username;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String profileImageUrl;
    private Gender gender;
    private Integer age;
    private Integer height;
    private Float weight;
    private Goal goal;
    private ActivityLevel activityLevel;

    @Override
    public User toEntity(Optional<User> existingEntity) {
        User entity = existingEntity.orElseGet(User::new);
        entity.setUsername(this.username);
        if (this.password != null) entity.setPassword(this.password);
        if (this.profileImageUrl != null) entity.setProfileImageUrl(this.profileImageUrl);
        entity.setFullName(this.fullName);
        entity.setGender(this.gender);
        entity.setAge(this.age);
        entity.setHeight(this.height);
        entity.setWeight(this.weight);
        entity.setGoal(this.goal);
        entity.setActivityLevel(this.activityLevel);
        return entity;
    }
}
