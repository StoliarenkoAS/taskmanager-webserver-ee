package ru.stoliarenkoas.tm.webserver.repository.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.User;
import ru.stoliarenkoas.tm.webserver.api.repository.UserRepository;
import ru.stoliarenkoas.tm.webserver.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;

    public UserRepositoryMySQL() {
        this.connection = DatabaseConnectionUtil.getJDBCConnection();
    }

    private boolean isCorrupted(@Nullable final User user) {
        return user == null ||
               user.getId() == null ||
               user.getLogin() == null ||
               user.getRole() == null;
    }

    private User fetch(@NotNull final ResultSet resultSet) throws SQLException {
        final User user = new User();
        user.setId(resultSet.getString("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPasswordHash(resultSet.getString("pwdHash"));
        user.setRole(User.Role.valueOf(resultSet.getString("role")));
        if (isCorrupted(user)) return null;
        return user;
    }

    private boolean isAdmin(@NotNull final String userId) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT role FROM `user` WHERE `id` = ? LIMIT 1");
        statement.setString(1, userId);
        final ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) return false;
        return User.Role.ADMIN.toString().equals(resultSet.getString(1));
    }

    @Override @NotNull
    public Optional<User> validate(@NotNull final String login, @NotNull final String pwdHash) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE `login` = ? AND `pwdHash` = ?");
        statement.setString(1, login);
        statement.setString(2, pwdHash);
        final ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) return Optional.empty();
        return Optional.ofNullable(fetch(resultSet));
    }

    @Override @NotNull
    public Collection<User> findAll(@NotNull final String userId) throws SQLException {
        if (!isAdmin(userId)) return Collections.emptySet();
        final Collection<User> users = new LinkedHashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user`");
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            final User user = fetch(resultSet);
            if (user != null) users.add(user);
        }
        return users;
    }

    @Override @NotNull
    public Collection<User> findByName(@NotNull final String userId, @NotNull final String name) throws SQLException {
        if (!isAdmin(userId)) return Collections.emptySet();
        final Collection<User> users = new LinkedHashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE `login` = ?");
        statement.setString(1, name);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            final User user = fetch(resultSet);
            if (user != null) users.add(user);
        }
        return users;
    }

    @Override @Nullable
    public User findOne(@NotNull final String userId, @NotNull final String id) throws SQLException {
        if (!userId.equals(id) && !isAdmin(userId)) return null;
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE `id` = ?");
        statement.setString(1, id);
        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return fetch(resultSet);
        }
        return null;
    }

    @Override @NotNull
    public Boolean persist(@NotNull final User user) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("INSERT INTO `user` (`id`, `login`, `pwdHash`, `role`) VALUES (?, ?, ?, ?)");
        statement.setString(1, user.getId());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPasswordHash());
        statement.setString(4, user.getRole().toString());
        return statement.execute();
    }

    @Override @NotNull
    public Boolean merge(@NotNull final String userId, @NotNull final User user) throws SQLException {
        if (!isAdmin(userId)) return false;
        final PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM `user` WHERE `id` = ?");
        deleteStatement.setString(1, user.getId());
        deleteStatement.executeUpdate();

        final PreparedStatement statement = connection.prepareStatement("INSERT INTO `user` (`id`, `login`, `pwdHash`, `role`) VALUES (?, ?, ?, ?)");
        statement.setString(1, user.getId());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPasswordHash());
        statement.setString(4, user.getRole().toString());
        return statement.execute();
    }

    @Override @Nullable
    public String remove(@NotNull final String userId, @NotNull final String id) throws SQLException {
        if (!isAdmin(userId)) return null;
        final PreparedStatement statement = connection.prepareStatement("DELETE FROM `user` WHERE `id` = ?");
        statement.setString(1, id);
        final boolean deleted = statement.executeUpdate() > 0;
        if (deleted) return id;
        return null;
    }

    @Override @Nullable
    public String remove(@NotNull final String userId, @NotNull final User user) throws SQLException {
        if (!isAdmin(userId)) return null;
        final PreparedStatement statement = connection.prepareStatement("DELETE FROM `user` WHERE `id` = ?");
        statement.setString(1, user.getId());
        final boolean deleted = statement.executeUpdate() > 0;
        if (deleted) return user.getId();
        return null;
    }

    @Override @NotNull
    public Collection<String> removeByName(@NotNull final String userId, @NotNull final String name) throws SQLException {
        if (!isAdmin(userId)) return Collections.emptySet();
        final Collection<String> ids = new HashSet<>();
        final PreparedStatement findStatement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `login` = ?");
        final PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM `user` WHERE `id` = ?");
        findStatement.setString(1, name);
        final ResultSet resultSet = findStatement.executeQuery();
        while (resultSet.next()) {
            final String id = resultSet.getString("id");
            if (id == null) continue;
            deleteStatement.setString(1, id);
            final boolean deleted = deleteStatement.executeUpdate() > 0;
            if (deleted) ids.add(id);
        }
        return ids;
    }

    @Override @NotNull
    public Collection<String> removeAll(@NotNull final String userId) throws SQLException {
        if (!isAdmin(userId)) return Collections.emptySet();
        final Collection<String> ids = new HashSet<>();
        final PreparedStatement findStatement = connection.prepareStatement("SELECT `id` FROM `user`");
        final PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM `user`");
        final ResultSet resultSet = findStatement.executeQuery();
        while (resultSet.next()) {
            final String id = resultSet.getString("id");
            if (id != null) ids.add(id);
        }
        deleteStatement.execute();
        return ids;
    }
}
