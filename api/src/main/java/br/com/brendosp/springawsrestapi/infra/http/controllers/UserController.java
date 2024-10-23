package br.com.brendosp.springawsrestapi.infra.http.controllers;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.domain.usecases.IUserUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.CreateUserRequestDTO;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.CreateUserResponseDTO;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.GetUserByIdResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final IUserUseCase userUseCase;
    private final ConversionService conversionService;

    @GetMapping("/me")
    @Operation(summary = "Get current user")
    public ResponseEntity<?> getCurrentUser() {
        return ResponseEntity.ok("Current user");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<GetUserByIdResponseDTO> getById(@PathVariable("id") UUID id) {
        User user = userUseCase.getUserById(id);

        return user != null
            ? ResponseEntity.ok(conversionService.convert(user, GetUserByIdResponseDTO.class))
            : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<CreateUserResponseDTO> create(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        UUID createdUserId = userUseCase.createUser(
            conversionService.convert(createUserRequestDTO, CreateUserCommand.class)
        );

        var response = new CreateUserResponseDTO(createdUserId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        userUseCase.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
