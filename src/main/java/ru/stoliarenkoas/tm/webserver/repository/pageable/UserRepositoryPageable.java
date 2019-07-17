package ru.stoliarenkoas.tm.webserver.repository.pageable;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.stoliarenkoas.tm.webserver.model.entity.User;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPageable extends JpaRepository<User, String> {

    @Query(value = "SELECT u FROM User u WHERE u.id = :userId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<User> findOne(@NotNull @Param("userId") String userId);

    @Query(value = "SELECT u FROM User u WHERE u.login = :login")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<User> findByLogin(@NotNull @Param("login") String login);

}
