package ru.stoliarenkoas.tm.webserver.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.comparator.ComparatorType;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SessionDTO {

    @NotNull private String id = UUID.randomUUID().toString();
    @NotNull private String userId = "";
    @NotNull private String userLogin = "";
    @NotNull private Date creationDate = new Date();
    @NotNull private ComparatorType sortMethod = ComparatorType.BY_CREATION_DATE;
    @Nullable private String hash;

    public SessionDTO(@NotNull final String userId, @NotNull final String userLogin) {
        this.userId = userId;
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "SessionDTO{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", sortMethod=" + sortMethod +
                '}';
    }

}
