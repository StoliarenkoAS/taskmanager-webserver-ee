package ru.stoliarenkoas.tm.webserver.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.stoliarenkoas.tm.webserver.Status;
import ru.stoliarenkoas.tm.webserver.api.entity.PlannedEntity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractPlannedEntity implements PlannedEntity {

    @NotNull @Id
    private String id = UUID.randomUUID().toString();

    @NotNull @Enumerated(EnumType.STRING)
    private Status status = Status.PLANNED;

    @NotNull
    private String name = "initName";

    @NotNull
    private String description = "initDescription";

    @NotNull
    private Date creationDate = new Date();

    @NotNull
    private Date startDate = creationDate;

    @NotNull
    private Date endDate = creationDate;

}
