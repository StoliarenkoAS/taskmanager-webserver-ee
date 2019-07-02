package ru.stoliarenkoas.tm.webserver.repository.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Task;
import ru.stoliarenkoas.tm.webserver.api.repository.TaskRepository;
import ru.stoliarenkoas.tm.webserver.util.DatabaseConnectionUtil;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class TaskRepositoryMySQL implements TaskRepository {

    private final Connection connection;

    public TaskRepositoryMySQL() {
        this.connection = DatabaseConnectionUtil.getJDBCConnection();
    }

    @NotNull
    private Task fetch(@NotNull final ResultSet resultSet) throws SQLException {
        final Task task = new Task();
        task.setId(resultSet.getString("id"));
        task.setProjectId(resultSet.getString("projectId"));
        task.setUserId(resultSet.getString("user_id"));
        task.setName(resultSet.getString("name"));
        task.setDescription(resultSet.getString("description"));
        task.setCreationDate(resultSet.getDate("creationDate"));
        task.setStartDate(resultSet.getDate("startDate"));
        task.setEndDate(resultSet.getDate("endDate"));
        task.setStatus(Status.valueOf(resultSet.getString("status")));
        return task;
    }

    @Override @NotNull
    public Collection<Task> findAll(@NotNull final String userId) throws SQLException {
        final Collection<Task> tasks = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE `user_id` = ?");
        statement.setString(1, userId);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            tasks.add(fetch(resultSet));
        }
        return tasks;
    }

    @Override @NotNull
    public Collection<Task> findAllAndSort(@NotNull final String userId, @NotNull final ComparatorType comparatorType) throws SQLException {
        final Collection<Task> tasks = new LinkedHashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE `user_id` = ? ORDER BY ?");
        statement.setString(1, userId);
        switch (comparatorType) {
            case BY_STATUS: {
                statement.setString(2, "status");
                break;
            }
            case BY_END_DATE: {
                statement.setString(2, "endDate");
                break;
            }
            case BY_START_DATE: {
                statement.setString(2, "startDate");
                break;
            }
            default: {
                statement.setString(2, "creationDate");
            }
        }
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            tasks.add(fetch(resultSet));
        }
        return tasks;
    }

    @Override @NotNull
    public Collection<Task> findByName(@NotNull final String userId, @NotNull final String name) throws SQLException {
        final Collection<Task> tasks = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE `user_id` = ? AND `name` = ?");
        statement.setString(1, userId);
        statement.setString(2, name);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            tasks.add(fetch(resultSet));
        }
        return tasks;
    }

    @Override @NotNull
    public Collection<Task> findByNameAndSort(@NotNull final String userId, @NotNull final ComparatorType comparatorType, @NotNull final String name) throws SQLException {
        final Collection<Task> tasks = new LinkedHashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE `user_id` = ? AND `name` = ? ORDER BY ?");
        statement.setString(1, userId);
        statement.setString(2, name);
        switch (comparatorType) {
            case BY_STATUS: {
                statement.setString(3, "status");
                break;
            }
            case BY_END_DATE: {
                statement.setString(3, "endDate");
                break;
            }
            case BY_START_DATE: {
                statement.setString(3, "startDate");
                break;
            }
            default: {
                statement.setString(3, "creationDate");
            }
        }
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            tasks.add(fetch(resultSet));
        }
        return tasks;
    }

    @Override @NotNull
    public Collection<Task> findByProjectId(@NotNull final String userId, @NotNull final String projectId) throws SQLException {
        final Collection<Task> tasks = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE `user_id` = ? AND `projectId` = ?");
        statement.setString(1, userId);
        statement.setString(2, projectId);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            tasks.add(fetch(resultSet));
        }
        return tasks;
    }

    @Override @NotNull
    public Collection<Task> search(@NotNull final String userId, @NotNull final String searchLine) throws SQLException {
        final Collection<Task> tasks = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE (`user_id` = ?) " +
                "AND (`name` LIKE CONCAT(\"%\", ?, \"%\") OR `description` LIKE LIKE CONCAT(\"%\", ?, \"%\"))");
        statement.setString(1, userId);
        statement.setString(2, searchLine);
        statement.setString(3, searchLine);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            tasks.add(fetch(resultSet));
        }
        return tasks;
    }

    @Override @Nullable
    public Task findOne(@NotNull final String userId, @NotNull final String id) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `task` WHERE `user_id` = ? AND `id` = ? LIMIT 1");
        statement.setString(1, userId);
        statement.setString(2, id);
        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) return fetch(resultSet);
        return null;
    }

    @Override @NotNull
    public Boolean persist(@NotNull final Task task) throws SQLException {
        final PreparedStatement checkIfExists = connection.prepareStatement("SELECT COUNT(*) AS `count` FROM `task` WHERE `id` = ? " +
                "OR (`projectId` = ? AND `name` = ?)");
        checkIfExists.setString(1, task.getId());
        checkIfExists.setString(2, task.getProjectId());
        checkIfExists.setString(3, task.getName());
        final ResultSet resultSet = checkIfExists.executeQuery();
        if (resultSet.next() && resultSet.getInt("count") > 0) return false;

        final PreparedStatement statement = connection.prepareStatement("INSERT INTO `task` " +
                "(`id`, `projectId`, `user_id`, `name`, `description`, `creationDate`, `startDate`, `endDate`, `status`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, task.getId());
        statement.setString(2, task.getProjectId());
        statement.setString(3, task.getUserId());
        statement.setString(4, task.getName());
        statement.setString(5, task.getDescription());
        statement.setTimestamp(6, new Timestamp(task.getCreationDate().getTime()), Calendar.getInstance());
        statement.setTimestamp(7, new Timestamp((Optional.ofNullable(task.getStartDate()).orElse(new Date())).getTime()), Calendar.getInstance());
        statement.setTimestamp(8, new Timestamp((Optional.ofNullable(task.getEndDate()).orElse(new Date())).getTime()), Calendar.getInstance());
        statement.setString(9, task.getStatus().toString());
        return statement.execute();
    }

    @Override @NotNull
    public Boolean merge(@NotNull final String userId, @NotNull final Task task) throws SQLException {
        final PreparedStatement checkIfExists = connection.prepareStatement("SELECT COUNT(*) AS `count` FROM `task` WHERE `id` = ? " +
                "OR (`projectId` = ? AND `name` = ?)");
        checkIfExists.setString(1, task.getId());
        checkIfExists.setString(2, task.getProjectId());
        checkIfExists.setString(3, task.getName());
        final ResultSet resultSet = checkIfExists.executeQuery();
        final boolean update = (resultSet.next() && resultSet.getInt(1) > 0);

        if (update) {
            final PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM `task` WHERE (`user_id` = ? AND `name` = ?) OR `id` = ?");
            deleteStatement.setString(1, task.getProjectId());
            deleteStatement.setString(2, task.getName());
            deleteStatement.setString(3, task.getId());
            deleteStatement.executeUpdate();
        }

        final PreparedStatement statement = connection.prepareStatement("INSERT INTO `task` " +
            "(`id`, `projectId`, `user_id`, `name`, `description`, `creationDate`, `startDate`, `endDate`, `status`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, task.getId());
        statement.setString(2, task.getProjectId());
        statement.setString(3, task.getUserId());
        statement.setString(4, task.getName());
        statement.setString(5, task.getDescription());
        statement.setTimestamp(6, new Timestamp(task.getCreationDate().getTime()), Calendar.getInstance());
        statement.setTimestamp(7, new Timestamp((Optional.ofNullable(task.getStartDate()).orElse(new Date())).getTime()), Calendar.getInstance());
        statement.setTimestamp(8, new Timestamp((Optional.ofNullable(task.getEndDate()).orElse(new Date())).getTime()), Calendar.getInstance());
        statement.setString(9, task.getStatus().toString());
        return statement.execute();
    }

    @Override @Nullable
    public String remove(@NotNull final String userId, @NotNull final String id) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `task` WHERE `user_id` = ? AND `id` = ?");
        preparedStatement.setString(1, userId);
        preparedStatement.setString(2, id);
        if (preparedStatement.executeUpdate() > 0) return id;
        return null;
    }

    @Override @Nullable
    public String remove(@NotNull final String userId, @NotNull final Task task) throws SQLException {
        return remove(userId, task.getId());
    }

    @Override @NotNull
    public Collection<String> removeByName(@NotNull final String userId, @NotNull final String name) throws SQLException {
        final Collection<String> ids = new HashSet<>();
        final PreparedStatement getIdsStatement = connection.prepareStatement("SELECT `id` FROM `task` WHERE `user_id` = ? AND `name` = ?");
        final PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM `task` WHERE `user_id` = ? AND `name` = ?");
        getIdsStatement.setString(1, userId);
        getIdsStatement.setString(2, name);
        removeStatement.setString(1, userId);
        removeStatement.setString(2, name);
        final ResultSet resultSet = getIdsStatement.executeQuery();
        while (resultSet.next()) {
            ids.add(resultSet.getString("id"));
        }
        removeStatement.executeUpdate();
        return ids;
    }

    @Override @NotNull
    public Collection<String> removeByProjectIds(@NotNull final String userId, @NotNull final Collection<String> ids) throws SQLException {
        final Collection<String> removedIds = new HashSet<>();
        final PreparedStatement getIdStatement = connection.prepareStatement("SELECT `id` FROM `task` WHERE `projectId` = ?");
        for (final String id : ids) {
            getIdStatement.setString(1, id);
            final ResultSet resultSet = getIdStatement.executeQuery();
            while (resultSet.next()) {
                removedIds.add(resultSet.getString("id"));
            }
        }
        final PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM `task` WHERE `id` = ?");
        for (final String id : removedIds) {
            deleteStatement.setString(1, id);
        }
        return removedIds;
    }

    @Override @NotNull
    public Collection<String> removeAll(@NotNull final String userId) throws SQLException {
        final Collection<String> ids = new HashSet<>();
        final PreparedStatement getIdsStatement = connection.prepareStatement("SELECT `id` FROM `task` WHERE `user_id` = ?");
        final PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM `task` WHERE `user_id` = ?");
        getIdsStatement.setString(1, userId);
        removeStatement.setString(1, userId);
        final ResultSet resultSet = getIdsStatement.executeQuery();
        while (resultSet.next()) {
            ids.add(resultSet.getString("id"));
        }
        removeStatement.executeUpdate();
        return ids;
    }
    
}
