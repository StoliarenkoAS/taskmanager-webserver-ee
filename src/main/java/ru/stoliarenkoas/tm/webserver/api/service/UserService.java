package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;

public interface UserService extends Service<User> {

    @Nullable
    Session login(@Nullable String login, @Nullable String password) throws Exception;

    @NotNull
    Boolean logout(@Nullable Session session) throws Exception;

    @NotNull
    Boolean register(@Nullable String login, @Nullable String password) throws Exception;

    @NotNull
    String showUserProfile(@Nullable Session session) throws Exception;

    @NotNull
    Boolean changePassword(@Nullable Session session, @Nullable String oldPassword, @Nullable String newPassword) throws Exception;

}
