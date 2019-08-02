package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.User;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryPageable extends JpaRepository<User, String> {

    @Query(value = "SELECT u FROM User u WHERE u.id = :userId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<User> findOne(@NotNull @Param("userId") String userId);

    @Query(value = "SELECT u FROM User u WHERE u.login = :login")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    User findByLogin(@NotNull @Param("login") String login);

    @Query(value = "SELECT u FROM User u WHERE u.login = :login AND u.passwordHash = :passwordHash")
    Optional<User> login(@NotNull @Param("login") String login, @NotNull @Param("passwordHash") String passwordHash);

}
