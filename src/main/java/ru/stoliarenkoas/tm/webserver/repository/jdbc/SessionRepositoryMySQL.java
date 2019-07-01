package ru.stoliarenkoas.tm.webserver.repository.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.api.repository.SessionRepository;
import ru.stoliarenkoas.tm.webserver.util.DatabaseConnectionUtil;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class SessionRepositoryMySQL implements SessionRepository {

    final Connection connection;

    public SessionRepositoryMySQL() {
        this.connection = DatabaseConnectionUtil.getJDBCConnection();
    }

    private Session fetch(@NotNull final ResultSet resultSet) throws SQLException {
        final Session session = new Session();
        session.setId(resultSet.getString("id"));
        session.setUserId(resultSet.getString("userId"));
        session.setUserLogin(resultSet.getString("userLogin"));
        session.setHash(resultSet.getString("hash"));
        session.setSortMethod(ComparatorType.valueOf(resultSet.getString("sortMethod")));
        session.setCreationDate(resultSet.getDate("creationDate"));
        return session;
    }

    @Override @NotNull
    public Collection<Session> findAll() throws SQLException {
        final Collection<Session> sessions = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `session`");
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            sessions.add(fetch(resultSet));
        }
        return sessions;
    }

    @Override @NotNull
    public Collection<Session> findByUserId(@NotNull final String userId) throws SQLException {
        final Collection<Session> sessions = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `session` WHERE `userId` = ?");
        statement.setString(1, userId);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            sessions.add(fetch(resultSet));
        }
        return sessions;
    }

    @Override @Nullable
    public Session findById(@NotNull final String id) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `session` WHERE `id` = ? LIMIT 1");
        statement.setString(1, id);
        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return fetch(resultSet);
        }
        return null;
    }

    @Override @NotNull
    public Boolean containsId(@NotNull final String id) throws SQLException {
        final Collection<Session> sessions = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS `count` FROM `session` WHERE `id` = ?");
        statement.setString(1, id);
        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("count") > 0;
        }
        return false;
    }

    @Override @NotNull
    public Boolean persist(@NotNull final Session session) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("INSERT IGNORE INTO `session` " +
                "(`id`, `userId`, `userLogin`, `hash`, `sortMethod`, `creationDate`) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, session.getId());
        statement.setString(2, session.getUserId());
        statement.setString(3, session.getUserLogin());
        statement.setString(4, session.getHash());
        statement.setString(5, session.getSortMethod().toString());
        statement.setTimestamp(6, new Timestamp(session.getCreationDate().getTime()), Calendar.getInstance());
        return statement.execute();
    }

    @Override @NotNull
    public Boolean deleteById(@NotNull final String id) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("DELETE FROM `session` WHERE `id` = ?");
        statement.setString(1, id);
        return statement.executeUpdate() > 0;
    }

    @Override @NotNull
    public Boolean deleteByUserId(@NotNull final String userId) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("DELETE FROM `session` WHERE `userId` = ?");
        statement.setString(1, userId);
        return statement.executeUpdate() > 0;
    }

    @Override @NotNull
    public Boolean deleteAll() throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("DELETE FROM `session`");
        return statement.executeUpdate() > 0;
    }
    
}
