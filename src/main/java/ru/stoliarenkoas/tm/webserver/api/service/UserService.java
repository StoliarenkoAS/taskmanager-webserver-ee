package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

public interface UserService extends Service<UserDTO> {

    @Nullable
    SessionDTO login(@Nullable String login, @Nullable String password) throws Exception;

    @NotNull
    Boolean logout(@Nullable SessionDTO session) throws Exception;

    @NotNull
    Boolean register(@Nullable String login, @Nullable String password) throws Exception;

    @NotNull
    String showUserProfile(@Nullable SessionDTO session) throws Exception;

    @NotNull
    Boolean changePassword(@Nullable SessionDTO session, @Nullable String oldPassword, @Nullable String newPassword) throws Exception;

}
