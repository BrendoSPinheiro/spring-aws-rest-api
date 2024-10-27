package br.com.brendosp.springawsrestapi.domain.usecases.impl;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.infra.database.repositories.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtUseCaseImplTest {

    private static final String TEST_SECRET = "HLuLwodU11PHayvus2nBdE9YFwDwVSkL";

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Claims claims;

    private JwtUseCaseImpl sut;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new JwtUseCaseImpl(userRepository);
        ReflectionTestUtils.setField(sut, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(sut, "expiration", 1000L * 60 * 60);
        ReflectionTestUtils.setField(sut, "applicationName", "testApp");
    }

    @BeforeEach
    void setUpEach() {
        reset(userRepository, userDetails, claims);
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = sut.generateToken(userDetails);

        assertThat(token).isNotBlank();
    }

    @Test
    void testIsTokenValid() {
        String token = generateTestToken("testUser");
        when(claims.getExpiration())
            .thenReturn(new Date(System.currentTimeMillis() + 1000L * 60));

        boolean isValid = sut.isValidToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void testIsTokenInvalid() {
        String token = generateExpiredTestToken("testUser");

        boolean isValid = sut.isValidToken(token);

        assertThat(isValid).isFalse();
    }

    @Test
    void testGetUserDetails() {
        String token = generateTestToken("testuser@gmail.com");

        User mockUserDTO = new User("Test User", "testuser@gmail.com", "123456");

        when(userRepository.findByEmail("testuser@gmail.com"))
            .thenReturn(Optional.of(mockUserDTO));

        var userDTO = sut.getUserDetailsFromToken(token);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getUsername()).isEqualTo("testuser@gmail.com");
    }

    @Test
    void testGetUserDetailsThrowsException() {
        String token = generateTestToken("testUser");

        when(userRepository.findByEmail("testUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getUserDetailsFromToken(token))
            .isInstanceOf(RuntimeException.class);
    }

    private String generateTestToken(String subject) {
        SecretKey secretKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
            .signWith(secretKey)
            .subject(subject)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
            .compact();
    }

    private String generateExpiredTestToken(String subject) {
        SecretKey secretKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
            .signWith(secretKey)
            .subject(subject)
            .issuedAt(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 2))
            .expiration(new Date(System.currentTimeMillis() - 1000L * 60 * 60))
            .compact();
    }
}
