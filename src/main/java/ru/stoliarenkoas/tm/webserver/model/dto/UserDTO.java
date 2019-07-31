package ru.stoliarenkoas.tm.webserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 12345678904L;

    public enum Role {
        USER("user"),
        ADMIN("administrator");

        @Getter
        @NotNull private final String displayName;

        Role(@NotNull final String displayName) {
            this.displayName = displayName;
        }
    }

    @XmlElement @NotNull private String id = UUID.randomUUID().toString();
    @XmlElement @Nullable private String login;
    @XmlElement @Nullable private String passwordHash;
    @XmlElement @NotNull private Role role = Role.USER;

    @Nullable @JsonIgnore
    public String getName() {
        return login;
    }

    @Override @NotNull
    public String toString() {
        return String.format("UserDTO: %s, id: %s, Role: %s", login, id, role.displayName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof UserDTO)) return false;
        final UserDTO other = (UserDTO) obj;
        if (other.login == null) return false;
        return other.id.equals(this.id) && other.login.equals(this.login);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
