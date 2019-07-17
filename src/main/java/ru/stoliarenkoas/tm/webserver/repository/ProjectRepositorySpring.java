package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.Project;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ProjectRepositorySpring extends CrudRepository<Project, String> {

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<Project> findByUser_Id(@NotNull @Param("userId") String userId);

    @Query(value = "SELECT e FROM Project e WHERE e.user.id = ?1 ORDER BY ?2")
    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<Project> findByUser_IdOrderBy(@NotNull String userId, @NotNull String sortColumn);

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<Project> findByUser_IdAndName(@NotNull String userId, @NotNull String name);

    @Query(value = "SELECT e FROM Project e WHERE e.user.id = ?1 AND e.name = ?2 ORDER BY ?3")
    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<Project> findByUser_IdAndNameOrderBy(@NotNull String userId, @NotNull String name, @NotNull String sortColumn);

    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    Project findAnyByUser_IdAndId(@NotNull String userId, @NotNull String id);

    @Query("SELECT e FROM Project e WHERE (e.user.id = :userId) AND (e.name LIKE :line OR e.description LIKE :line)")
    @QueryHints({@QueryHint(name="org.hibernate.cacheable", value="true")})
    List<Project> search(@NotNull @Param("userId") String userId, @NotNull @Param("line") String searchLine);

    void deleteByUser_IdAndId(@NotNull String userId, @NotNull String id);

    int deleteByUser_IdAndName(@NotNull @Param("userId") String userId, @NotNull @Param("name") String name);

    int deleteByUser_Id(@NotNull String userId);

}
