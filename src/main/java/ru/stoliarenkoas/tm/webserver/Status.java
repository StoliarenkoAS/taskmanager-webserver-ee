package ru.stoliarenkoas.tm.webserver;

import org.jetbrains.annotations.NotNull;

public enum Status {
    PLANNED("planned"),
    IN_PROGRESS("in-progress"),
    COMPLETE("complete");

    @NotNull public final String displayName;

    Status(@NotNull final String displayName) {
        this.displayName = displayName;
    }
}
