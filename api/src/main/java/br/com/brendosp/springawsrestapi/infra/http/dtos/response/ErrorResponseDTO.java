package br.com.brendosp.springawsrestapi.infra.http.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
    String error,
    String statusCode,
    String errorMessage,
    List<String> errorMessages
) {
}