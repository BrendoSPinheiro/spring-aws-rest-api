package br.com.brendosp.springawsrestapi.domain.usecases.impl;

import br.com.brendosp.springawsrestapi.domain.usecases.IAuthenticationUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.IJwtUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.AuthenticateUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCaseImpl implements IAuthenticationUseCase {

    private final AuthenticationManager authenticationManager;
    private final IJwtUseCase jwtUseCase;

    @Override
    public String authenticate(AuthenticateUserCommand authenticateUserCommand) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
            authenticateUserCommand.email(),
            authenticateUserCommand.password()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return jwtUseCase.generateToken((UserDetails) authentication.getPrincipal());
    }
}
