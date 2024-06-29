package org.beatengine.filebrowser.websocket.controller;

import org.beatengine.filebrowser.websocket.protocol.HTMLComponentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
public class HTMLDeliveryController {

    @Autowired
    private TemplateEngine templateEngine;

    /*           --> /app/component */
    @MessageMapping("/component")
    @SendTo("/topic/components")
    public HTMLComponentMessage resolveMessage(final HTMLComponentMessage message) throws Exception {

        final String html = resolveTemplate(message);
        return new HTMLComponentMessage(message.getId(), html, false, true);
    }



    public String resolveTemplate(final HTMLComponentMessage message)
    {
        final Context context = new Context();
        // Add the variables into the model
        context.setVariable("customText", "Hello World over Websocket!");
        // Load the Template
        return templateEngine.process(message.getId(), context);
    }

}
