package br.com.brendosp.springawsrestapi.infra.http.controllers;

import br.com.brendosp.springawsrestapi.domain.usecases.IAuthenticationUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.AuthenticateUserRequestDTO;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.AuthenticateUserResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final IAuthenticationUseCase authenticationUseCase;
    private final ConversionService conversionService;

    @PostMapping
    public ResponseEntity<AuthenticateUserResponseDTO> authenticate(@Valid @RequestBody AuthenticateUserRequestDTO authenticateUserRequestDTO) {
        String accessToken = authenticationUseCase.authenticate(
            conversionService.convert(authenticateUserRequestDTO, AuthenticateUserCommand.class)
        );

        var response = new AuthenticateUserResponseDTO(accessToken);

        return ResponseEntity.ok(response);
    }
}
