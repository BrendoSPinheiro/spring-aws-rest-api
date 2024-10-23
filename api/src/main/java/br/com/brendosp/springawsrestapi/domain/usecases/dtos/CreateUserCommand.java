package br.com.brendosp.springawsrestapi.domain.usecases.dtos;

public record CreateUserCommand(
    String name,
    String email,
    String password
) {
}
