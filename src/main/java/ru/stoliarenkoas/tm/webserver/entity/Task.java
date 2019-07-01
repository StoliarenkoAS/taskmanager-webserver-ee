package ru.stoliarenkoas.tm.webserver.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.entity.PlannedEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Task implements PlannedEntity, Serializable {

    private static final long serialVersionUID = 12345678903L;
    @NotNull private String id = UUID.randomUUID().toString();
    @NotNull private String userId = "initId";
    @NotNull private String projectId = "initId";
    @NotNull private Status status = Status.PLANNED;
    @Nullable private String name;
    @Nullable private String description;
    @NotNull private Date creationDate = new Date();
    @Nullable private Date startDate;
    @Nullable private Date endDate;

    public Task(@NotNull final String userId, @NotNull final String name) {
        this.userId = userId;
        this.name = name;
    }

    public Task() {}

    @Override @NotNull
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("Task: %s (%s) for project id:=%s.%n" +
                "Creation: %s, Start: %s, End: %s."+
                "Status: %s.",
                name,
                description,
                userId,
                formatter.format(creationDate),
                formatter.format(startDate),
                formatter.format(endDate),
                status.displayName);
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;
        return this.id.equals(((Task)obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
