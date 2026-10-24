package org.beatengine.filebrowser.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    final TokenStore tokenStore = TokenStore.getStore();

    final static int SESSION_TOKEN_LIFETIME_SEC = 86400; // 24h default

    public boolean isValid(final String token, final UserDetails userDetails) {
        final String email = extractEmail(token.substring(7));
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractEmail(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(final UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    public String generateToken(final Map<String, Object> extraClaims, final UserDetails userDetails) {
        return Jwts.builder().claims(extraClaims).
                subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(SESSION_TOKEN_LIFETIME_SEC)))
                .signWith(getSignInKey())
                .compact();
    }

    private boolean isTokenExpired(final String token)
    {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private SecretKey getSignInKey()
    {
        return Jwts.SIG.HS256.key().build();
    }

    private Claims extractAllClaims(final String token)
    {
        return Jwts.parser()
                .decryptWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
