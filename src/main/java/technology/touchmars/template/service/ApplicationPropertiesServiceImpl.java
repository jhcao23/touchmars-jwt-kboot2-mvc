package technology.touchmars.template.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = ApplicationPropertiesServiceImpl.APPLICATION_PROPERTIES_PREFIX)
public class ApplicationPropertiesServiceImpl implements ApplicationPropertiesService {

    public static final String APPLICATION_PROPERTIES_PREFIX = "application";

    @Getter @Setter
    private String secretKey;


}
