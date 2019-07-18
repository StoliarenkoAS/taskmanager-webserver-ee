package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectServicePageable;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.Project;
import ru.stoliarenkoas.tm.webserver.repository.ProjectRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class ProjectServicePageableImpl implements ProjectServicePageable {

    private ProjectRepositoryPageable repository;
    @Autowired
    public void setRepository(ProjectRepositoryPageable repository) {
        this.repository = repository;
    }

    private UserServicePageable userService;
    @Autowired
    public void setUserService(UserServicePageable userService) {
        this.userService = userService;
    }

    private UserRepositoryPageable userRepository;
    @Autowired
    public void setUserRepository(UserRepositoryPageable userRepository) {
        this.userRepository = userRepository;
    }

    @NotNull
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<ProjectDTO> findAllByUserId(@Nullable final String loggedUserId)
            throws AccessForbiddenException {
        checkAuthorization(loggedUserId);
        return repository.findAllByUser_Id(loggedUserId)
                .stream().map(Project::toDTO).collect(Collectors.toList());
    }

    @NotNull
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Page<ProjectDTO> findAllByUserId(
            @Nullable final String loggedUserId,
            @Nullable PageRequest page
            ) throws AccessForbiddenException {
        checkAuthorization(loggedUserId);
        if (page == null) page = PageRequest.of(0, 10);
        return repository.findAllByUser_Id(loggedUserId, page).map(Project::toDTO);
    }

    @Nullable
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ProjectDTO findOne(
            @Nullable final String loggedUserId,
            @Nullable final String requestedProjectId
            ) throws AccessForbiddenException, IncorrectDataException {
        checkAuthorization(loggedUserId);
        if (requestedProjectId == null || requestedProjectId.isEmpty()) {
            throw new IncorrectDataException("empty project request");
        }
        return repository.findOne(loggedUserId, requestedProjectId).map(Project::toDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean exists(@Nullable String loggedUserId, @Nullable String projectId) {
        if (loggedUserId == null || projectId == null || loggedUserId.isEmpty() || projectId.isEmpty()) {
            return false;
        }
        return repository.existsByUser_IdAndId(loggedUserId, projectId);
    }

    @Override
    @Transactional
    public void persist(
            @Nullable final String loggedUserId,
            @Nullable final ProjectDTO persistableProject
            ) throws AccessForbiddenException, IncorrectDataException {
        checkAuthorization(loggedUserId);
        checkProject(loggedUserId, persistableProject);
        if (repository.existsById(persistableProject.getId())) {
            throw new AccessForbiddenException("project already exists");
        }
        repository.save(new Project(persistableProject, userRepository.getOne(loggedUserId)));
    }

    @Override
    @Transactional
    public void merge(
            @Nullable final String loggedUserId,
            @Nullable final ProjectDTO persistableProject
            ) throws AccessForbiddenException, IncorrectDataException {
        checkAuthorization(loggedUserId);
        checkProject(loggedUserId, persistableProject);
        repository.save(new Project(persistableProject, userRepository.getOne(loggedUserId)));
    }

    @Override
    @Transactional
    public void remove(
            @Nullable final String loggedUserId,
            @Nullable final String removableProjectId
            ) throws AccessForbiddenException, IncorrectDataException {
        checkAuthorization(loggedUserId);
        if (removableProjectId == null || removableProjectId.isEmpty()) {
            throw new IncorrectDataException("no project");
        }
        final Optional<Project> project = repository.findOne(loggedUserId, removableProjectId);
        if (!project.isPresent()) throw new IncorrectDataException("project doesn't exist");
        repository.delete(project.get());
    }

    private void checkAuthorization(@Nullable final String loggedUserId) throws AccessForbiddenException {
        if (loggedUserId == null || loggedUserId.isEmpty()) {
            throw new AccessForbiddenException("not authorized");
        }
        if (!userService.exists(loggedUserId)) {
            throw new AccessForbiddenException("no such user");
        }
    }

    private void checkProject(@NotNull final String userId, @Nullable final ProjectDTO projectDTO)
            throws AccessForbiddenException, IncorrectDataException {
        if (projectDTO == null) throw new IncorrectDataException("null project");
        if (!userId.equals(projectDTO.getUserId())) throw new AccessForbiddenException("save by wrong user");
        if (projectDTO.getName() == null || projectDTO.getName().isEmpty()) {
            throw new IncorrectDataException("empty project name");
        }
    }

}
