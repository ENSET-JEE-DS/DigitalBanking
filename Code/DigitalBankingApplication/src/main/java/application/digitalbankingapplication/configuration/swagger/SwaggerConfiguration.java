package application.digitalbankingapplication.configuration.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI digitalBankingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Banking API")
                        .description("Spring Boot REST API for Digital Banking Application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Digital Banking Team")
                                .email("support@digitalbanking.com")));
    }
}
