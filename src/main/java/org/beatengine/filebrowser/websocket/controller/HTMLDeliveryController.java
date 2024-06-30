package org.beatengine.filebrowser.websocket.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.beatengine.filebrowser.mapping.FileInfo;
import org.beatengine.filebrowser.security.TokenStore;
import org.beatengine.filebrowser.websocket.protocol.HTMLComponentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class HTMLDeliveryController {

    @Autowired
    private TemplateEngine templateEngine;

    private final TokenStore tokenStore = TokenStore.getStore();

    private Path currentPath = Path.of(System.getProperty("user.home"));

    /*           --> /app/component */
    @MessageMapping("/component")
    @SendTo("/topic/components")
    public HTMLComponentMessage resolveMessage(final HTMLComponentMessage message) throws Exception {

        final String html = resolveTemplate(message);
        runActions(message.getProps());
        return new HTMLComponentMessage(message.getId(), html, false, true);
    }

    private void runActions(final Map<String, String> props)
    {
        String actions = props.get("action");
        if("navigateToParent".equals(actions))
        {
            final Path curp = currentPath.getParent();
            if(curp != null) {
                currentPath = curp;
            }
        }
    }

    private String formatFileSize(long sz)
    {
        if(sz < 1024)
        {
            return sz + " bytes";
        } else if(sz < 1024*1024)
        {
            return sz / 1024 + " kb";
        } else
        {
            return sz / 1024*1024 + " mb";
        }
    }

    public List<FileInfo> listFilesUsingDirectoryStream(final Path dir) throws IOException {
        List<FileInfo> fileSet = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    File f = path.toFile();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
                    fileSet.add(new FileInfo(true, f.getName(), "", dateFormat.format(f.lastModified())));
                }
                else {
                    File f = path.toFile();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
                    new FileInfo(false, f.getName(), formatFileSize(f.length()), dateFormat.format(f.lastModified()));
                }
            }
        }
        return fileSet;
    }

    private void setFilesAndDirectoriesInfo(final Context context)
    {
        try {
            context.setVariable("fileinfos", listFilesUsingDirectoryStream(currentPath));
        } catch (IOException e) {
            context.setVariable("fileinfos", Collections.emptyList());
            throw new RuntimeException(e);
        }
    }

    public String resolveTemplate(final HTMLComponentMessage message)
    {
        final Context context = new Context();
        // Add the variables into the model
        context.setVariable("customText", "Hello World over Websocket!");
        context.setVariable("currentPath", currentPath.toString());
        setFilesAndDirectoriesInfo(context);
        // Load the Template
        return templateEngine.process(message.getId(), context);
    }

}
