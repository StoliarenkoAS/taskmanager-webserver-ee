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
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositorySpring;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;
import ru.stoliarenkoas.tm.webserver.util.SessionUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional
@Qualifier("spring")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositorySpring userRepository;

    private SessionService sessionService;

    private UserService userService;

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Nullable
    private String getCurrentUserId(@Nullable final SessionDTO session) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return null;
        if (!sessionService.isOpen(session.getId())) return null;
        return session.getUserId();
    }

    @Override
    public @Nullable SessionDTO login(@Nullable String login, @Nullable String password) throws Exception {
        if (login == null || login.isEmpty()) return null;
        if (password == null || password.isEmpty()) return null;

        final String passwordHash = CypherUtil.getMd5(password);
        final User user = userRepository.findByLoginAndPasswordHash(login, passwordHash);
        if (user == null) return null;
        final SessionDTO sessionDTO = SessionUtil.getSessionForUser(user.toDTO());
        sessionService.open(sessionDTO);
        System.out.println(sessionDTO);
        return sessionDTO;
    }

    @Override
    public @NotNull Boolean logout(@Nullable SessionDTO session) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return false;
        return sessionService.closeById(session.getId());
    }

    @Override
    public @NotNull Boolean register(@Nullable String login, @Nullable String password) throws Exception {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) return false;

        final boolean exists = !userRepository.findByLogin(login).isEmpty();
        if (exists) return false;
        final UserDTO userDTO = new UserDTO();
        userDTO.setLogin(login);
        userDTO.setPasswordHash(CypherUtil.getMd5(password));
        userRepository.save(new User(userDTO));
        return true;
    }

    @Override
    public @NotNull String showUserProfile(@Nullable SessionDTO sessionDTO) throws Exception {
        final UserDTO currentUser = get(sessionDTO, getCurrentUserId(sessionDTO));
        if (currentUser == null || sessionDTO == null || !SessionUtil.isValid(sessionDTO)) return "[YOU ARE NOT LOGGED IN]";
        return "USER PROFILE:" + "\n" +
                "UserDTO: " + currentUser.getLogin() + "\n" +
                "UserDTO status: " + currentUser.getRole().getDisplayName() + "\n";
    }

    @Override
    public @NotNull Boolean changePassword(@Nullable SessionDTO session, @Nullable String oldPassword, @Nullable String newPassword) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return false;
        final String userId = getCurrentUserId(session);
        if (userId == null) return false;
        final User user = userRepository.findAnyById(userId);
        if (oldPassword == null || newPassword == null || newPassword.isEmpty()) return false;
        if (user == null || !CypherUtil.getMd5(oldPassword).equals(user.getPasswordHash())) return false;
        user.setPasswordHash(CypherUtil.getMd5(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public @NotNull Collection<UserDTO> getAll(@Nullable SessionDTO session) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return Collections.emptyList();
        return ((Collection<User>)userRepository.findAll()).stream().map(User::toDTO).collect(Collectors.toList());
    }

    @Override
    public @NotNull Collection<UserDTO> getAllByName(@Nullable SessionDTO session, @Nullable String name) throws Exception {
        if (session == null || name == null || name.isEmpty() || !SessionUtil.isValid(session)) return Collections.emptyList();
        return userRepository.findByLogin(name).stream().map(User::toDTO).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public UserDTO get(@Nullable SessionDTO session, @Nullable String id) throws Exception {
        if (session == null || id == null ||
                id.isEmpty() || !SessionUtil.isValid(session)) return null;
        return userRepository.findAnyById(id).toDTO();
    }

    @Override
    public Boolean save(@Nullable SessionDTO session, @Nullable UserDTO userDTO) throws Exception {
        if (session == null || !SessionUtil.isValid(session) || userDTO == null) return false;
        return userRepository.save(new User(userDTO)) != null;
    }

    @Override
    public Boolean delete(@Nullable SessionDTO session, @Nullable String id) throws Exception {
        if (session == null || id == null ||
                id.isEmpty() || !SessionUtil.isValid(session)) return null;
        return userRepository.removeById(id) != null;
    }

    @Override
    public Boolean delete(@Nullable SessionDTO session, @Nullable UserDTO object) throws Exception {
        if (session == null || object == null || !SessionUtil.isValid(session)) return null;
        return userRepository.removeById(object.getId()) != null;
    }

    @Override
    public Boolean deleteByIds(@Nullable SessionDTO session, @Nullable Collection<String> ids) throws Exception {
        if (session == null || ids == null ||
                ids.isEmpty() || !SessionUtil.isValid(session)) return null;
        for (final String id : ids) {
            userRepository.removeById(id);
        }
        return true;
    }

    @Override
    public Boolean deleteByName(@Nullable SessionDTO session, @Nullable String name) throws Exception {
        if (session == null || name == null ||
                name.isEmpty() || !SessionUtil.isValid(session)) return null;
        return !userRepository.removeByLogin(name).isEmpty();
    }

    @Override
    public Boolean deleteAll(@Nullable SessionDTO session) throws Exception {
        if (session == null || !SessionUtil.isValid(session)) return null;
        userRepository.deleteAll();
        return true;
    }

}
