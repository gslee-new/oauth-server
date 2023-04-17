package concise.oauth.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ClientsForm {
    private String clientName;
    private String clientSecret;
    private List<String> redirectUris;
    private List<String> grantTypes;
    private List<String> responseTypes;
    private List<String> scopes;
    private List<String> postLogoutRedirectUris;
}
