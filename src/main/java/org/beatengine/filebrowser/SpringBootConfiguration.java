package org.beatengine.filebrowser;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:postgresql.properties")
public class SpringBootConfiguration {
    //@Value(("${app.datasource.password}"))
    //private String password = "123456";
}
