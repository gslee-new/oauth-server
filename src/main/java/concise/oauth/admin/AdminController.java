package concise.oauth.admin;

import concise.oauth.hydra.HydraAdminClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.ory.hydra.model.IntrospectedOAuth2Token;
import sh.ory.hydra.model.OAuth2Client;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    @NonNull final HydraAdminClient hydraAdminClient;

    @PostMapping(value = "client")
    public ResponseEntity<OAuth2Client> createClient(@RequestBody ClientsForm clientsForm) {
        OAuth2Client reqClient = map(clientsForm);
        OAuth2Client client = hydraAdminClient.createClient(reqClient);

        return ResponseEntity.ok(client);
    }



    @GetMapping(value = "/token/introspect")
    public ResponseEntity<IntrospectedOAuth2Token> introspectToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(hydraAdminClient.introspectToken(token));
    }

    /**
     * {"redirectUris":["http://localhost:8081/token/code", "http://localhost:8080/logout", "http://localhost:8081/token/logout"],
     * "postLogoutRedirectUris":["http://localhost:8080/logout","http://localhost:8081/token/logout"],
     * "grantTypes":["authorization_code","refresh_token", "client_credentials", "implicit"],
     * "responseTypes":["code"],
     * "scopes":["offline", "openid"],
     * "clientSecret":"111111"}
     *
     * @param clientsForm
     * @return
     */
    private OAuth2Client map(ClientsForm clientsForm) {
        String joinScopes = clientsForm.getScopes().stream().map(String::valueOf).collect(Collectors.joining(" "));

        OAuth2Client oAuth2Client = new OAuth2Client();

        /** 클라이언트 아이디와 비번값이 없으면 랜덤으로 클라이언트 정보 생성 **/
        oAuth2Client.clientName(clientsForm.getClientName());
        oAuth2Client.clientSecret(clientsForm.getClientSecret());

        /** authorization code 전달할 리다이렉트 url**/
        oAuth2Client.redirectUris(clientsForm.getRedirectUris());
        oAuth2Client.grantTypes(clientsForm.getGrantTypes());
        oAuth2Client.responseTypes(clientsForm.getResponseTypes());
        oAuth2Client.scope(joinScopes);
        oAuth2Client.postLogoutRedirectUris(clientsForm.getPostLogoutRedirectUris());
        oAuth2Client.authorizationCodeGrantAccessTokenLifespan("1h");
        oAuth2Client.authorizationCodeGrantRefreshTokenLifespan("1h");
        oAuth2Client.authorizationCodeGrantIdTokenLifespan("1h");
        oAuth2Client.clientCredentialsGrantAccessTokenLifespan("1h");
        oAuth2Client.contacts(List.of());
        oAuth2Client.implicitGrantAccessTokenLifespan("1h");
        oAuth2Client.implicitGrantIdTokenLifespan("1h");
        oAuth2Client.jwtBearerGrantAccessTokenLifespan("1h");
        oAuth2Client.refreshTokenGrantAccessTokenLifespan("1h");
        oAuth2Client.refreshTokenGrantRefreshTokenLifespan("1h");
        oAuth2Client.refreshTokenGrantIdTokenLifespan("1h");

        return oAuth2Client;
    }
}
