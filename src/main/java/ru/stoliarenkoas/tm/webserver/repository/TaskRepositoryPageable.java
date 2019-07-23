package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.Task;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepositoryPageable extends JpaRepository<Task, String> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Task> findAllByProject_User_Id(@NotNull String userId);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Task> findAllByProject_User_Id(@NotNull String userId, @NotNull Pageable pageable);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Page<Task> findAllByProject_Id(@NotNull String projectId, @NotNull Pageable pageable);

    @Query(value = "SELECT t FROM Task t WHERE t.project.user.id = :userId AND t.id = :id")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Task> findOne(@NotNull @Param("userId") String userId, @NotNull @Param("id") String id);

    @Modifying
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    void deleteAllByProject_Id(@NotNull String projectId);

}
