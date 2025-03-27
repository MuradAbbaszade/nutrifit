package az.m10.service;

import az.m10.domain.UserFavoriteMeal;
import az.m10.dto.UserFavoriteMealDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.MealRepository;
import az.m10.repository.UserFavoriteMealRepository;
import az.m10.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserFavoriteMealService {
    private final UserFavoriteMealRepository userFavoriteMealRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public UserFavoriteMealService(UserFavoriteMealRepository userFavoriteMealRepository, UserRepository userRepository, MealRepository mealRepository) {
        this.userFavoriteMealRepository = userFavoriteMealRepository;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    @Transactional
    public UserFavoriteMealDTO addFavoriteMeal(Long userId, Long mealId) {
        userRepository.findById(userId).orElseThrow(() -> new CustomNotFoundException("User not found."));
        mealRepository.findById(mealId).orElseThrow(() -> new CustomNotFoundException("Meal not found."));

        Optional<UserFavoriteMeal> existingFavorite = userFavoriteMealRepository.findByUserIdAndMealId(userId, mealId);
        if (existingFavorite.isPresent()) {
            throw new IllegalStateException("Meal is already in favorites.");
        }
        UserFavoriteMeal favoriteMeal = new UserFavoriteMeal();
        favoriteMeal.setUserId(userId);
        favoriteMeal.setMealId(mealId);
        return userFavoriteMealRepository.save(favoriteMeal).toDto();
    }

    @Transactional
    public void removeFavoriteMeal(Long userId, Long mealId) {
        userFavoriteMealRepository.deleteByUserIdAndMealId(userId, mealId);
    }

    public Page<UserFavoriteMealDTO> getUserFavoriteMeals(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userFavoriteMealRepository.findByUserId(userId, pageable).map(UserFavoriteMeal::toDto);
    }
}
