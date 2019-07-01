package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Project;
import ru.stoliarenkoas.tm.webserver.entity.Session;

public interface ProjectService extends PlannedEntityService<Project> {

    @NotNull
    Boolean deleteProjectTasks(@Nullable Session session, @Nullable String projectId) throws Exception;

}
