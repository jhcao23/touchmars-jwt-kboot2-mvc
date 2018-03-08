package technology.touchmars.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import technology.touchmars.template.service.*;

@Configuration
public class FirebaseConfig {

    @Autowired
    private Environment environment;
    @Autowired
    private ApplicationPropertiesService applicationPropertiesService;

    // Firebase

    @Bean
    public FirebaseDiscoveryService getFirebaseDiscoveryService() {
        return new FirebaseDiscoveryServiceImpl(environment);
    }

    @Bean
    public FirebaseService getFirebaseService() {
        return new FirebaseServiceImpl(getFirebaseDiscoveryService());
    }

    // Jwt

    @Bean
    public JwtTokenService getJwtTokenService() {
        return new JwtTokenServiceImpl(getFirebaseService(), applicationPropertiesService);
    }


}
