package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.AuthenticateUserRequestDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserRequestDTOToAuthenticateUserCommandConverter implements Converter<AuthenticateUserRequestDTO, AuthenticateUserCommand> {
    @Override
    public AuthenticateUserCommand convert(AuthenticateUserRequestDTO source) {
        return new AuthenticateUserCommand(
            source.email(),
            source.password()
        );
    }
}
