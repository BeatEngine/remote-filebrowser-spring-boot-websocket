package org.beatengine.filebrowser.websocket;

import jakarta.servlet.http.Cookie;
import org.beatengine.filebrowser.security.JwtAuthenticationFilter;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CookieHandshakeInterceptor implements HandshakeInterceptor {
    /***
     * Inject the HTTP Header Cookie to the websocket handler attributes
     * @param request the current request
     * @param response the current response
     * @param wsHandler the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     * session; the provided attributes are copied, the original map is not used.
     * @return true
     * @throws Exception
     */

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            Cookie[] cookies =
                    servletRequest.getServletRequest().getCookies();

            if (cookies != null) {
                System.getLogger(this.getClass().getSimpleName()).log(System.Logger.Level.INFO,
                        "Cookies in ws handshake: " + Arrays.stream(cookies).map(Cookie::getName).collect(Collectors.joining(", ")));
                Arrays.stream(cookies)
                        .filter(c -> JwtAuthenticationFilter.AUTH_COOKIE_NAME.equals(c.getName()))
                        .findFirst()
                        .ifPresent(cookie ->
                                attributes.put(JwtAuthenticationFilter.AUTH_COOKIE_NAME, cookie.getValue())
                        );
            }

        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
