package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.GetUserByIdResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserToGetUserByIdResponseDTOConverterTest {

    private final UserToGetUserByIdResponseDTOConverter converter = new UserToGetUserByIdResponseDTOConverter();

    @Test
    @DisplayName("It should convert user to GetUserByIdResponseDTO")
    void itShouldConvertUserToGetUserByIdResponseDTO() {
        var user = new User("Brendo", "brendo@mail.com", "123456");

        GetUserByIdResponseDTO response = converter.convert(user);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(user.getId());
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.email()).isEqualTo(user.getEmail());
        assertThat(response.createdAt()).isEqualTo(user.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(user.getUpdatedAt());
    }

}