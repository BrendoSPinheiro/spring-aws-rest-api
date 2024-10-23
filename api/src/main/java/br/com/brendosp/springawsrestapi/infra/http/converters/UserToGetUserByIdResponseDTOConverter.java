package br.com.brendosp.springawsrestapi.infra.http.converters;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.GetUserByIdResponseDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToGetUserByIdResponseDTOConverter implements Converter<User, GetUserByIdResponseDTO> {
    @Override
    public GetUserByIdResponseDTO convert(final User source) {
        return new GetUserByIdResponseDTO(
            source.getId(),
            source.getName(),
            source.getEmail(),
            source.getCreatedAt(),
            source.getUpdatedAt()
        );
    }
}
