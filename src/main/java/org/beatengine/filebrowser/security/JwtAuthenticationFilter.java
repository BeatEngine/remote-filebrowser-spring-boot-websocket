package org.beatengine.filebrowser.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private final JwtService jwtService = new JwtService();

    private final UserDetailsService userDetailsService;

    public final static String HEADER_NAME_AUTH = "Authorization";
    public final static String HEADER_VALUE_PREFIX_BEARER = "Bearer ";
    public final static String AUTH_COOKIE_NAME = "auth-session";

    private final TokenStore tokenStore = TokenStore.getStore();

    public static Cookie getSessionCookie(final String jwtToken) {
        final Cookie cookie = new Cookie(AUTH_COOKIE_NAME, jwtToken);
        cookie.setHttpOnly(false); // Needed for Websocket auth
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(JwtService.SESSION_TOKEN_LIFETIME_SEC);
        return cookie;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //if(request.getHeader())
        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader(HEADER_NAME_AUTH);
        final String jwtToken;
        final String email;
        // Check header and cookies for AUTH_COOKIE_NAME
        if(authHeader == null || !authHeader.startsWith(HEADER_VALUE_PREFIX_BEARER))
        {
            // Use cookie instead

            Cookie auth = null;
            if(request.getCookies() != null) {
                for (final Cookie cookie : request.getCookies()) {
                    if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                        auth = cookie;
                        break;
                    }
                }
            }
            if(auth != null)
            {
                jwtToken = auth.getValue();
            }
            else {
                // Apply the filter --> Not authenticated
                filterChain.doFilter(request, response);
                return;
            }
        }
        else
        {
            jwtToken = authHeader.substring(7);
        }

        try {
            email = jwtService.extractEmail(jwtToken);
        } catch (Exception e)
        {
            filterChain.doFilter(request, response);
            return;
        }
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            final Token tok = tokenStore.findByToken(jwtToken);
            // If the token is valid for the email (contains it and is not expired)
            if(jwtService.isValid(jwtToken, userDetails) && tok != null && !tok.isExpired())
            {
                // Set the security-context token authenticated
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
