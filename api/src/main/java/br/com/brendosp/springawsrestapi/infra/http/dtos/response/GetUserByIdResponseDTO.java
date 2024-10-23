package br.com.brendosp.springawsrestapi.infra.http.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserByIdResponseDTO(
    @Schema(description = "User ID")
    UUID id,
    @Schema(description = "User name")
    String name,
    @Schema(description = "User email")
    String email,
    @Schema(description = "Created At")
    LocalDateTime createdAt,
    @Schema(description = "Updated At")
    LocalDateTime updatedAt
) {
}
