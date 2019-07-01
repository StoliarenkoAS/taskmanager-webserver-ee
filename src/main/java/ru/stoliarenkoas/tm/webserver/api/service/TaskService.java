package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.Task;

import java.util.Collection;

public interface TaskService extends PlannedEntityService<Task> {

    @NotNull
    Collection<Task> getTasksByProjectId(@Nullable Session session, @Nullable String projectId) throws Exception;

    @NotNull
    Collection<String> removeTasksByProjectIds(@Nullable Session session, @Nullable Collection<String> ids) throws Exception;

}
