package org.beatengine.filebrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
@ComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Filebrowser {

	public static void main(String[] args) {
		SpringApplication.run(Filebrowser.class, args);
	}

}
