package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;
import ru.stoliarenkoas.tm.webserver.api.service.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.enumerate.Role;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class UserServicePageableImpl implements UserServicePageable {

    private UserRepositoryPageable repository;
    @Autowired
    public void setRepository(UserRepositoryPageable repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @NotNull List<UserDTO> findAll(
            @Nullable final String loggedUserId) throws AccessForbiddenException {
        if (loggedUserId == null) throw new AccessForbiddenException("no logged user provided");
        final User loggedUser = repository.findOne(loggedUserId).orElse(null);
        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            throw new AccessForbiddenException();
        }
        final List<User> users = repository.findAll();
        return users.stream().map(User::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @NotNull Page<UserDTO> findAll(
            @Nullable final String loggedUserId,
            @Nullable final PageRequest page)
            throws AccessForbiddenException {
        if (loggedUserId == null || page == null) return Page.empty();
        final User loggedUser = repository.findOne(loggedUserId).orElse(null);
        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            throw new AccessForbiddenException();
        }
        final Page<User> users = repository.findAll(page);
        return users.map(User::toDTO);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @Nullable UserDTO findOne(
            @Nullable final String loggedUserId,
            @Nullable final String requestedUserId)
            throws AccessForbiddenException {
        if (loggedUserId == null || requestedUserId == null || requestedUserId.isEmpty()) return null;
        if (!loggedUserId.equals(requestedUserId)) {
            final User loggedUser = repository.findOne(loggedUserId).orElse(null);
            if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
                throw new AccessForbiddenException();
            }
        }
        return repository.findOne(requestedUserId).map(User::toDTO).orElse(null);
    }

    @NotNull
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserDTO login(@Nullable final String login, @Nullable final String password)
            throws IncorrectDataException {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            throw new IncorrectDataException("invalid input data");
        };
        final Optional<User> user = repository.login(login, CypherUtil.getMd5(password));
        if (!user.isPresent()) throw new IncorrectDataException("invalid login or password");
        return user.map(User::toDTO).get();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean exists(@Nullable String userId) {
        if (userId == null || userId.isEmpty()) return false;
        return repository.existsById(userId);
    }

    @Override
    @Transactional(rollbackFor = AccessForbiddenException.class)
    public void persist(
            @Nullable final String loggedUserId,
            @Nullable final UserDTO persistableUser)
            throws AccessForbiddenException, IncorrectDataException {
        if (loggedUserId == null) throw new AccessForbiddenException("not logged in");
        if (persistableUser == null) throw new IncorrectDataException("null user");
        if (persistableUser.getLogin() == null || persistableUser.getLogin().isEmpty()) {
            throw new IncorrectDataException("empty login");
        }
        if (persistableUser.getRole() != Role.USER) {
            final User loggedUser = repository.findOne(loggedUserId).orElse(null);
            if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
                throw new AccessForbiddenException();
            }
        }
        if (repository.findOne(persistableUser.getId()).isPresent()) {
            throw new AccessForbiddenException("user already exists");
        }
        try {
            repository.save(new User(persistableUser));
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectDataException(e.getMessage());
        }
    }

    @Override
    public void merge(
            @Nullable final String loggedUserId,
            @Nullable final UserDTO mergableUser)
            throws AccessForbiddenException, IncorrectDataException {
        if (loggedUserId == null) throw new AccessForbiddenException("not logged in");
        if (mergableUser == null) throw new IncorrectDataException("null user");
        if (!loggedUserId.equals(mergableUser.getId())) {
            final User loggedUser = repository.findOne(loggedUserId).orElse(null);
            if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
                throw new AccessForbiddenException();
            }
        }
        final Optional<User> userOptional = repository.findOne(mergableUser.getId());
        final User user = userOptional.orElseThrow(()->new IncorrectDataException("no such user"));
        user.setLogin(mergableUser.getLogin());
        if (mergableUser.getPasswordHash() != null) {
            user.setPasswordHash(CypherUtil.getMd5(mergableUser.getPasswordHash()));
        }
        user.setRole(mergableUser.getRole());
        repository.save(user);
    }

    @Override
    @Transactional(rollbackFor = IncorrectDataException.class)
    public void register(
            @Nullable final String login,
            @Nullable final String password)
            throws IncorrectDataException {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            throw new IncorrectDataException("empty field");
        }
        if (repository.findByLogin(login).size() > 0) {
            throw new IncorrectDataException("login is already taken");
        }
        final User user = new User();
        user.setLogin(login);
        user.setPasswordHash(CypherUtil.getMd5(password));
        repository.save(user);
    }

    @Override
    @Transactional(rollbackFor = AccessForbiddenException.class)
    public void remove(
            @Nullable final String loggedUserId,
            @Nullable final String removableUserId)
            throws AccessForbiddenException {
        if (loggedUserId == null || removableUserId == null) return;
        final User loggedUser = repository.findOne(loggedUserId).orElse(null);
        if (loggedUser == null || loggedUser.getRole() != Role.ADMIN) {
            throw new AccessForbiddenException();
        }
        repository.deleteById(removableUserId);
    }

}
