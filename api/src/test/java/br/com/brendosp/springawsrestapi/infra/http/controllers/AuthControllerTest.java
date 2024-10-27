package br.com.brendosp.springawsrestapi.infra.http.controllers;

import br.com.brendosp.springawsrestapi.domain.usecases.IAuthenticationUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.AuthenticateUserRequestDTO;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.AuthenticateUserResponseDTO;
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
class AuthControllerTest {

    @Mock
    private IAuthenticationUseCase authenticationUseCase;

    @Mock
    private ConversionService conversionService;

    private AuthController sut;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new AuthController(authenticationUseCase, conversionService);
    }

    @BeforeEach
    void setUpEach() {
        reset(authenticationUseCase, conversionService);
    }

    @Test
    @DisplayName("It should authenticate user and return access token when send valid credentials")
    void itShouldAuthenticateUserAndReturnAccessTokenWhenSendValidCredentials() {
        var email = "brendo@mail.com";
        var password = "123456";
        var request = new AuthenticateUserRequestDTO(email, password);
        var command = new AuthenticateUserCommand(email, password);
        when(conversionService.convert(request, AuthenticateUserCommand.class)).thenReturn(command);
        when(authenticationUseCase.authenticate(command)).thenReturn("accessToken");

        ResponseEntity<AuthenticateUserResponseDTO> response = sut.authenticate(request);
        var responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.accessToken()).isNotBlank().isEqualTo("accessToken");
        verify(conversionService).convert(request, AuthenticateUserCommand.class);
        verify(authenticationUseCase).authenticate(command);
    }


}