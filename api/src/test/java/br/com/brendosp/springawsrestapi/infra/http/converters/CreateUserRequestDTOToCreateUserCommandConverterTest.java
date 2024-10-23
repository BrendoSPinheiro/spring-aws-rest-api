package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import br.com.brendosp.springawsrestapi.infra.http.dtos.request.CreateUserRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRequestDTOToCreateUserCommandConverterTest {

    private final CreateUserRequestDTOToCreateUserCommandConverter sut = new CreateUserRequestDTOToCreateUserCommandConverter();


    @Test
    @DisplayName("It should convert CreateUserRequestDTO to CreateUserCommand")
    void itShouldConvertCreateUserRequestDTOToCreateUserCommand() {
        var createUserRequestDTO = new CreateUserRequestDTO(
            "Brendo Souza",
            "brendo@mail.com",
            "123456"
        );


        CreateUserCommand command = sut.convert(createUserRequestDTO);

        assertThat(command).isNotNull();
        assertThat(command.name()).isEqualTo(createUserRequestDTO.name());
        assertThat(command.email()).isEqualTo(createUserRequestDTO.email());
        assertThat(command.password()).isEqualTo(createUserRequestDTO.password());
    }
}