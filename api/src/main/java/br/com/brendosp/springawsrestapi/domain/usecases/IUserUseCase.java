package br.com.brendosp.springawsrestapi.domain.usecases;


import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.domain.usecases.dtos.CreateUserCommand;
import java.util.UUID;

public interface IUserUseCase {
    User getUserById(UUID id);
    UUID createUser(CreateUserCommand createUserCommand);
    void deleteUserById(UUID id);
}
