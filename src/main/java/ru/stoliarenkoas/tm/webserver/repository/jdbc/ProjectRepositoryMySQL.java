package ru.stoliarenkoas.tm.webserver.repository.jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.repository.ProjectRepository;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.entity.Project;
import ru.stoliarenkoas.tm.webserver.util.DatabaseConnectionUtil;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class ProjectRepositoryMySQL implements ProjectRepository {

    final Connection connection;

    public ProjectRepositoryMySQL() {
        this.connection = DatabaseConnectionUtil.getJDBCConnection();
    }

    @NotNull
    private Project fetch(@NotNull final ResultSet resultSet) throws SQLException {
        final Project project = new Project();
        project.setId(resultSet.getString("id"));
        project.setUserId(resultSet.getString("userId"));
        project.setName(resultSet.getString("name"));
        project.setDescription(resultSet.getString("description"));
        project.setCreationDate(resultSet.getDate("creationDate"));
        project.setStartDate(resultSet.getDate("startDate"));
        project.setEndDate(resultSet.getDate("endDate"));
        project.setStatus(Status.valueOf(resultSet.getString("status")));
        return project;
    }

    @Override @NotNull
    public Collection<Project> findAll(@NotNull final String userId) throws SQLException {
        final Collection<Project> projects = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE `userId` = ?");
        statement.setString(1, userId);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            projects.add(fetch(resultSet));
        }
        return projects;
    }

    @Override @NotNull
    public Collection<Project> findAllAndSort(@NotNull final String userId, @NotNull final ComparatorType comparatorType) throws SQLException {
        final Collection<Project> projects = new LinkedHashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE `userId` = ? ORDER BY ?");
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
            projects.add(fetch(resultSet));
        }
        return projects;
    }

    @Override @NotNull
    public Collection<Project> findByName(@NotNull final String userId, @NotNull final String name) throws SQLException {
        final Collection<Project> projects = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE `userId` = ? AND `name` = ?");
        statement.setString(1, userId);
        statement.setString(2, name);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            projects.add(fetch(resultSet));
        }
        return projects;
    }

    @Override @NotNull
    public Collection<Project> findByNameAndSort(@NotNull final String userId, @NotNull final ComparatorType comparatorType, @NotNull final String name) throws SQLException {
        final Collection<Project> projects = new LinkedHashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE `userId` = ? AND `name` = ? ORDER BY ?");
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
            projects.add(fetch(resultSet));
        }
        return projects;
    }

    @Override @NotNull
    public Collection<Project> search(@NotNull final String userId, @NotNull final String searchLine) throws SQLException {
        final Collection<Project> projects = new HashSet<>();
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE (`userId` = ?) " +
                "AND (`name` LIKE CONCAT(\"%\", ?, \"%\") OR `description` LIKE CONCAT(\"%\", ?, \"%\"))");
        statement.setString(1, userId);
        statement.setString(2, searchLine);
        statement.setString(3, searchLine);
        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            projects.add(fetch(resultSet));
        }
        return projects;
    }

    @Override @Nullable
    public Project findOne(@NotNull final String userId, @NotNull final String id) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement("SELECT * FROM `project` WHERE `userId` = ? AND `id` = ? LIMIT 1");
        statement.setString(1, userId);
        statement.setString(2, id);
        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return fetch(resultSet);
        }
        return null;
    }

    @Override @NotNull
    public Boolean persist(@NotNull final Project project) throws SQLException {
        final PreparedStatement checkIfExists = connection.prepareStatement("SEARCH COUNT(*) AS `count` FROM `project` WHERE `id` = ? " +
                "OR (`userId` = ? AND `name` = ?)");
        checkIfExists.setString(1, project.getId());
        checkIfExists.setString(2, project.getUserId());
        checkIfExists.setString(3, project.getName());
        final ResultSet resultSet = checkIfExists.executeQuery();
        if (resultSet.next() && resultSet.getInt("count") > 0) return false;

        final PreparedStatement persistStatement = connection.prepareStatement("INSERT INTO `project` " +
                "(`id`, `userId`, `name`, `description`, `creationDate`, `startDate`, `endDate`, `status`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        persistStatement.setString(1, project.getId());
        persistStatement.setString(2, project.getUserId());
        persistStatement.setString(3, project.getName());
        persistStatement.setString(4, project.getDescription());
        persistStatement.setTimestamp(5, new Timestamp(project.getCreationDate().getTime()), Calendar.getInstance());
        persistStatement.setTimestamp(6, new Timestamp(Optional.ofNullable(project.getStartDate()).orElse(new Date()).getTime()), Calendar.getInstance());
        persistStatement.setTimestamp(7, new Timestamp(Optional.ofNullable(project.getEndDate()).orElse(new Date()).getTime()), Calendar.getInstance());
        persistStatement.setString(8, project.getStatus().toString());
        return persistStatement.executeUpdate() > 0;
    }

    @Override @NotNull
    public Boolean merge(@NotNull final String userId, @NotNull final Project project) throws SQLException {
        final PreparedStatement checkIfExists = connection.prepareStatement("SELECT COUNT(*) AS `count` FROM `project` WHERE `id` = ?" +
                "OR (`userId` = ? AND `name` = ?)");
        checkIfExists.setString(1, project.getId());
        checkIfExists.setString(2, project.getUserId());
        checkIfExists.setString(3, project.getName());
        final ResultSet resultSet = checkIfExists.executeQuery();
        final boolean update = resultSet.next() && resultSet.getInt("count") > 0;

        if (update) {
            final PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM `project` WHERE (`userId` = ? AND `name` = ?) OR `id` = ?");
            deleteStatement.setString(1, project.getUserId());
            deleteStatement.setString(2, project.getName());
            deleteStatement.setString(3, project.getId());

            deleteStatement.executeUpdate();
        }

        final PreparedStatement statement = connection.prepareStatement("INSERT INTO `project` " +
                "(`id`, `userId`, `name`, `description`, `creationDate`, `startDate`, `endDate`, `status`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, project.getId());
        statement.setString(2, project.getUserId());
        statement.setString(3, project.getName());
        statement.setString(4, project.getDescription());
        statement.setTimestamp(5, new Timestamp(project.getCreationDate().getTime()), Calendar.getInstance());
        statement.setTimestamp(6, new Timestamp((Optional.ofNullable(project.getStartDate()).orElse(new Date())).getTime()), Calendar.getInstance());
        statement.setTimestamp(7, new Timestamp((Optional.ofNullable(project.getEndDate()).orElse(new Date())).getTime()), Calendar.getInstance());
        statement.setString(8, project.getStatus().toString());
        return statement.executeUpdate() > 0;
    }

    @Override @Nullable
    public String remove(@NotNull final String userId, @NotNull final String id) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `project` WHERE `userId` = ? AND `id` = ?");
        preparedStatement.setString(1, userId);
        preparedStatement.setString(2, id);
        if (preparedStatement.executeUpdate() > 0) return id;
        return null;
    }

    @Override @Nullable
    public String remove(@NotNull final String userId, @NotNull final Project project) throws SQLException {
        return remove(userId, project.getId());
    }

    @Override @NotNull
    public Collection<String> removeByName(@NotNull final String userId, @NotNull final String name) throws SQLException {
        final Collection<String> ids = new HashSet<>();
        final PreparedStatement getIdsStatement = connection.prepareStatement("SELECT `id` FROM `project` WHERE `userId` = ? AND `name` = ?");
        final PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM `project` WHERE `userId` = ? AND `name` = ?");
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
    public Collection<String> removeAll(@NotNull final String userId) throws SQLException {
        final Collection<String> ids = new HashSet<>();
        final PreparedStatement getIdsStatement = connection.prepareStatement("SELECT `id` FROM `project` WHERE `userId` = ?");
        final PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM `project` WHERE `userId` = ?");
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
