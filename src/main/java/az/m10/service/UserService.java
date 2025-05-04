package az.m10.service;

import az.m10.domain.Meal;
import az.m10.domain.User;
import az.m10.domain.enums.ActivityLevel;
import az.m10.domain.enums.Gender;
import az.m10.domain.enums.Goal;
import az.m10.dto.MealDTO;
import az.m10.dto.NutritionRequirement;
import az.m10.dto.UserDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.UserRepository;
import az.m10.util.FileUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final FileUtil fileUtil;

    private final String PROFILE_IMAGE_PATH = "src/main/resources/uploads/profile-images/";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FileUtil fileUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileUtil = fileUtil;
    }

    public UserDTO add(UserDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = userRepository.save(dto.toEntity(Optional.of(new User())));
        return user.toDto();
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found")
        );
        return user.toDto();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> findAll() {
        List<User> entities = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (User entity : entities) {
            dtos.add(entity.toDto());
        }
        return dtos;
    }

    public UserDTO update(Long id, UserDTO dto, MultipartFile profileImageFile) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found.")
        );
        user = dto.toEntity(Optional.of(user));
        if (profileImageFile != null) {
            user.setProfileImageUrl(fileUtil.saveImage(profileImageFile, PROFILE_IMAGE_PATH));
        }
        userRepository.save(user);
        return user.toDto();
    }

    public UserDTO partialUpdate(Long id, UserDTO dto, MultipartFile profileImageFile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (dto != null) {
            if (dto.getUsername() != null) user.setUsername(dto.getUsername());
            if (dto.getPassword() != null) user.setPassword(dto.getPassword());
            if (dto.getFullName() != null) user.setFullName(dto.getFullName());
            if (dto.getGender() != null) user.setGender(dto.getGender());
            if (dto.getAge() != null) user.setAge(dto.getAge());
            if (dto.getHeight() != null) user.setHeight(dto.getHeight());
            if (dto.getWeight() != null) user.setWeight(dto.getWeight());
            if (dto.getGoal() != null) user.setGoal(dto.getGoal());
            if (dto.getActivityLevel() != null) user.setActivityLevel(dto.getActivityLevel());
        }

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            user.setProfileImageUrl(fileUtil.saveImage(profileImageFile, PROFILE_IMAGE_PATH));
        }

        userRepository.save(user);
        return user.toDto();
    }

    public NutritionRequirement calculateDailyNutritionRequirement(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found.")
        );
        double bmr = calculateBMR(user);
        double tdee = bmr * getActivityMultiplier(user.getActivityLevel());
        double calories = Math.round(adjustCaloriesForGoal(tdee, user.getGoal()));

        double protein = Math.round((calories * 0.25) / 4);
        double fat = Math.round((calories * 0.25) / 9);
        double carbs = Math.round((calories * 0.50) / 4);
        double sugar = Math.round(carbs * 0.15);

        return new NutritionRequirement(calories, protein, carbs, fat, sugar);
    }

    private double calculateBMR(User user) {
        if (user.getGender() == Gender.MALE) {
            return 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5;
        } else {
            return 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161;
        }
    }

    private double getActivityMultiplier(ActivityLevel level) {
        switch (level) {
            case PASSIVE:
                return 1.2;
            case LOW_ACTIVE:
                return 1.375;
            case ACTIVE:
                return 1.55;
            case VERY_ACTIVE:
                return 1.725;
            default:
                return 1.2;
        }
    }

    private double adjustCaloriesForGoal(double calories, Goal goal) {
        switch (goal) {
            case WEIGHT_LOSS:
                return calories - 500;
            case WEIGHT_GAIN:
                return calories + 500;
            case MAINTAIN_WEIGHT:
            default:
                return calories;
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomNotFoundException("User not found"));
    }
}
