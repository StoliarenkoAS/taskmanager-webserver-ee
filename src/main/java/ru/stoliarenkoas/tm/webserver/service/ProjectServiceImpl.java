package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.stoliarenkoas.tm.webserver.api.repository.PlannedEntityRepository;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectService;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Project;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.repository.jdbc.ProjectRepositoryMySQL;

import java.util.Collection;
import java.util.Collections;

@Service
public class ProjectServiceImpl extends AbstractService<Project> implements ProjectService {

    private TaskServiceImpl taskService = new TaskServiceImpl();

    public ProjectServiceImpl() {
        super(new ProjectRepositoryMySQL());
    }

    @Override
    public Boolean deleteChildrenByParentId(@Nullable final Session session, @Nullable final String id) throws Exception {
        return taskService.deleteByIds(session, Collections.singletonList(id));
    }

    @Override
    public Boolean deleteChildrenByParentIds(@Nullable final Session session, @Nullable final Collection<String> ids) throws Exception {
        taskService.removeTasksByProjectIds(session, ids);
        return true;
    }

    @Override @NotNull
    public Collection<Project> search(@Nullable final Session session, @Nullable final String searchLine) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || searchLine == null || searchLine.isEmpty()) return Collections.emptySet();
        return ((PlannedEntityRepository<Project>)repository).search(userId, searchLine);
    }

    @Override @NotNull
    public Collection<Project> getAllSorted(@Nullable final Session session, @Nullable final ComparatorType comparatorType) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null) return Collections.emptySet();
        if (comparatorType == null) return getAll(session);
        return ((PlannedEntityRepository<Project>)repository).findAllAndSort(userId, comparatorType);
    }

    @Override @NotNull
    public Collection<Project> getAllByNameSorted(@Nullable final Session session, @Nullable final String name, @Nullable final ComparatorType comparatorType) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || name == null) return Collections.emptySet();
        if (comparatorType == null) return getAllByName(session, name);
        return ((PlannedEntityRepository<Project>)repository).findByNameAndSort(userId, comparatorType, name);
    }

    @Override
    public @NotNull Boolean deleteProjectTasks(@Nullable final Session session, @Nullable final String projectId) throws Exception {
        return deleteChildrenByParentId(session, projectId);
    }
}
