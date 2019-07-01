package ru.stoliarenkoas.tm.webserver.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.stoliarenkoas.tm.webserver.entity.Task;

import java.util.Collection;

public interface TaskRepository extends PlannedEntityRepository<Task> {

    @NotNull
    Collection<Task> findByProjectId(@NotNull String userId, @NotNull String projectId) throws Exception;

    @NotNull
    Collection<String> removeByProjectIds(@NotNull String userId, @NotNull Collection<String> ids) throws Exception;


}
