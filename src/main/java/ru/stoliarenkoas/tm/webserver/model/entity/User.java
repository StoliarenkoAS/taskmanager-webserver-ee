package ru.stoliarenkoas.tm.webserver.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.enumerate.Role;
import ru.stoliarenkoas.tm.webserver.model.dto.UserDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@Cacheable
@NoArgsConstructor
@Table(name = "user")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {

    public User(@NotNull final UserDTO userDTO) {
        this.id = userDTO.getId();
        this.login = userDTO.getLogin();
        this.passwordHash = userDTO.getPasswordHash();
        this.role = userDTO.getRole();
    }

    @NotNull @Id
    private String id = UUID.randomUUID().toString();

    @Nullable @Column(unique = true)
    private String login;

    @Nullable @Column(name = "pwdHash")
    private String passwordHash;

    @NotNull @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @NotNull @OneToMany(targetEntity = Project.class, cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Project> projects = new ArrayList<>();

    @NotNull @OneToMany(targetEntity = Session.class, cascade = CascadeType.REMOVE, mappedBy = "user")
    private List<Session> sessions = new ArrayList<>();

    public @Nullable String getName() {
        return login;
    }

    public UserDTO toDTO() {
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setLogin(login);
        userDTO.setPasswordHash(passwordHash);
        userDTO.setRole(role);
        return userDTO;
    }

}
