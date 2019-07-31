package ru.stoliarenkoas.tm.webserver.enumerate;

import org.jetbrains.annotations.NotNull;

public enum Role {
    USER("user"),
    ADMIN("administrator");

    @NotNull
    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    Role(@NotNull final String displayName) {
        this.displayName = displayName;
    }
}
