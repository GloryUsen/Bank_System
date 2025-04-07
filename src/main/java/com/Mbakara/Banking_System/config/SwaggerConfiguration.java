package com.Mbakara.Banking_System.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

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
            ),
        //security = @SecurityRequirement(name = "BearerAuth") // Apply JWT security globally
        security = @SecurityRequirement(name = "Bearer Authentication")
      )

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "JWT token authentication"
)
//   @SecurityScheme(
//           name = "BearerAuth",
//           scheme = "bearer",
//           type = SecuritySchemeType.HTTP,
//           bearerFormat = "JWT" //Informing swagger to use JWT tokens
//   )



public class SwaggerConfiguration {


}
