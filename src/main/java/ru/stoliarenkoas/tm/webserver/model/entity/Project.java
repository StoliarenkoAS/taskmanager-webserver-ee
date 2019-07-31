package ru.stoliarenkoas.tm.webserver.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.model.dto.ProjectDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Cacheable
@NoArgsConstructor
@Table(name = "project")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends AbstractPlannedEntity {

    @NotNull @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL, mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();
    
    @NotNull @ManyToOne(targetEntity = User.class)
    private User user;

    public Project(@NotNull final ProjectDTO dto, @NotNull final User user) {
        this.setId(dto.getId());
        this.setName(dto.getName() == null ? "NNM" : dto.getName());
        this.setDescription(dto.getDescription() == null ? "no description" : dto.getDescription());
        this.setStatus(dto.getStatus());
        this.setCreationDate(dto.getCreationDate());
        this.setStartDate(dto.getStartDate() == null ? dto.getCreationDate() : dto.getStartDate());
        this.setEndDate(dto.getEndDate() == null ? dto.getCreationDate() : dto.getEndDate());
        this.user = user;
    }

    public ProjectDTO toDTO() {
        final ProjectDTO dto = new ProjectDTO();
        dto.setId(this.getId());
        dto.setUserId(this.user.getId());
        dto.setName(this.getName());
        dto.setDescription(this.getDescription());
        dto.setStatus(this.getStatus());
        dto.setCreationDate(this.getCreationDate());
        dto.setStartDate(this.getStartDate());
        dto.setEndDate(this.getEndDate());
        return dto;
    }

    public @Nullable String getUserId() {
        return user.getId();
    }

}
