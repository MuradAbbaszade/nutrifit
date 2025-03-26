package az.m10.service;

import az.m10.domain.Meal;
import az.m10.domain.User;
import az.m10.domain.UserMeal;
import az.m10.dto.UserMealDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.MealRepository;
import az.m10.repository.UserMealRepository;
import az.m10.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMealService {
    private final UserMealRepository userMealRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public UserMealService(UserMealRepository userMealRepository, UserRepository userRepository, MealRepository mealRepository) {
        this.userMealRepository = userMealRepository;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    public UserMealDTO addUserMeal(Long userId, Long mealId) {
        userRepository.findById(userId).orElseThrow(
                () -> new CustomNotFoundException("User not found.")
        );
        mealRepository.findById(mealId).orElseThrow(
                () -> new CustomNotFoundException("Meal not found.")
        );

        UserMeal userMeal = new UserMeal();
        userMeal.setMealId(mealId);
        userMeal.setUserId(userId);
        userMeal.setDate(LocalDate.now());

        UserMeal savedUserMeal = userMealRepository.save(userMeal);
        return savedUserMeal.toDto();
    }

    @Transactional
    public void deleteUserMeal(Long userId, Long mealId) {
        LocalDate today = LocalDate.now();
        userMealRepository.deleteByUserIdAndMealIdAndDate(userId, mealId, today);
    }

    public List<UserMealDTO> getUserMealsByDate(Long userId, LocalDate date) {
        List<UserMeal> userMeals = userMealRepository.findByUserIdAndDate(userId, date);
        return userMeals.stream().map(UserMeal::toDto).collect(Collectors.toList());
    }

    public List<UserMealDTO> findAllByUserId(Long userId) {
        List<UserMeal> userMeals = userMealRepository.findByUserId(userId);
        return userMeals.stream().map(UserMeal::toDto).collect(Collectors.toList());
    }
}
