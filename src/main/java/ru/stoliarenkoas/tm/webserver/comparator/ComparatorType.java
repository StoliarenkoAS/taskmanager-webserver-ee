package ru.stoliarenkoas.tm.webserver.comparator;

import org.jetbrains.annotations.NotNull;
import ru.stoliarenkoas.tm.webserver.api.entity.PlannedEntity;

import java.util.Comparator;

public enum ComparatorType {
    BY_CREATION_DATE(new CreationDateComparator(), "creation-date"),
    BY_START_DATE(new StartDateComparator(), "start-date"),
    BY_END_DATE(new EndDateComparator(), "end-date"),
    BY_STATUS(new StatusComparator(), "status");

    public @NotNull final Comparator<PlannedEntity> comparator;
    public @NotNull final String commandName;

    ComparatorType(@NotNull final Comparator<PlannedEntity> comparator, @NotNull final String commandName) {
        this.comparator = comparator;
        this.commandName = commandName;
    }
}
