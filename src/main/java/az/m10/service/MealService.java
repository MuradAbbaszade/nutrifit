package az.m10.service;

import az.m10.domain.Meal;
import az.m10.dto.MealDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.MealRepository;
import az.m10.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<MealDTO> findAll() {
        List<Meal> entities = mealRepository.findAll();
        List<MealDTO> dtos = new ArrayList<>();
        for (Meal entity : entities) {
            dtos.add(entity.toDto());
        }
        return dtos;
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
}
