package br.com.brendosp.springawsrestapi.domain.usecases;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtUseCase {
    String generateToken(UserDetails userDetails);

    Boolean isValidToken(String token);

    UserDetails getUserDetailsFromToken(String token);
}
