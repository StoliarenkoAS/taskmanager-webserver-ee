package ru.stoliarenkoas.tm.webserver.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.api.repository.SessionRepository;
import ru.stoliarenkoas.tm.webserver.api.service.SessionService;
import ru.stoliarenkoas.tm.webserver.repository.jdbc.SessionRepositoryMySQL;

import java.util.Collection;
import java.util.Collections;

public class SessionServiceImpl implements SessionService {
    
    private final SessionRepository repository = new SessionRepositoryMySQL();

    @Override @NotNull
    public Collection<Session> getAll() throws Exception {
        return repository.findAll();
    }

    @Override @NotNull
    public Collection<Session> getByUserId(@Nullable final String userId) throws Exception {
        if (userId == null || userId.isEmpty()) return Collections.emptySet();
        return repository.findByUserId(userId);
    }

    @Override @Nullable
    public Session getById(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) return null;
        return repository.findById(id);
    }

    @Override @NotNull
    public Boolean isOpen(@Nullable String id) throws Exception {
        if (id == null || id.isEmpty()) return false;
        return repository.containsId(id);
    }

    @Override @NotNull
    public Boolean open(@Nullable final Session session) throws Exception {
        if (session == null) return false;
        return repository.persist(session);
    }

    @Override @NotNull
    public Boolean closeById(@Nullable final String id) throws Exception {
        if (id == null || id.isEmpty()) return false;
        return repository.deleteById(id);
    }

    @Override @NotNull
    public Boolean closeByUserId(@Nullable final String userId) throws Exception {
        if (userId == null || userId.isEmpty()) return false;
        return repository.deleteByUserId(userId);
    }

    @Override @NotNull
    public Boolean closeAll() throws Exception {
        return repository.deleteAll();
    }
}
