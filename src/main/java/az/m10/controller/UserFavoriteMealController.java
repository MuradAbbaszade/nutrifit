package az.m10.controller;

import az.m10.auth.UserDetailsService;
import az.m10.domain.User;
import az.m10.dto.UserFavoriteMealDTO;
import az.m10.service.UserFavoriteMealService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-favorite-meals")
@CrossOrigin(origins = {"http://localhost:3000", "*"})
public class UserFavoriteMealController {

    private final UserFavoriteMealService userFavoriteMealService;
    private final UserDetailsService userDetailsService;

    public UserFavoriteMealController(UserFavoriteMealService userFavoriteMealService, UserDetailsService userDetailsService) {
        this.userFavoriteMealService = userFavoriteMealService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<UserFavoriteMealDTO> addFavoriteMeal(Principal principal, @RequestParam Long mealId) {
        UserFavoriteMealDTO userFavoriteMeal = userFavoriteMealService.addFavoriteMeal(getAuthenticatedUserId(principal), mealId);
        return ResponseEntity.ok(userFavoriteMeal);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavoriteMeal(Principal principal, @RequestParam Long mealId) {
        userFavoriteMealService.removeFavoriteMeal(getAuthenticatedUserId(principal), mealId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<UserFavoriteMealDTO>> getUserFavoriteMeals(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserFavoriteMealDTO> userFavoriteMeals = userFavoriteMealService.getUserFavoriteMeals(getAuthenticatedUserId(principal), page, size);
        return ResponseEntity.ok(userFavoriteMeals);
    }

    private Long getAuthenticatedUserId(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        return user.getId();
    }
}
