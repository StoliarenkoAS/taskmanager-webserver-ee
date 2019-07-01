package ru.stoliarenkoas.tm.webserver.api.repository;

import org.jetbrains.annotations.NotNull;
import ru.stoliarenkoas.tm.webserver.api.entity.PlannedEntity;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;

import java.util.Collection;

public interface PlannedEntityRepository<T extends PlannedEntity> extends Repository<T> {

    @NotNull
    Collection<T> findAllAndSort(@NotNull String userId, @NotNull ComparatorType comparatorType) throws Exception;

    @NotNull
    Collection<T> findByNameAndSort(@NotNull String userId, @NotNull ComparatorType comparatorType, @NotNull String name) throws Exception;

    @NotNull
    Collection<T> search(@NotNull String userId, @NotNull String searchLine) throws Exception;
}
