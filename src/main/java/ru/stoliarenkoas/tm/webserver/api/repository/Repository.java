package ru.stoliarenkoas.tm.webserver.api.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.entity.Entity;

import java.util.Collection;

public interface Repository<T extends Entity> {

    @NotNull
    Collection<T> findAll(@NotNull String userId) throws Exception;

    @NotNull
    Collection<T> findByName(@NotNull String userId, @NotNull String name) throws Exception;

    @Nullable
    T findOne(@NotNull String userId, @NotNull String id) throws Exception;

    @NotNull
    Boolean persist(@NotNull T object) throws Exception;

    @NotNull
    Boolean merge(@NotNull String userId, @NotNull T object) throws Exception;

    @Nullable
    String remove(@NotNull String userId, @NotNull String id) throws Exception;

    @Nullable
    String remove(@NotNull String userId, @NotNull T object) throws Exception;

    @NotNull
    Collection<String> removeByName(@NotNull String userId, @NotNull String name) throws Exception;

    @NotNull
    Collection<String> removeAll(@NotNull String userId) throws Exception;
}
