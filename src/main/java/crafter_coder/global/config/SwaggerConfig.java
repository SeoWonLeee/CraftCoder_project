package crafter_coder.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${dev-host}")
    private String serverHost;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(servers());
    }
    private Info apiInfo() {
        return new Info()
                .title("INTIP API명세서")
                .description("api 명세서")
                .version("1.0.0");
    }
    private List<Server> servers() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080").description("Local URL"));
        servers.add(new Server().url(serverHost).description("Dev Server URL"));
        return servers;
    }
}