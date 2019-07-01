package ru.stoliarenkoas.tm.webserver.api.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.Status;

import java.util.Date;

public interface PlannedEntity extends Entity {

    @Nullable
    String getUserId();

    @NotNull
    Date getCreationDate();

    @Nullable
    Date getStartDate();

    @Nullable
    Date getEndDate();

    @NotNull
    Status getStatus();

}
