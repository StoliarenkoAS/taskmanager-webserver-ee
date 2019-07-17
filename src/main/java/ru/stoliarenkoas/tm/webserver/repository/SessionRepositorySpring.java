package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.Session;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface SessionRepositorySpring extends CrudRepository<Session, String> {

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<Session> findByUser_Id(@NotNull String userId);

    void deleteById(@NotNull String id);

    int deleteByUser_Id(@NotNull String userId);

    void deleteAll();

}
