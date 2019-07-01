package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.entity.PlannedEntity;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Session;

import java.util.Collection;

public interface PlannedEntityService<T extends PlannedEntity> extends Service<T> {

    @NotNull
    Collection<T> search(@Nullable Session session, @Nullable String searchLine) throws Exception;

    @NotNull
    Collection<T> getAllSorted(@Nullable Session session, @Nullable ComparatorType comparatorType) throws Exception;

    @NotNull
    Collection<T> getAllByNameSorted(@Nullable Session session, @Nullable String name, @Nullable ComparatorType comparatorType) throws Exception;

}
