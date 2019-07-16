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
import ru.stoliarenkoas.tm.webserver.model.dto.TaskDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.Project;
import ru.stoliarenkoas.tm.webserver.model.entity.Task;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.TaskRepositorySpring;
import ru.stoliarenkoas.tm.webserver.util.SessionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Qualifier("spring")
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepositorySpring repository;

    private SessionService sessionService;

    private UserService userService;

    private ProjectService projectService;

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Nullable
    private String getCurrentUserId(@Nullable final SessionDTO session) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return null;
        if (!sessionService.isOpen(session.getId())) return null;
        return session.getUserId();
    }

    @Override @NotNull
    public Collection<TaskDTO> getAll(@Nullable SessionDTO session) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null) return Collections.emptyList();
        return repository.findByProject_User_Id(userId).stream().map(Task::toDTO).collect(Collectors.toList());
    }

    @Override @NotNull
    public Collection<TaskDTO> getAllByName(@Nullable SessionDTO session, @Nullable String name) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || name == null || userId.isEmpty() || name.isEmpty()) return Collections.emptyList();
        return repository.findByProject_User_IdAndName(userId, name)
                .stream().map(Task::toDTO).collect(Collectors.toList());
    }

    @Override @NotNull
    public Collection<TaskDTO> getTasksByProjectId(@Nullable SessionDTO session, @Nullable String projectId) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || projectId == null || userId.isEmpty() || projectId.isEmpty()) return Collections.emptyList();
        return repository.findByProject_User_IdAndProject_Id(userId, projectId)
                .stream().map(Task::toDTO).collect(Collectors.toList());
    }

    @Override @Nullable
    public TaskDTO get(@Nullable SessionDTO session, @Nullable String id) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || id == null || userId.isEmpty() || id.isEmpty()) return null;
        return repository.findAnyByProject_User_IdAndId(userId, id).toDTO();
    }

    @Override @NotNull
    public Collection<TaskDTO> search(@Nullable SessionDTO session, @Nullable String searchLine) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || searchLine == null || userId.isEmpty() || searchLine.isEmpty()) return Collections.emptyList();
        searchLine = "%" + searchLine + "%";
        return repository.search(userId, searchLine).stream().map(Task::toDTO).collect(Collectors.toList());
    }

    @Override @NotNull
    public Boolean save(@Nullable SessionDTO session, @Nullable TaskDTO taskDTO) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || taskDTO == null || userId.isEmpty()) return false;
        if (!userId.equals(taskDTO.getUserId())) return false;
        final UserDTO userDTO = userService.get(session, session.getUserId());
        final ProjectDTO projectDTO = projectService.get(session, taskDTO.getProjectId());
        if (userDTO == null || projectDTO == null) return false;
        final User user = new User(userDTO);
        final Project project = new Project(projectDTO, user);
        repository.save(new Task(taskDTO, project));
        return true;
    }

    @Override @NotNull
    public Boolean delete(@Nullable SessionDTO session, @Nullable String id) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || id == null || userId.isEmpty() || id.isEmpty()) return false;
        repository.deleteByProject_User_IdAndId(userId, id);
        return true;
    }

    @Override @NotNull
    public Boolean delete(@Nullable SessionDTO session, @Nullable TaskDTO object) throws Exception {
        if (object == null) return false;
        return delete(session, object.getId());
    }

    @Override @NotNull
    public Boolean deleteByIds(@Nullable SessionDTO session, @Nullable Collection<String> ids) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || ids == null || userId.isEmpty() || ids.isEmpty()) return false;
        for (final String id : ids) {
            repository.deleteByProject_User_IdAndId(userId, id);
        }
        return true;
    }

    @Override @NotNull
    public Boolean deleteByName(@Nullable SessionDTO session, @Nullable String name) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || name == null || userId.isEmpty() || name.isEmpty()) return false;
        return repository.deleteByProject_User_IdAndName(userId, name) > 0;
    }

    @Override @NotNull
    public Boolean deleteAll(@Nullable SessionDTO session) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || userId.isEmpty()) return false;
        return repository.deleteByProject_User_Id(userId) > 0;
    }

    @Override @NotNull
    public Collection<String> removeTasksByProjectIds(@Nullable SessionDTO session, @Nullable Collection<String> ids) throws Exception {
        final String userId = getCurrentUserId(session);
        if (userId == null || ids == null || userId.isEmpty() || ids.isEmpty()) return Collections.emptyList();
        final Set<String> taskIds = new HashSet<>();
        for (final String id : ids) {
            taskIds.addAll(getTasksByProjectId(session, id).stream().map(TaskDTO::getId).collect(Collectors.toList()));
        }
        System.out.println("tasks to delete: " + taskIds);
        deleteByIds(session, taskIds);
        return taskIds;
    }
    
}
