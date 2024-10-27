package br.com.brendosp.springawsrestapi.domain.usecases;

import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;

public interface IAuthenticationUseCase {
    String authenticate(AuthenticateUserCommand authenticateUserCommand);
}
