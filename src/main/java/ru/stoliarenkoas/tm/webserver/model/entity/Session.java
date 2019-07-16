package ru.stoliarenkoas.tm.webserver.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;
import ru.stoliarenkoas.tm.webserver.model.dto.SessionDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Cacheable
@Table(name = "session")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Session {

    @NotNull @Id
    private String id = UUID.randomUUID().toString();

    @NotNull
    private Date creationDate = new Date();

    @NotNull @Enumerated(EnumType.STRING)
    private ComparatorType sortMethod = ComparatorType.BY_CREATION_DATE;

    @Nullable
    private String hash;

    @ManyToOne()
    private User user;

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", creationDate=" + creationDate +
                ", sortMethod=" + sortMethod +
                ", user=" + user +
                '}';
    }

    public Session() {
    }

    public Session(@NotNull final SessionDTO dto, final User user) {
        this.id = dto.getId();
        this.sortMethod = dto.getSortMethod();
        this.creationDate = dto.getCreationDate();
        this.hash = dto.getHash();
        this.user = user;
    }

    @NotNull
    public SessionDTO toDTO() {
        final SessionDTO dto = new SessionDTO();
        dto.setId(this.id);
        dto.setUserId(user.getId());
        if (user.getLogin() != null) dto.setUserLogin(user.getLogin());
        dto.setSortMethod(this.sortMethod);
        dto.setHash(this.hash);
        dto.setCreationDate(this.creationDate);
        return dto;
    }
}
