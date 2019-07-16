package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoliarenkoas.tm.webserver.api.service.SessionService;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.Session;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.SessionRepositorySpring;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Qualifier("spring")
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepositorySpring repository;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override @NotNull
    public Collection<SessionDTO> getAll() throws Exception {
        return ((Collection<Session>) repository.findAll()).stream().map(Session::toDTO).collect(Collectors.toList());
    }

    @Override @NotNull
    public Collection<SessionDTO> getByUserId(@Nullable String userId) throws Exception {
        if (userId == null || userId.isEmpty()) return Collections.emptyList();
        return repository.findByUser_Id(userId).stream().map(Session::toDTO).collect(Collectors.toList());
    }

    @Override @Nullable
    public SessionDTO getById(@Nullable String id) throws Exception {
        if (id == null || id.isEmpty()) return null;
        final Optional<Session> session = repository.findById(id);
        return session.map(Session::toDTO).orElse(null);
    }

    @Override @NotNull
    public Boolean isOpen(@Nullable String id) throws Exception {
        return getById(id) != null;
    }

    @Override @NotNull
    public Boolean open(@Nullable SessionDTO sessionDTO) throws Exception {
        if (sessionDTO == null) return false;
        final UserDTO userDTO = userService.get(sessionDTO, sessionDTO.getUserId());
        if (userDTO == null) return false;
        repository.save(new Session(sessionDTO, new User(userDTO)));
        return true;
    }

    @Override @NotNull
    public Boolean closeById(@Nullable String id) throws Exception {
        if (id == null || id.isEmpty()) return false;
        repository.deleteById(id);
        return true;
    }

    @Override @NotNull
    public Boolean closeByUserId(@Nullable String userId) throws Exception {
        if (userId == null || userId.isEmpty()) return false;
        return repository.deleteByUser_Id(userId) > 0;
    }

    @Override @NotNull
    public Boolean closeAll() throws Exception {
        repository.deleteAll();
        return true;
    }
}
