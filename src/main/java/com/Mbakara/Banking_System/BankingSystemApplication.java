package com.Mbakara.Banking_System;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Glorious Banking-System ",
				description = "Backend Rest APIs for Glorious Banking App",
				version = "v1.0",
				contact = @Contact(
						name = "Glory Usen",
						email = "gloryanwana4@gmail.com",
						url = "https://github.com/GloryUsen/Bank_System"
				),
				license = @License(
						name = "The Glorious Project",
						url = "https://github.com/GloryUsen/Bank_System"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Glorious Banking-System Application Documentation",
				url = "https://github.com/GloryUsen/Bank_System"
		)
)
public class BankingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingSystemApplication.class, args);
	}

}
