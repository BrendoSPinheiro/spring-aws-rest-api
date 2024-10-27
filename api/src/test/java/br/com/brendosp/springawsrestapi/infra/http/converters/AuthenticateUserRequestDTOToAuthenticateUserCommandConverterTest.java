package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.AuthenticateUserRequestDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticateUserRequestDTOToAuthenticateUserCommandConverterTest {

    @Test
    void itShouldConvertAuthenticateUserRequestDTOToAuthenticateUserCommand() {

        var authenticateUserRequestDTO = new AuthenticateUserRequestDTO("email", "password");

        var converter = new AuthenticateUserRequestDTOToAuthenticateUserCommandConverter();

        AuthenticateUserCommand response = converter.convert(authenticateUserRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(authenticateUserRequestDTO.email());
        assertThat(response.password()).isEqualTo(authenticateUserRequestDTO.password());
    }

}