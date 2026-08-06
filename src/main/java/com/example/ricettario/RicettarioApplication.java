package com.example.ricettario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class RicettarioApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(RicettarioApplication.class, args);

		Environment env = context.getEnvironment();
		System.out.println("Valore letto da Spring Environment: " + env.getProperty("GEMINI_API_KEY"));
	}

}
