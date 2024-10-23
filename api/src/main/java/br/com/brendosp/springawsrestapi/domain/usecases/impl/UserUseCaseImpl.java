package br.com.brendosp.springawsrestapi.domain.usecases.impl;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.domain.usecases.IUserUseCase;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import br.com.brendosp.springawsrestapi.domain.usecases.exceptions.EmailAlreadyInUseException;
import br.com.brendosp.springawsrestapi.infra.database.repositories.IUserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCaseImpl implements IUserUseCase {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UUID createUser(final CreateUserCommand createUserCommand) {
        userRepository.findByEmail(createUserCommand.email()).ifPresent(user -> {
            throw new EmailAlreadyInUseException();
        });

        String encodedPassword = passwordEncoder.encode(createUserCommand.password());

        var user = new User(
            createUserCommand.name(),
            createUserCommand.email(),
            encodedPassword
        );

        User createdUser = userRepository.save(user);

        return createdUser.getId();
    }

    @Override
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }
}
