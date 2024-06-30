package org.beatengine.filebrowser.controller;

import org.beatengine.filebrowser.security.JwtAuthenticationFilter;
import org.beatengine.filebrowser.security.Token;
import org.beatengine.filebrowser.security.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainAppController {

    /**
     * Get the main index page
     * @return A Thymeleaf model for the index
     */
    @GetMapping("/")
    public ModelAndView indexPage(@CookieValue(name = JwtAuthenticationFilter.AUTH_COOKIE_NAME, required = false) String token) {
        final Token tok = TokenStore.getStore().findByToken(token);
        if(token == null || tok == null || tok.isExpired())
        {
            //Session not found --> login
            return new ModelAndView("redirect:/auth/login");
        }


        ModelAndView mav = new ModelAndView("index");
        mav.addObject("initTitle", "File browser DEMO - Spring & STOMP");
        mav.addObject("jsPath", "js/");
        return mav;
    }

}
