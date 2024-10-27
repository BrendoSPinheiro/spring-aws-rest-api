package br.com.brendosp.springawsrestapi.infra.http.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticateUserRequestDTO(
    @Schema(description = "User email", example = "brendo@mail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email(message = "Invalid email")
    String email,

    @Schema(description = "User password", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 6, message = "Password must have at least 6 characters")
    String password
) {
}
