package io.groom.scubadive.shoppingmall.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "쇼핑몰 API", version = "v1", description = "쇼핑몰 프로젝트 API 명세서"),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버")
        }
)
public class SwaggerConfig {
}
