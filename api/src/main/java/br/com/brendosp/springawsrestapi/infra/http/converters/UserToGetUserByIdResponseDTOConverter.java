package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.GetUserResponseDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToGetUserByIdResponseDTOConverter implements Converter<User, GetUserResponseDTO> {
    @Override
    public GetUserResponseDTO convert(final User source) {
        return new GetUserResponseDTO(
            source.getId(),
            source.getName(),
            source.getEmail(),
            source.getCreatedAt(),
            source.getUpdatedAt()
        );
    }
}
