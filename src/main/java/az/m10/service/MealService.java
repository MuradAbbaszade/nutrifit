package az.m10.service;

import az.m10.domain.Meal;
import az.m10.domain.enums.MealType;
import az.m10.dto.MealDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.MealRepository;
import az.m10.util.FileUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealService {
    private final MealRepository mealRepository;
    private final FileUtil fileUtil;

    private final String MEAL_IMAGE_PATH = "src/main/resources/uploads/meal-images/";

    public MealService(MealRepository mealRepository, FileUtil fileUtil) {
        this.mealRepository = mealRepository;
        this.fileUtil = fileUtil;
    }

    public MealDTO add(MealDTO dto, MultipartFile image) {
        dto.setImage(fileUtil.saveImage(image, MEAL_IMAGE_PATH));
        Meal meal = mealRepository.save(dto.toEntity(Optional.of(new Meal())));
        return meal.toDto();
    }

    public MealDTO findById(Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found")
        );
        return meal.toDto();
    }

    public void delete(Long id) {
        mealRepository.deleteById(id);
    }

    public MealDTO update(Long id, MealDTO dto, MultipartFile profileImageFile) {
        Meal meal = mealRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found.")
        );
        meal = dto.toEntity(Optional.of(meal));
        if (profileImageFile != null) {
            meal.setImage(fileUtil.saveImage(profileImageFile, MEAL_IMAGE_PATH));
        }
        mealRepository.save(meal);
        return meal.toDto();
    }

    public Page<MealDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findAll(pageable).map(Meal::toDto);
    }

    public Page<MealDTO> findByType(MealType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findByType(type, pageable).map(Meal::toDto);
    }

    public Page<MealDTO> findByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findByName(name, pageable).map(Meal::toDto);
    }
}
