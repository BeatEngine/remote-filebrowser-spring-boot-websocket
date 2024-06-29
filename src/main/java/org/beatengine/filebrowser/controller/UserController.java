package org.beatengine.filebrowser.websocket.controller;

import org.beatengine.filebrowser.entity.User;
import org.beatengine.filebrowser.repository.UserRepository;
import org.beatengine.filebrowser.security.JwtAuthenticationFilter;
import org.beatengine.filebrowser.security.Token;
import org.beatengine.filebrowser.security.TokenStore;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class UserController {

    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }


}
