package br.com.brendosp.springawsrestapi.domain.usecases.impl;

import br.com.brendosp.springawsrestapi.domain.usecases.IJwtUseCase;
import br.com.brendosp.springawsrestapi.infra.database.repositories.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUseCaseImpl implements IJwtUseCase {

    private final IUserRepository userRepository;

    @Value("${security.jwt.secret-key:afb789a987d29a72f8563162167be815}")
    private String secret;

    @Value("${security.jwt.expiration:86400000}")
    private Long expiration;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .issuer(applicationName)
            .signWith(getSecretKey())
            .compact();
    }

    @Override
    public Boolean isValidToken(String token) {
        try {
            return getClaimsFromToken(token).getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public UserDetails getUserDetailsFromToken(String token) {
        return userRepository.findByEmail(getClaimsFromToken(token).getSubject())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
