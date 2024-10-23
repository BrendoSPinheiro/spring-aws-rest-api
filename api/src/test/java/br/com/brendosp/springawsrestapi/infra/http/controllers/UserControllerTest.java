package br.com.brendosp.springawsrestapi.infra.http.controllers;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.domain.usecases.IUserUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.CreateUserRequestDTO;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.CreateUserResponseDTO;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.GetUserByIdResponseDTO;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    @Mock
    private IUserUseCase userUseCase;

    @Mock
    private ConversionService conversionService;

    private UserController sut;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new UserController(userUseCase, conversionService);
    }

    @BeforeEach
    void setUpEach() {
        reset(userUseCase, conversionService);
    }

    @Test
    @DisplayName("It should create user and return created user id")
    void itShouldCreateUserAndReturnCreatedUserId() {
        var request = new CreateUserRequestDTO(
            "Brendo Souza",
            "brendo@mail.com",
            "123456"
        );
        var createUserCommand = new CreateUserCommand(
            "Brendo Souza",
            "brendo@mail.com",
            "123456"
        );
        var createdUserId = UUID.randomUUID();

        when(conversionService.convert(request, CreateUserCommand.class)).thenReturn(createUserCommand);
        when(userUseCase.createUser(createUserCommand)).thenReturn(createdUserId);

        ResponseEntity<CreateUserResponseDTO> response = sut.create(request);
        var responseBody = response.getBody();

        verify(conversionService).convert(request, CreateUserCommand.class);
        verify(userUseCase).createUser(createUserCommand);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull();
        assertThat(responseBody.id()).isEqualTo(createdUserId);
    }

    @Test
    @DisplayName("It should get user by id and return user when exists id")
    void itShouldGetUserByIdAndReturnUserWhenExistsId() {
        var userId = UUID.randomUUID();
        var user = new User("Brendo", "brendo@mail.com", "123456");

        when(userUseCase.getUserById(userId)).thenReturn(user);
        when(conversionService.convert(user, GetUserByIdResponseDTO.class)).thenReturn(
            new GetUserByIdResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            )
        );

        ResponseEntity<GetUserByIdResponseDTO> response = sut.getById(userId);
        var responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.id()).isNotNull().isEqualTo(user.getId());
        assertThat(responseBody.name()).isNotNull().isEqualTo(user.getName());
        assertThat(responseBody.email()).isNotNull().isEqualTo(user.getEmail());
        assertThat(responseBody.createdAt()).isNotNull().isEqualTo(user.getCreatedAt());
        assertThat(responseBody.updatedAt()).isNotNull().isEqualTo(user.getUpdatedAt());
        verify(userUseCase).getUserById(userId);
        verify(conversionService).convert(user, GetUserByIdResponseDTO.class);
    }

    @Test
    @DisplayName("It should return not found when user does not exists")
    void itShouldReturnNotFoundWhenUserDoesNotExists() {
        var userId = UUID.randomUUID();
        when(userUseCase.getUserById(userId)).thenReturn(null);

        ResponseEntity<GetUserByIdResponseDTO> response = sut.getById(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(userUseCase).getUserById(userId);
    }

    @Test
    @DisplayName("It should delete user by id")
    void itShouldDeleteUserById() {
        var userId = UUID.randomUUID();

        sut.deleteById(userId);

        verify(userUseCase).deleteUserById(userId);
    }

}