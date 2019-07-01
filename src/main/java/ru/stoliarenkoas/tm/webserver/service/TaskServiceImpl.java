package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.repository.PlannedEntityRepository;
import ru.stoliarenkoas.tm.webserver.api.repository.TaskRepository;
import ru.stoliarenkoas.tm.webserver.api.service.TaskService;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.Task;
import ru.stoliarenkoas.tm.webserver.repository.jdbc.TaskRepositoryMySQL;

import java.util.Collection;
import java.util.Collections;

public class TaskServiceImpl extends AbstractService<Task> implements TaskService {

    public TaskServiceImpl() {
        super(new TaskRepositoryMySQL());
    }

    @Override
    public Boolean deleteChildrenByParentId(@Nullable final Session session, @Nullable final String id) throws Exception { //no children = no problem
        return true;
    }

    @Override
    public Boolean deleteChildrenByParentIds(@Nullable final Session session, @Nullable final Collection<String> ids) throws Exception {
        return true;
    }

    @Override @NotNull
    public Collection<Task> getTasksByProjectId(@Nullable final Session session, @Nullable final String projectId) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || projectId == null) return Collections.emptySet();
        return ((TaskRepository)repository).findByProjectId(userId, projectId);
    }

    @Override
    public @NotNull Collection<String> removeTasksByProjectIds(@Nullable final Session session, @Nullable final Collection<String> ids) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || ids == null || ids.isEmpty() || session == null) return Collections.emptySet();
        return ((TaskRepository)repository).removeByProjectIds(session.getUserId(), ids);
    }

    @Override @NotNull
    public Collection<Task> search(@Nullable final Session session, @Nullable final String searchLine) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || searchLine == null || searchLine.isEmpty()) return Collections.emptySet();
        return ((PlannedEntityRepository<Task>)repository).search(userId, searchLine);
    }

    @Override @NotNull
    public Collection<Task> getAllSorted(@Nullable final Session session, @Nullable final ComparatorType comparatorType) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null) return Collections.emptySet();
        if (comparatorType == null) return getAll(session);
        return ((PlannedEntityRepository<Task>)repository).findAllAndSort(userId, comparatorType);
    }

    @Override @NotNull
    public Collection<Task> getAllByNameSorted(@Nullable final Session session, @Nullable final String name, @Nullable final ComparatorType comparatorType) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || name == null) return Collections.emptySet();
        if (comparatorType == null) return getAllByName(session, name);
        return ((PlannedEntityRepository<Task>)repository).findByNameAndSort(userId, comparatorType, name);
    }

}
