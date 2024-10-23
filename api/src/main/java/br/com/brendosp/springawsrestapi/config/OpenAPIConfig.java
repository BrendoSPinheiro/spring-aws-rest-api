package br.com.brendosp.springawsrestapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        var openAPIContact = new Contact();
        openAPIContact.setName("Brendo Souza Pinheiro");
        openAPIContact.setEmail("brendo.spinheiro@gmail.com");
        openAPIContact.setUrl("brendosp.com.br");

        var openAPILicence = new License();
        openAPILicence.setName("MIT License");
        openAPILicence.setUrl("https://github.com/BrendoSPinheiro/spring-aws-rest-api/blob/main/LICENSE");

        var openAPIInfo = new Info();
        openAPIInfo.setTitle("Spring AWS REST API");
        openAPIInfo.setDescription("REST API to practice Spring Boot and AWS");
        openAPIInfo.setVersion("1.0.0");
        openAPIInfo.setContact(openAPIContact);
        openAPIInfo.setLicense(openAPILicence);

        var openAPIServer = new Server();
        openAPIServer.setUrl("http://localhost:8080");
        openAPIServer.setDescription("Server URL in development environment");

        return new OpenAPI().info(openAPIInfo).servers(List.of(openAPIServer));
    }
}
