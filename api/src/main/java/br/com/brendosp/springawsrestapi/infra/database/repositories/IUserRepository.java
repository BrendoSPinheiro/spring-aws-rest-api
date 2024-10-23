package br.com.brendosp.springawsrestapi.infra.database.repositories;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User save(User user);
    void deleteById(UUID id);
}
