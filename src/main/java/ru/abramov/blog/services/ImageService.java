package ru.abramov.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {

    Optional<String> save(MultipartFile imageFile);
}
