package az.m10.service;

import az.m10.domain.UserMeal;
import az.m10.domain.enums.UnitType;
import az.m10.dto.MealDTO;
import az.m10.dto.TotalMealValuesDTO;
import az.m10.dto.UserMealDTO;
import az.m10.dto.UserMealResponse;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.MealRepository;
import az.m10.repository.UserMealRepository;
import az.m10.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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

    public UserMealDTO addUserMeal(Long userId, Long mealId, Float quantity) {
        userRepository.findById(userId).orElseThrow(
                () -> new CustomNotFoundException("User not found.")
        );
        mealRepository.findById(mealId).orElseThrow(
                () -> new CustomNotFoundException("Meal not found.")
        );

        UserMeal userMeal = new UserMeal();
        userMeal.setMealId(mealId);
        userMeal.setUserId(userId);
        userMeal.setQuantity(quantity);
        userMeal.setDate(LocalDate.now());

        UserMeal savedUserMeal = userMealRepository.save(userMeal);
        return savedUserMeal.toDto();
    }

    @Transactional
    public void deleteUserMeal(Long userId, Long mealId) {
        LocalDate today = LocalDate.now();
        userMealRepository.deleteByUserIdAndMealIdAndDate(userId, mealId, today);
    }

    public UserMealResponse getUserMealsByDate(Long userId, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserMealDTO> userMealDTOS = userMealRepository.findByUserIdAndDate(userId, date, pageable).map(UserMeal::toDto);
        TotalMealValuesDTO totalValues = calculateTotalMealValues(userMealDTOS);

        return new UserMealResponse(userMealDTOS, totalValues);
    }

    public UserMealResponse findAllByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserMealDTO> userMealDTOS = userMealRepository.findByUserIdAndDate(userId, LocalDate.now(), pageable).map(UserMeal::toDto);
        TotalMealValuesDTO totalValues = calculateTotalMealValues(userMealDTOS);

        return new UserMealResponse(userMealDTOS, totalValues);
    }

    public TotalMealValuesDTO calculateTotalMealValues(Page<UserMealDTO> userMealDTOS) {
        double totalProtein = 0.0, totalCarbs = 0.0, totalCal = 0.0, totalFat = 0.0, totalSugar = 0.0;

        double unitAdjustment = 100.0;

        for (UserMealDTO userMealDTO : userMealDTOS) {
            MealDTO meal = userMealDTO.getMeal();
            double quantity = userMealDTO.getQuantity();

            double multiplier = isWeightOrVolumeUnit(userMealDTO.getMeal().getUnitType())
                    ? quantity / unitAdjustment
                    : quantity;

            totalProtein += meal.getProtein() * multiplier;
            totalCarbs += meal.getCarbs() * multiplier;
            totalCal += meal.getCal() * multiplier;
            totalFat += meal.getFat() * multiplier;
            totalSugar += meal.getSugar() * multiplier;
        }

        return new TotalMealValuesDTO(totalProtein, totalCarbs, totalCal, totalFat, totalSugar);
    }

    private boolean isWeightOrVolumeUnit(UnitType unitType) {
        return Set.of(UnitType.GRAM, UnitType.MILLILITER).contains(unitType);
    }

}