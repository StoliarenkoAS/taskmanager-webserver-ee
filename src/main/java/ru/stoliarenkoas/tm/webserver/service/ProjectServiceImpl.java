package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectService;
import ru.stoliarenkoas.tm.webserver.api.service.SessionService;
import ru.stoliarenkoas.tm.webserver.api.service.TaskService;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.Project;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.ProjectRepositorySpring;
import ru.stoliarenkoas.tm.webserver.util.SessionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional
@Qualifier("spring")
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepositorySpring repository;

    private SessionService sessionService;

    private UserService userService;

    private TaskService taskService;

    @Autowired
    public void setRepository(ProjectRepositorySpring repository) {
        this.repository = repository;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Nullable
    private String getCurrentUserId(@Nullable final SessionDTO session) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return null;
        if (!sessionService.isOpen(session.getId())) return null;
        return session.getUserId();
    }

    @Override @NotNull
    public Collection<ProjectDTO> getAll(@Nullable SessionDTO session) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null) return Collections.emptyList();
        return repository.findByUser_Id(userId).stream().map(Project::toDTO).collect(Collectors.toList());
    }

    @Override @NotNull
    public Collection<ProjectDTO> getAllByName(@Nullable SessionDTO session, @Nullable String name) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || name == null || userId.isEmpty() || name.isEmpty()) return Collections.emptyList();
        return repository.findByUser_IdAndName(userId, name).stream().map(Project::toDTO)
                .collect(Collectors.toList());
    }

    @Override @Nullable
    public ProjectDTO get(@Nullable SessionDTO session, @Nullable String id) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || id == null || userId.isEmpty() || id.isEmpty()) return null;
        return repository.findAnyByUser_IdAndId(userId, id).toDTO();
    }

    @Override @NotNull
    public Collection<ProjectDTO> search(@Nullable SessionDTO session, @Nullable String searchLine) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || searchLine == null || userId.isEmpty() || searchLine.isEmpty()) return Collections.emptyList();
        searchLine = "%" + searchLine + "%";
        return repository.search(userId, searchLine).stream().map(Project::toDTO).collect(Collectors.toList());
    }

    @Override @NotNull
    public Boolean save(@Nullable SessionDTO session, @Nullable ProjectDTO projectDTO) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || projectDTO == null || userId.isEmpty()) return false;
        if (!userId.equals(projectDTO.getUserId())) return false;
        repository.save(new Project(projectDTO, new User(userService.get(session, userId))));
        return true;
    }

    @Override @NotNull
    public Boolean delete(@Nullable SessionDTO session, @Nullable String id) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || id == null || userId.isEmpty() || id.isEmpty()) return false;
        repository.deleteByUser_IdAndId(userId, id);
        return true;
    }

    @Override @NotNull
    public Boolean delete(@Nullable SessionDTO session, @Nullable ProjectDTO projectDTO) throws Exception {
        if (projectDTO == null) return false;
        return delete(session, projectDTO.getId());
    }

    @Override @NotNull
    public Boolean deleteByIds(@Nullable SessionDTO session, @Nullable Collection<String> ids) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || ids == null || userId.isEmpty() || ids.isEmpty()) return false;
        for (final String id : ids) {
            repository.deleteByUser_IdAndId(userId, id);
        }
        return true;
    }

    @Override @NotNull
    public Boolean deleteByName(@Nullable SessionDTO session, @Nullable String name) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || name == null || userId.isEmpty() || name.isEmpty()) return false;
        return repository.deleteByUser_IdAndName(userId, name) > 0;
    }

    @Override @NotNull
    public Boolean deleteAll(@Nullable SessionDTO session) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || userId.isEmpty()) return false;
        return repository.deleteByUser_Id(userId) > 0;
    }

    @Override @NotNull
    public Boolean deleteProjectTasks(@Nullable SessionDTO session, @Nullable String projectId) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || projectId == null || userId.isEmpty() || projectId.isEmpty()) return false;
        return !taskService.removeTasksByProjectIds(session, Collections.singletonList(projectId)).isEmpty();
    }
    
}
