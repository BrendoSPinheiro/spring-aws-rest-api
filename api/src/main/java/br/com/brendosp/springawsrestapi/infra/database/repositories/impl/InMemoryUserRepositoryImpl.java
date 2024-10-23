package br.com.brendosp.springawsrestapi.infra.database.repositories.impl;

import br.com.brendosp.springawsrestapi.domain.entities.User;
import br.com.brendosp.springawsrestapi.infra.database.repositories.IUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepositoryImpl implements IUserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> findById(UUID id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Override
    public void deleteById(UUID id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
