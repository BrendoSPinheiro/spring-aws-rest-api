package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.CreateUserRequestDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestDTOToCreateUserCommandConverter implements Converter<CreateUserRequestDTO, CreateUserCommand> {
    @Override
    public CreateUserCommand convert(final CreateUserRequestDTO source) {
        return new CreateUserCommand(source.name(), source.email(), source.password());
    }
}
