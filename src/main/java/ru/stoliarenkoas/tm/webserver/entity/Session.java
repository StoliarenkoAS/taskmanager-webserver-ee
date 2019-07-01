package ru.stoliarenkoas.tm.webserver.entity;

import org.jetbrains.annotations.NotNull;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;

import java.util.Date;
import java.util.UUID;

public class Session {

    @NotNull private String id = UUID.randomUUID().toString();
    @NotNull private String userId = "";
    @NotNull private String userLogin = "";
    @NotNull private Date creationDate = new Date();
    @NotNull private ComparatorType sortMethod = ComparatorType.BY_CREATION_DATE;
    private String hash;

    public Session(@NotNull final String userId, @NotNull final String userLogin) {
        this.userId = userId;
        this.userLogin = userLogin;
    }

    public Session() {
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", sortMethod=" + sortMethod +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ComparatorType getSortMethod() {
        return sortMethod;
    }

    public void setSortMethod(ComparatorType sortMethod) {
        this.sortMethod = sortMethod;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
