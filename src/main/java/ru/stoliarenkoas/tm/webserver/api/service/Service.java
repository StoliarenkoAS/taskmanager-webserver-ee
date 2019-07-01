package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.entity.Entity;
import ru.stoliarenkoas.tm.webserver.entity.Session;

import java.util.Collection;

public interface Service<T extends Entity> {

    @NotNull
    Collection<T> getAll(@Nullable Session session) throws Exception;

    @NotNull
    Collection<T> getAllByName(@Nullable Session session, @Nullable String name) throws Exception;

    @Nullable
    T get(@Nullable Session session, @Nullable String id) throws Exception;

    Boolean save(@Nullable Session session, @Nullable T object) throws Exception;

    Boolean delete(@Nullable Session session, @Nullable String id) throws Exception;

    Boolean delete(@Nullable Session session, @Nullable T object) throws Exception;

    Boolean deleteByIds(@Nullable Session session, @Nullable Collection<String> ids) throws Exception;

    Boolean deleteByName(@Nullable Session session, @Nullable String name) throws Exception;

    Boolean deleteAll(@Nullable Session session) throws Exception;

}
