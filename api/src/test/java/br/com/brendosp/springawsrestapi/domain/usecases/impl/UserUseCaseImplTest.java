package br.com.brendosp.springawsrestapi.domain.usecases.impl;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import br.com.brendosp.springawsrestapi.domain.usecases.exceptions.EmailAlreadyInUseException;
import br.com.brendosp.springawsrestapi.infra.database.repositories.IUserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserUseCaseImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserUseCaseImpl sut;

    private static final CreateUserCommand command = new CreateUserCommand(
        "Brendo",
        "brendo@mail.com",
        "123456"
    );

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new UserUseCaseImpl(userRepository, passwordEncoder);
    }

    @BeforeEach
    void setUpEach() {
        reset(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("It should create user when email is not in use")
    void itShouldCreateUserWhenEmailIsNotInUse() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        var hashedPassword = "hashedPassword";
        var user = new User("Brendo", "brendo@mail.com", hashedPassword);

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(command.password())).thenReturn(hashedPassword);
        when(userRepository.save(any())).thenReturn(user);

        UUID createdUserId = sut.createUser(command);

        assertThat(createdUserId).isNotNull();
        verify(userRepository).findByEmail(command.email());
        verify(passwordEncoder).encode(command.password());
        verify(userRepository).save(captor.capture());

        User capturedUser = captor.getValue();

        assertThat(capturedUser.getId()).isNotNull();
        assertThat(capturedUser.getName()).isEqualTo(command.name());
        assertThat(capturedUser.getEmail()).isEqualTo(command.email());
        assertThat(capturedUser.getPassword()).isEqualTo(hashedPassword);
        assertThat(capturedUser.getCreatedAt()).isNotNull();
        assertThat(capturedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("It should not create user when email is already in use")
    void itShouldNotCreateUserWhenEmailIsAlreadyInUse() {
        var existsUser = new User("abc", "abc@mail.com", "123456");
        when(userRepository.findByEmail(command.email()))
            .thenReturn(Optional.of(existsUser));

        assertThatExceptionOfType(EmailAlreadyInUseException.class)
            .isThrownBy(() -> sut.createUser(command));

        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("It should return user by id")
    void itShouldReturnUserById() {
        var userId = UUID.randomUUID();
        var user = new User("Brendo", "brendo@mail.com", "123456");
        when(userRepository.findById(userId)).thenReturn(
            Optional.of(user)
        );

        User findedUser = sut.getUserById(userId);

        assertThat(findedUser).isNotNull();
        assertThat(findedUser).isEqualTo(user);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("It should return null when user does not exists")
    void itShouldReturnNullWhenUserDoesNotExists() {
        var userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User findedUser = sut.getUserById(userId);

        assertThat(findedUser).isNull();
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("It should delete user by id")
    void itShouldDeleteUserById() {
        var userId = UUID.randomUUID();
        sut.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }
}