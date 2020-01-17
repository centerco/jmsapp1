package ru.chuchalov;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class PHMessageApplication {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(PHMessageApplication.class)
			.listeners(new PHMessageApplicationListener())
			.run(args);
	}
}
