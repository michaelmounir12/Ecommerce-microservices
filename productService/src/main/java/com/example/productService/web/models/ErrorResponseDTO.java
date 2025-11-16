package com.example.productService.web.models;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponseDTO(
        @Schema(description = "Error message") String message,
        @Schema(description = "Error code") int status
) {}
