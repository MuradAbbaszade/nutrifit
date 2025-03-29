package az.m10.controller;

import az.m10.domain.enums.MealType;
import az.m10.dto.ImageDTO;
import az.m10.dto.MealDTO;
import az.m10.service.MealService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/meals")
@CrossOrigin(origins = {"http://localhost:3000", "*"})
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MealDTO> add(@Valid @RequestPart MealDTO dto,
                                       @ModelAttribute ImageDTO imageDTO) throws IOException {
        MealDTO createdVolunteer = mealService.add(dto, imageDTO.getImage());
        return ResponseEntity.ok(createdVolunteer);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            mealService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AccessDeniedException exp) {
            throw new AccessDeniedException(exp.getMessage());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<MealDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<MealDTO> update(@PathVariable Long id, @Valid @RequestPart MealDTO dto,
                                          @ModelAttribute ImageDTO imageDTO) {
        MealDTO e = mealService.update(id, dto, imageDTO.getImage());
        return ResponseEntity.ok(e);
    }

    @GetMapping
    public ResponseEntity<Page<MealDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(mealService.findAll(page, size));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<MealDTO>> findByType(@PathVariable MealType type,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(mealService.findByType(type, page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MealDTO>> findByName(@RequestParam String name,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(mealService.findByName(name, page, size));
    }
}
