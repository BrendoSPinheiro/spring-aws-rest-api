package br.com.brendosp.springawsrestapi.domain.usecases.impl;

import br.com.brendosp.springawsrestapi.domain.usecases.IJwtUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IJwtUseCase jwtUseCase;

    @Mock
    private Authentication authentication;

    private AuthenticationUseCaseImpl sut;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new AuthenticationUseCaseImpl(authenticationManager, jwtUseCase);
    }

    @BeforeEach
    void setUpEach() {
        reset(authenticationManager, jwtUseCase);
    }

    @Test
    void itShouldAuthenticateUserAndReturnTokenWhenCredentialsAreValid() {
        var command = new AuthenticateUserCommand("email", "password");
        when(authenticationManager.authenticate(any()))
            .thenReturn(authentication);
        when(jwtUseCase.generateToken(any())).thenReturn("token");

        var result = sut.authenticate(command);

        assertThat(result).isEqualTo("token");
        verify(authenticationManager).authenticate(any());
        verify(jwtUseCase).generateToken(any());
    }

}