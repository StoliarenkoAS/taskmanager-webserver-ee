package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.repository.UserRepository;
import ru.stoliarenkoas.tm.webserver.api.service.ProjectService;
import ru.stoliarenkoas.tm.webserver.api.service.UserService;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.jdbc.UserRepositoryMySQL;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;
import ru.stoliarenkoas.tm.webserver.util.SessionUtil;

import java.util.Collection;
import java.util.Collections;

public class UserServiceImpl extends AbstractService<User> implements UserService {

    private final ProjectService projectService = new ProjectServiceImpl();

    public UserServiceImpl() {
        super(new UserRepositoryMySQL());
    }

    @NotNull
    private Boolean isValid(@Nullable final Session session, @Nullable final User user) throws Exception {
        if (session == null || user == null) return false;
        if (user.getLogin() == null || user.getLogin().isEmpty()) return false;
        if (!session.getUserId().equals(user.getId())) return false;
        return user.getPasswordHash() != null && !user.getPasswordHash().isEmpty();
    }

    @Override @NotNull
    public Boolean save(@Nullable final Session session, @Nullable final User user) throws Exception {
        if (!isValid(session, user)) return false;
        user.setPasswordHash(CypherUtil.getMd5(user.getPasswordHash())); //checked in validation method
        repository.merge(user.getId(), user);
        return true;
    }

    @NotNull
    public Boolean persist(@Nullable final Session session, @Nullable final User user) throws Exception {
        if (!isValid(session, user)) return false;
        repository.persist(user); //checked in validation method
        return true;
    }

    @Override @NotNull
    public Boolean deleteChildrenByParentId(@Nullable final Session session, @Nullable final String id) throws Exception {
        return projectService.deleteByIds(session, Collections.singleton(id));
    }

    @Override @NotNull
    public Boolean deleteChildrenByParentIds(@Nullable final Session session, @Nullable final Collection<String> ids) throws Exception {
        return projectService.deleteByIds(session, ids);
    }

    @Override @NotNull
    public Boolean register(@Nullable final String login, @Nullable final String password) throws Exception {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) return false;
        final User user = new User();
        user.setLogin(login);
        user.setPasswordHash(CypherUtil.getMd5(password));
        final Session session = SessionUtil.getSessionForUser(user);
        return persist(session, user);
    }

    @Override @Nullable
    public Session login(@Nullable final String login, @Nullable final String password) throws Exception {
        System.out.printf("[AUTH] Login: %s, Password: %s %n", login, password);
        if (login == null || login.isEmpty()) return null;
        if (password == null || password.isEmpty()) return null;
        final String passwordHash = CypherUtil.getMd5(password);
        final User user = ((UserRepository)repository).validate(login, passwordHash).orElse(null);
        if (user == null) return null;
        final Session session = SessionUtil.getSessionForUser(user);
        sessionService.open(session);
        return session;
    }

    @Override
    public @NotNull Boolean logout(@Nullable final Session session) throws Exception {
        if (session == null) return false;
        return sessionService.closeById(session.getId());
    }

    @Override @NotNull
    public String showUserProfile(@Nullable final Session session) throws Exception {
        final User currentUser = get(session, getCurrentUserId(session));
        if (currentUser == null) return "[YOU ARE NOT LOGGED IN]";
        return "USER PROFILE:" + "\n" +
                "User: " + currentUser.getLogin() + "\n" +
                "User status: " + currentUser.getRole().getDisplayName() + "\n";
    }

    @Override @NotNull
    public Boolean changePassword(@Nullable final Session session, @Nullable final String oldPassword, @Nullable final String newPassword) throws Exception {
        final User currentUser = get(session, getCurrentUserId(session));
        if (oldPassword == null || newPassword == null || newPassword.isEmpty()) return false;
        if (currentUser == null || CypherUtil.getMd5(oldPassword).equals(currentUser.getPasswordHash())) return false;
        currentUser.setPasswordHash(CypherUtil.getMd5(newPassword));
        repository.merge(currentUser.getId(), currentUser);
        return true;
    }
}
