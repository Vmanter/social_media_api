package com.YK.social_media.controllers;

import com.YK.social_media.models.Image;
import com.YK.social_media.repositories.ImageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.awt.font.ImageGraphicAttribute;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController // Представление возвращать не будем, поэтому Rest
@RequiredArgsConstructor
@Tag(name = "Картинки")
public class ImageController {
    private final ImageRepository imageRepository;

    @Operation(
            summary = "Подробный просмотр картинки",
            description = "Endpoint просмотра картинки по ее ID для всех пользователей",
            responses = {
                    @ApiResponse(
                            description = "При успешном запросе, получаем содержимое картинки для просмотра",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
