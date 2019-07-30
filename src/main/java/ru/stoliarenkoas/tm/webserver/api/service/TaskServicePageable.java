package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;

import java.util.List;

public interface TaskServicePageable {

    @NotNull
    List<TaskDTO> findAllByUserId(@Nullable String loggedUserId) throws AccessForbiddenException;

    @NotNull
    Page<TaskDTO> findAllByUserId(@Nullable String loggedUserId, @Nullable PageRequest page)
                                  throws AccessForbiddenException;

    @NotNull
    Page<TaskDTO> findAllByProjectId(@Nullable String loggedUserId,
                                     @NotNull String projectId,
                                     @Nullable PageRequest page)
                                     throws AccessForbiddenException, IncorrectDataException;

    @Nullable
    TaskDTO findOne(@Nullable String loggedUserId, @Nullable String requestedTaskId)
                    throws AccessForbiddenException, IncorrectDataException;

    @NotNull
    Boolean exists(@Nullable String loggedUserId, @Nullable String requestedTaskId);

    void persist(@Nullable String loggedUserId, @Nullable TaskDTO persistableTask)
                 throws AccessForbiddenException, IncorrectDataException;

    void merge(@Nullable String loggedUserId, @Nullable TaskDTO persistableTask)
               throws AccessForbiddenException, IncorrectDataException;

    void remove(@Nullable String loggedUserId, @Nullable String removableTaskId)
                throws AccessForbiddenException, IncorrectDataException;

    void removeByProjectId(@Nullable String loggedUserId, @Nullable String projectId)
                           throws AccessForbiddenException, IncorrectDataException;

}
