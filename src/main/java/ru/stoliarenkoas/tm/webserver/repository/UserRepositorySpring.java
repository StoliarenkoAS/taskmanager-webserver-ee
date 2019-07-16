package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepositorySpring extends CrudRepository<User, String> {

    User findByLoginAndPasswordHash(@NotNull String login, @NotNull String passwordHash);

    User findAnyById(@NotNull String id);

    List<User> findByLogin(@NotNull String login);

    User removeById(@NotNull String id);

    Collection<User> removeByLogin(@NotNull String name);

}
