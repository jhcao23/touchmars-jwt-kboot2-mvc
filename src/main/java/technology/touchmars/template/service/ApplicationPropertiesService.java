package technology.touchmars.template.service;


/**
 * you must specify application secret key value in application.yml.bak
 * application.secret-key: KEY
 */
public interface ApplicationPropertiesService {

    public String getSecretKey();

}
