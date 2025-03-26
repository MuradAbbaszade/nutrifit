package az.m10.service;

import az.m10.domain.Meal;
import az.m10.domain.User;
import az.m10.dto.MealDTO;
import az.m10.dto.UserDTO;
import az.m10.exception.CustomNotFoundException;
import az.m10.repository.UserRepository;
import az.m10.util.FileUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final FileUtil fileUtil;

    private final String PROFILE_IMAGE_PATH = "src/main/resources/uploads/profile-images/";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, FileUtil fileUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileUtil = fileUtil;
    }

    public UserDTO add(UserDTO dto) {
        User user = userRepository.save(dto.toEntity(Optional.of(new User())));
        return user.toDto();
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found")
        );
        return user.toDto();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDTO> findAll() {
        List<User> entities = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (User entity : entities) {
            dtos.add(entity.toDto());
        }
        return dtos;
    }

    public UserDTO update(Long id, UserDTO dto, MultipartFile profileImageFile) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Entity not found.")
        );
        user = dto.toEntity(Optional.of(user));
        if (profileImageFile != null) {
            user.setProfileImageUrl(fileUtil.saveImage(profileImageFile, PROFILE_IMAGE_PATH));
        }
        userRepository.save(user);
        return user.toDto();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomNotFoundException("User not found"));
    }
}
