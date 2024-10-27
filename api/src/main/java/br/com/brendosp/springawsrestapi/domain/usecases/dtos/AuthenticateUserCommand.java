package br.com.brendosp.springawsrestapi.domain.usecases.dtos;

public record AuthenticateUserCommand(
    String email,
    String password
) {
}
