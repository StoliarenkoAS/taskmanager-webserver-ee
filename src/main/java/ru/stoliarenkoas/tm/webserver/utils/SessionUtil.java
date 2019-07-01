package ru.stoliarenkoas.tm.webserver.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stoliarenkoas.tm.webserver.entity.Session;
import ru.stoliarenkoas.tm.webserver.entity.User;

public class SessionUtil {

    public static boolean isValid(@NotNull final Session session) {
        final String sessionHash = session.getHash();
        session.setHash(null);
        final String realSessionHash = CypherUtil.getMd5(session.toString());
        final boolean success = realSessionHash.equals(sessionHash);
        session.setHash(sessionHash);
        return success;
    }

    public static void sign(@NotNull final Session session) {
        session.setHash(null);
        session.setHash(CypherUtil.getMd5(session.toString()));
    }

    @Nullable
    public static Session getSessionForUser(@NotNull final User user) {
        final String userLogin = user.getLogin();
        if (userLogin == null) return null;
        final Session session = new Session(user.getId(), userLogin);
        sign(session);
        return session;
    }

}
