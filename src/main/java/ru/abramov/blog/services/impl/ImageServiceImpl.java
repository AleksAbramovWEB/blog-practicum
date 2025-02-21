package ru.abramov.blog.services.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.abramov.blog.configs.AppConfig;
import ru.abramov.blog.services.ImageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final AppConfig appConfig;
    private final ResourceLoader resourceLoader;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    @SneakyThrows
    @Override
    public Optional<String> save(MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            return Optional.empty();
        }

        String mimeType = Files.probeContentType(
                Path.of(
                        Objects.requireNonNull(
                                imageFile.getOriginalFilename()
                        )
                )
        );

        if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported image type: " + mimeType);
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

        String uploadImageDir = appConfig.getUploadImageDir();

        Path uploadPath = Path.of(
                resourceLoader.getResource("classpath:/")
                        .getFile()
                        .getPath(),
                uploadImageDir
        );

        Path imagePath = uploadPath.resolve(fileName);

        Files.copy(
                imageFile.getInputStream(),
                imagePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return Optional.of("/" + uploadImageDir + fileName);
    }

}
