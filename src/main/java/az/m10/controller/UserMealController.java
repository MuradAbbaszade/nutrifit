package az.m10.controller;

import az.m10.auth.UserDetailsService;
import az.m10.domain.User;
import az.m10.dto.UserMealDTO;
import az.m10.service.UserMealService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserMealDTO> addUserMeal(Principal principal, @RequestParam Long mealId) {
        UserMealDTO userMeal = userMealService.addUserMeal(getAuthenticatedUserId(principal), mealId);
        return ResponseEntity.ok(userMeal);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserMeal(Principal principal, @RequestParam Long mealId) {
        userMealService.deleteUserMeal(getAuthenticatedUserId(principal), mealId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<UserMealDTO>> getUserMealsByDate(Principal principal,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<UserMealDTO> userMeals = userMealService.getUserMealsByDate(getAuthenticatedUserId(principal), date);
        return ResponseEntity.ok(userMeals);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<UserMealDTO>> findAllByUserId(Principal principal) {
        List<UserMealDTO> userMeals = userMealService.findAllByUserId(getAuthenticatedUserId(principal));
        return ResponseEntity.ok(userMeals);
    }

    private Long getAuthenticatedUserId(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        return user.getId();
    }
}
