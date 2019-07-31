package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;

import java.util.List;

public interface ProjectServicePageable {

    @NotNull
    List<ProjectDTO> findAllByUserId(@Nullable String loggedUserId)
            throws AccessForbiddenException;

    @NotNull
    Page<ProjectDTO> findAllByUserId(@Nullable String loggedUserId, @Nullable PageRequest page)
            throws AccessForbiddenException;

    @Nullable
    ProjectDTO findOne(@Nullable String loggedUserId, @Nullable String requestedProjectId)
            throws AccessForbiddenException, IncorrectDataException;

    boolean exists(@Nullable String loggedUserId, @Nullable String projectId);

    void persist(@Nullable String loggedUserId, @Nullable ProjectDTO persistableProject)
            throws AccessForbiddenException, IncorrectDataException;

    void merge(@Nullable String loggedUserId, @Nullable ProjectDTO persistableProject)
            throws AccessForbiddenException, IncorrectDataException;

    void remove(@Nullable String loggedUserId, @Nullable String removableProjectId)
            throws AccessForbiddenException, IncorrectDataException;

}
