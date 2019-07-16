package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;

public interface ProjectService extends PlannedEntityService<ProjectDTO> {

    @NotNull
    Boolean deleteProjectTasks(@Nullable SessionDTO session, @Nullable String projectId) throws Exception;

}
