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
import ru.stoliarenkoas.tm.webserver.model.entity.Project;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepositoryPageable extends JpaRepository<Project, String> {

    List<Project> findAllByUser_Id(@NotNull String userId);

    Page<Project> findAllByUser_Id(@NotNull String userId, @NotNull Pageable pageable);

    @Query(value = "SELECT p FROM Project p WHERE p.user.id = :userId AND p.id = :projectId")
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Project> findOne(@NotNull @Param("userId") String userId, @NotNull @Param("projectId") String projectId);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    boolean existsByUser_IdAndId(@NotNull String userId, @NotNull String id);

    @Modifying
    void deleteAllByUser_Id(@NotNull String userId);

}
