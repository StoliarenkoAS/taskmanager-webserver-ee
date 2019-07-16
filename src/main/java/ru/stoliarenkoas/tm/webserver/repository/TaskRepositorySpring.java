package ru.stoliarenkoas.tm.webserver.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stoliarenkoas.tm.webserver.model.entity.Task;

import java.util.List;

@Repository
public interface TaskRepositorySpring extends CrudRepository<Task, String> {

    List<Task> findByProject_User_Id(@NotNull @Param("userId") String userId);

    @Query(value = "SELECT e FROM Task e WHERE e.project.user.id = ?1 ORDER BY ?2")
    List<Task> findByProject_User_IdOrderBy(@NotNull String userId, @NotNull String sortColumn);

    List<Task> findByProject_User_IdAndName(@NotNull String userId, @NotNull String name);

    List<Task> findByProject_User_IdAndProject_Id(@NotNull String userId, @NotNull String projectId);

    @Query(value = "SELECT e FROM Task e WHERE e.project.user.id = ?1 AND e.name = ?2 ORDER BY ?3")
    List<Task> findByProject_User_IdAndNameOrderBy(@NotNull String userId, @NotNull String name, @NotNull String sortColumn);

    Task findAnyByProject_User_IdAndId(@NotNull String userId, @NotNull String id);

    @Query("SELECT e FROM Task e WHERE (e.project.user.id = :userId) AND (e.name LIKE :line OR e.description LIKE :line)")
    List<Task> search(@NotNull @Param("userId") String userId, @NotNull @Param("line") String searchLine);

    void deleteByProject_User_IdAndId(@NotNull String userId, @NotNull String id);

    int deleteByProject_User_IdAndName(@NotNull String userId, @NotNull String name);

    int deleteByProject_User_Id(@NotNull String userId);
    
}
