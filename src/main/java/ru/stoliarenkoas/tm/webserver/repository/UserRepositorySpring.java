package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.User;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepositorySpring extends CrudRepository<User, String> {

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    User findByLoginAndPasswordHash(@NotNull String login, @NotNull String passwordHash);

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    User findAnyById(@NotNull String id);

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<User> findByLogin(@NotNull String login);

    User removeById(@NotNull String id);

    Collection<User> removeByLogin(@NotNull String name);

}
