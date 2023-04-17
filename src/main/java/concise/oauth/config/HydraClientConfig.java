package concise.oauth.config;
import concise.oauth.properties.HydraProperties;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sh.ory.hydra.ApiClient;
import sh.ory.hydra.api.OAuth2Api;

@Configuration
public class HydraClientConfig {

    @Bean("oAuth2Api")
    public OAuth2Api oAuth2Api(@NonNull final HydraProperties hydraProperties) {
        ApiClient apiClient = sh.ory.hydra.Configuration.getDefaultApiClient()
                .setBasePath(hydraProperties.getBasePath());
        return new OAuth2Api(apiClient);
    }

}
