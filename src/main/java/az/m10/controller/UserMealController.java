package az.m10.controller;

import az.m10.auth.UserDetailsService;
import az.m10.domain.User;
import az.m10.domain.enums.MealType;
import az.m10.dto.UserMealDTO;
import az.m10.dto.UserMealResponse;
import az.m10.service.UserMealService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-meals")
@CrossOrigin(origins = {"http://localhost:3000", "*"})
public class UserMealController {

    private final UserMealService userMealService;
    private final UserDetailsService userDetailsService;

    public UserMealController(UserMealService userMealService, UserDetailsService userDetailsService) {
        this.userMealService = userMealService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<UserMealDTO> addUserMeal(Principal principal, @RequestParam Long mealId, @RequestParam Float quantity) {
        UserMealDTO userMeal = userMealService.addUserMeal(getAuthenticatedUserId(principal), mealId, quantity);
        return ResponseEntity.ok(userMeal);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserMeal(Principal principal, @RequestParam Long mealId) {
        userMealService.deleteUserMeal(getAuthenticatedUserId(principal), mealId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<UserMealResponse> getUserMealsByDate(
            Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserMealResponse response = userMealService.getUserMealsByDate(getAuthenticatedUserId(principal), date, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-type")
    public ResponseEntity<UserMealResponse> getUserMealsByDateAndType(
            Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam MealType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserMealResponse response = userMealService.getUserMealsByDateAndType(getAuthenticatedUserId(principal), date, type , page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-user")
    public ResponseEntity<UserMealResponse> findAllByUserId(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserMealResponse response = userMealService.findAllByUserId(getAuthenticatedUserId(principal), page, size);
        return ResponseEntity.ok(response);
    }

    private Long getAuthenticatedUserId(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        return user.getId();
    }
}
