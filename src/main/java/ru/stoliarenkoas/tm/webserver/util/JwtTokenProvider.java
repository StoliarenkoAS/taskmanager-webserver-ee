package ru.stoliarenkoas.tm.webserver.util;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.stoliarenkoas.tm.webserver.exception.AccessForbiddenException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@PropertySource("classpath:security.properties")
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @NotNull
    public String createToken(@NotNull final String userId, @NotNull final String role) {

        @NotNull final Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);

        @NotNull final Date now = new Date();
        @NotNull final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Nullable
    public String resolveToken(@NotNull final HttpServletRequest servletRequest) {
        @Nullable final String bearerToken = servletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoles(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }

    public void validateToken(@Nullable final String token) throws AccessForbiddenException {
        if (token == null || token.isEmpty()) throw new AccessForbiddenException("invalid token");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                throw new AccessForbiddenException("expired token");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new AccessForbiddenException("Expired or invalid JWT token");
        }
    }

}
