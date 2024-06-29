package org.beatengine.filebrowser.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service

public class LogoutService implements LogoutHandler {

    private final TokenStore tokenStore = TokenStore.getStore();

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        if (tokenStore.findByToken(jwt) != null) {
            tokenStore.removeToken(jwt);
            SecurityContextHolder.clearContext();
        }
    }
}
