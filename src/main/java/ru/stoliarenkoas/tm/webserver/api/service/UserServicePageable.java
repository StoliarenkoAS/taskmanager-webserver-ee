package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

public interface UserServicePageable {

    @NotNull
    Page<UserDTO> findAll(@Nullable String loggedUserId, @Nullable PageRequest page) throws AccessForbiddenException;

    @Nullable
    UserDTO findOne(@Nullable String loggedUserId, @Nullable String requestedUserId) throws AccessForbiddenException;

    @Nullable
    UserDTO login(@Nullable String login, @Nullable String password);

    boolean exists(@Nullable String userId);

    void persist(@Nullable String loggedUserId, @Nullable UserDTO persistableUser) throws AccessForbiddenException;

    void register(@Nullable String login, @Nullable String password) throws IncorrectDataException;

    void remove(@Nullable String loggedUserId, @Nullable String removableUserId) throws AccessForbiddenException;

}
