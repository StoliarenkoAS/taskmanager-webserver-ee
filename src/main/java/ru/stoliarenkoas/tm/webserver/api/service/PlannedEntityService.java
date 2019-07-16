package ru.stoliarenkoas.tm.webserver.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.entity.PlannedEntity;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;

import java.util.Collection;

public interface PlannedEntityService<T extends PlannedEntity> extends Service<T> {

    @NotNull
    Collection<T> search(@Nullable SessionDTO session, @Nullable String searchLine) throws Exception;

}
