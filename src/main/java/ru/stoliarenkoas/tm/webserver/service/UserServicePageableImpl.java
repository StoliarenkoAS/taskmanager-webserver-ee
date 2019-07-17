package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.stoliarenkoas.tm.webserver.api.service.pageable.UserServicePageable;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;
import ru.stoliarenkoas.tm.webserver.exception.IncorrectDataException;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;
import ru.stoliarenkoas.tm.webserver.model.entity.User;
import ru.stoliarenkoas.tm.webserver.repository.pageable.UserRepositoryPageable;
import ru.stoliarenkoas.tm.webserver.util.CypherUtil;

@Service
public class UserServicePageableImpl implements UserServicePageable {

    private UserRepositoryPageable repository;
    @Autowired
    public void setRepository(UserRepositoryPageable repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public @NotNull Page<UserDTO> findAll(
            @Nullable final String loggedUserId,
            @Nullable final PageRequest page)
            throws AccessForbiddenException {
        if (loggedUserId == null || page == null) return Page.empty();
        final User loggedUser = repository.findOne(loggedUserId).orElse(null);
        if (loggedUser == null || loggedUser.getRole() != UserDTO.Role.ADMIN) {
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
            if (loggedUser == null || loggedUser.getRole() != UserDTO.Role.ADMIN) {
                throw new AccessForbiddenException();
            }
        }
        return repository.findOne(requestedUserId).map(User::toDTO).orElse(null);
    }

    @Override
    @Modifying
    @Transactional(rollbackFor = AccessForbiddenException.class)
    public void persist(
            @Nullable final String loggedUserId,
            @Nullable final UserDTO persistableUser)
            throws AccessForbiddenException {
        if (loggedUserId == null || persistableUser == null) return;
        if (persistableUser.getRole() != UserDTO.Role.USER) {
            final User loggedUser = repository.findOne(loggedUserId).orElse(null);
            if (loggedUser == null || loggedUser.getRole() != UserDTO.Role.ADMIN) {
                throw new AccessForbiddenException();
            }
        }
        if (repository.findOne(persistableUser.getId()).isPresent()) {
            throw new AccessForbiddenException("user already exists");
        }
        repository.save(new User(persistableUser));
    }

    @Override
    @Modifying
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
    @Modifying
    @Transactional(rollbackFor = AccessForbiddenException.class)
    public void remove(
            @Nullable final String loggedUserId,
            @Nullable final String removableUserId)
            throws AccessForbiddenException {
        if (loggedUserId == null || removableUserId == null) return;
        final User loggedUser = repository.findOne(loggedUserId).orElse(null);
        if (loggedUser == null || loggedUser.getRole() != UserDTO.Role.ADMIN) {
            throw new AccessForbiddenException();
        }
        repository.deleteById(removableUserId);
    }

}
