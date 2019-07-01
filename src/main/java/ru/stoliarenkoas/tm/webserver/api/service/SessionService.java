package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Session;

import java.util.Collection;

public interface SessionService {

    @NotNull
    Collection<Session> getAll() throws Exception;

    @NotNull
    Collection<Session> getByUserId(@Nullable String userId) throws Exception;

    @Nullable
    Session getById(@Nullable String id) throws Exception;

    @NotNull
    Boolean isOpen(@Nullable String id) throws Exception;

    @NotNull
    Boolean open(@Nullable Session session) throws Exception;

    @NotNull
    Boolean closeById(@Nullable String id) throws Exception;

    @NotNull
    Boolean closeByUserId(@Nullable String userId) throws Exception;

    @NotNull
    Boolean closeAll() throws Exception;

}
