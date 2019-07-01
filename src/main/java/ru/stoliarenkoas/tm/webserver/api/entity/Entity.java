package ru.stoliarenkoas.tm.webserver.api.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Entity {

    @NotNull
    String getId();

    @Nullable
    String getName();

}
