package ru.stoliarenkoas.tm.webserver.api.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Session;

import java.util.Collection;

public interface SessionRepository {

    @NotNull
    Collection<Session> findAll() throws Exception;

    @NotNull
    Collection<Session> findByUserId(@NotNull String userId) throws Exception;

    @Nullable
    Session findById(@NotNull String id) throws Exception;

    @NotNull
    Boolean containsId(@NotNull String id) throws Exception;

    @NotNull
    Boolean persist(@NotNull Session session) throws Exception;

    @NotNull
    Boolean deleteById(@NotNull String id) throws Exception;

    @NotNull
    Boolean deleteByUserId(@NotNull String userId) throws Exception;

    @NotNull
    Boolean deleteAll() throws Exception;

}
