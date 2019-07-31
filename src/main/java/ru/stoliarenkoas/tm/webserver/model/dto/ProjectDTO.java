package ru.stoliarenkoas.tm.webserver.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.enumerate.Status;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "project")
public class ProjectDTO implements Serializable {

    private static final long serialVersionUID = 12345678902L;
    @NotNull private String id = UUID.randomUUID().toString();
    @NotNull private String userId = "initId";
    @NotNull private Status status = Status.PLANNED;
    @Nullable private String name;
    @Nullable private String description;
    @NotNull private Date creationDate = new Date();
    @Nullable private Date startDate;
    @Nullable private Date endDate;

    @Override @NotNull
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("ProjectDTO: %s (%s) belongs to id:=%s.%n"+
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
        if (!(obj instanceof ProjectDTO)) return false;
        return this.id.equals(((ProjectDTO)obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
