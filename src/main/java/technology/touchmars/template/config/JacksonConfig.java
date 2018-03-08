package technology.touchmars.template.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import technology.touchmars.template.util.DateModule;

@Configuration
public class JacksonConfig {

    @Bean
    public Module getDateModule() {
        return new DateModule();
    }

    @Bean
    public Module getJavaTimeModule() {
        return new JavaTimeModule();
    }

}
