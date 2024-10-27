package br.com.brendosp.springawsrestapi.infra.http.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticateUserResponseDTO(
    @Schema(description = "The authentication accessToken")
    String accessToken
) {
}
