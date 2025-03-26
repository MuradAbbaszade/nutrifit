package az.m10.controller;

import az.m10.auth.UserDetailsService;
import az.m10.domain.User;
import az.m10.dto.JwtResponse;
import az.m10.dto.SignInDTO;
import az.m10.dto.TokenRefreshRequest;
import az.m10.dto.UserDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.service.UserService;
import az.m10.util.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://localhost:3000", "*"})
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody SignInDTO signInDTO) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getUsername(), signInDTO.getPassword()));
        User user = userService.findByUsername(signInDTO.getUsername());
        String refreshToken = jwtUtil.generateRefreshTokenFromUsername(signInDTO.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        return new JwtResponse(token, refreshToken, user.toDto().getProfileImageUrl());
    }

    @PostMapping("/sign-up")
    public JwtResponse signUp(@RequestBody UserDTO userDTO) {
        try {
            userService.findByUsername(userDTO.getUsername());
            throw new IllegalArgumentException("User already exist");
        } catch (CustomNotFoundException ex) {
            UserDTO savedUser = userService.add(userDTO);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    savedUser.getUsername(), null, new ArrayList<>()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authentication);
            String refreshToken = jwtUtil.generateRefreshTokenFromUsername(userDTO.getUsername());
            return new JwtResponse(token, refreshToken, null);
        }
    }


    @PostMapping("/refresh-token")
    public JwtResponse refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        jwtUtil.validateToken(requestRefreshToken);

        String username = jwtUtil.extractClaims(request.getRefreshToken()).getSubject();
        String token = jwtUtil.generateTokenFromUsername(username);
        requestRefreshToken = jwtUtil.generateRefreshTokenFromUsername(username);

        User user = userService.findByUsername(username);
        return new JwtResponse(token, requestRefreshToken, user.toDto().getProfileImageUrl());
    }

    @GetMapping
    public ResponseEntity<UserDTO> getAuthenticatedUser(Principal principal){
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        return ResponseEntity.ok(user.toDto());
    }
}
