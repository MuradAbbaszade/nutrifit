package az.m10.domain;

import az.m10.config.Constants;
import az.m10.domain.enums.ActivityLevel;
import az.m10.domain.enums.Gender;
import az.m10.domain.enums.Goal;
import az.m10.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User extends BaseEntity<UserDTO> implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String profileImageUrl;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    private Integer height;

    private Float weight;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities = new HashSet<>();

    private boolean isEnabled = true;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public UserDTO toDto() {
        return UserDTO.builder()
                .id(this.id)
                .fullName(this.fullName)
                .profileImageUrl(this.profileImageUrl != null ?
                        Constants.UPLOAD_PATH + "profile-images/".concat(this.profileImageUrl.substring(this.profileImageUrl.lastIndexOf("\\") + 1)) : null)
                .password(this.password)
                .username(this.username)
                .gender(this.gender)
                .age(this.age)
                .height(this.height)
                .weight(this.weight)
                .goal(this.goal)
                .activityLevel(this.activityLevel)
                .build();
    }
}
