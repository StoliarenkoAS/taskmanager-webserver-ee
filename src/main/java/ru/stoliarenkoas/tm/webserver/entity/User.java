package ru.stoliarenkoas.tm.webserver.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.api.entity.Entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Entity, Serializable {

    private static final long serialVersionUID = 12345678904L;

    public enum Role {
        USER("user"),
        ADMIN("administrator");

        @NotNull private final String displayName;

        public String getDisplayName() {
            return displayName;
        }

        Role(@NotNull final String displayName) {
            this.displayName = displayName;
        }
    }

    @NotNull private String id = UUID.randomUUID().toString();
    @Nullable private String login;
    @Nullable private String passwordHash;
    @NotNull private Role role = Role.USER;

    @Override @Nullable
    public String getName() {
        return login;
    }

    @Override @NotNull
    public String toString() {
        return String.format("User: %s, id: %s, Role: %s", login, id, role.displayName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        final User other = (User) obj;
        if (other.login == null) return false;
        return other.id.equals(this.id) && other.login.equals(this.login);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
