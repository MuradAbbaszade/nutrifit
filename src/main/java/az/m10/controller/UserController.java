package az.m10.controller;

import az.m10.auth.UserDetailsService;
import az.m10.domain.User;
import az.m10.dto.ImageDTO;
import az.m10.dto.NutritionRequirement;
import az.m10.dto.UserDTO;
import az.m10.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = {"http://localhost:3000", "*"})
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public UserController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
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
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestPart UserDTO dto,
                                          @ModelAttribute ImageDTO imageDTO) {
        UserDTO e = userService.update(id, dto, imageDTO.getImage());
        return ResponseEntity.ok(e);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateAuthenticatedUser(Principal principal, @Valid @RequestPart UserDTO dto,
                                                           @ModelAttribute ImageDTO imageDTO) {
        UserDTO e = userService.update(getAuthenticatedUserId(principal), dto, imageDTO.getImage());
        return ResponseEntity.ok(e);
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/nutrition-requirement")
    public ResponseEntity<NutritionRequirement> getDailyNutritionRequirement(Principal principal){
        return ResponseEntity.ok(userService.calculateDailyNutritionRequirement(getAuthenticatedUserId(principal)));
    }

    private Long getAuthenticatedUserId(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        return user.getId();
    }
}
