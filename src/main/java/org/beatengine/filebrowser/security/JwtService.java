package org.beatengine.filebrowser.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(SESSION_TOKEN_LIFETIME_SEC)))
                .signWith(getSigeInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(final String token)
    {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static String getRandomHexString(final int length){
        final Random r = new Random();
        final StringBuffer sb = new StringBuffer();
        while(sb.length() < length){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, length);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static final String SIGN_IN_KEY = getRandomHexString(256);

    private Key getSigeInKey()
    {
        final byte[] kb = Decoders.BASE64.decode(SIGN_IN_KEY);
        return Keys.hmacShaKeyFor(kb);
    }

    private Claims extractAllClaims(final String token)
    {
        return Jwts.parser()
                .setSigningKey(getSigeInKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }
}
