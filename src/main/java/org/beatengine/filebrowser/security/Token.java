package org.beatengine.filebrowser.security;

import java.time.Instant;

public class Token {

    public Token(final String token, final long userId) {
        this.token = token;
        this.userId = userId;
        creation = Instant.now();
    }

    public String token;
    public Instant creation;
    public long userId;

    public boolean isExpired()
    {
        return creation.plusSeconds(JwtService.SESSION_TOKEN_LIFETIME_SEC).isBefore(Instant.now());
    }
}
