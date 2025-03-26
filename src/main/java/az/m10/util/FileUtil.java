package az.m10.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUtil {
    public String saveImage(MultipartFile imageFile, String path) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileExtension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + fileExtension;
            try {
                Path folderPath = Paths.get(path);
                if (!Files.exists(folderPath)) {
                    Files.createDirectories(folderPath);
                }
                byte[] bytes = imageFile.getBytes();
                Path filePath = folderPath.resolve(fileName);
                Files.write(filePath, bytes);
                return filePath.toString();
            } catch (IOException e) {
                throw new IllegalArgumentException("An error occurred while saving profile image");
            }
        }
        return null;
    }
}
