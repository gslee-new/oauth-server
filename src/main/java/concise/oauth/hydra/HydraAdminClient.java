package concise.oauth.hydra;

import concise.oauth.consent.AcceptConsentRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sh.ory.hydra.ApiException;
import sh.ory.hydra.api.OAuth2Api;
import sh.ory.hydra.model.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HydraAdminClient {
    @NonNull final OAuth2Api oAuth2Api;

    private static final Long DEFAULT_SESSION_EXPIRATION_IN_SECONDS = 3600L;

    /**
     * client list 조회
     * @return
     */
    public List<OAuth2Client> listOAuth2Client() {
        try {
            return oAuth2Api.listOAuth2Clients(1000L, null, null, null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 클라이언트 생성
     * @param reqOAuth2Client
     * @return
     */
    public OAuth2Client createClient(OAuth2Client reqOAuth2Client) {
        try {
            return oAuth2Api.createOAuth2Client(reqOAuth2Client);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<OAuth2LoginRequest> getLoginRequest(@NonNull String loginChallenge) {
        try {
            return Optional.of(oAuth2Api.getOAuth2LoginRequest(loginChallenge));
        } catch (ApiException e) {
            return switch (e.getCode()) {
                case 410 -> Optional.empty(); // requestWasHandledResponse
                case 400, 404 -> Optional.empty(); // jsonError
                case 500 -> Optional.empty(); // jsonError
                default -> throw new RuntimeException("unhandled code: " + e.getCode(), e);
            };
        }
    }

    public OAuth2RedirectTo acceptLoginRequest(OAuth2LoginRequest request) {
        AcceptOAuth2LoginRequest acceptOAuth2LoginRequest = new AcceptOAuth2LoginRequest();
        acceptOAuth2LoginRequest.subject(request.getSubject());
        acceptOAuth2LoginRequest.remember(request.getSkip());
        try {
            return oAuth2Api.acceptOAuth2LoginRequest(request.getChallenge(), acceptOAuth2LoginRequest);
        } catch (ApiException e) {
            switch (e.getCode()) {
                case 400, 401, 404, 500 -> throw new RuntimeException("code: " + e.getCode(), e); // jsonError
                default -> throw new RuntimeException("unhandled code: " + e.getCode(), e);
            }
        }
    }

    public OAuth2ConsentRequest getConsentRequest(@NonNull String consentChallenge) {
        try {
            return oAuth2Api.getOAuth2ConsentRequest(consentChallenge);
        } catch (ApiException e) {
            switch (e.getCode()) {
                case 400, 404 -> throw new RuntimeException("code: " + e.getCode(), e); // jsonError
                default -> throw new RuntimeException("unhandled code: " + e.getCode(), e);
            }
        }
    }

    public OAuth2RedirectTo acceptConsentRequest(@NonNull AcceptConsentRequest acceptConsentRequest) {
        AcceptOAuth2ConsentRequest acceptOAuth2ConsentRequest = new AcceptOAuth2ConsentRequest()
                .grantScope(acceptConsentRequest.getScopes())
                .grantAccessTokenAudience(acceptConsentRequest.getGrantAccessTokenAudience())
                .remember(acceptConsentRequest.isRemember())
                .rememberFor(DEFAULT_SESSION_EXPIRATION_IN_SECONDS);
                //.session(getAcceptOAuth2ConsentRequestSession());

        try {
            return oAuth2Api.acceptOAuth2ConsentRequest(acceptConsentRequest.getConsentChallenge(),
                    acceptOAuth2ConsentRequest);
        } catch (ApiException e) {
            switch (e.getCode()) {
                case 404, 500 -> throw new RuntimeException("code: " + e.getCode(), e); // jsonError
                default -> throw new RuntimeException("unhandled code: " + e.getCode(), e);
            }
        }
    }

    /**
     * id_token 에 커스텀 클레임을 추가하는 메서드
     * @param
     * @return
     */
    private AcceptOAuth2ConsentRequestSession getAcceptOAuth2ConsentRequestSession() {
        record CustomClaims(String cn) {}

        return new AcceptOAuth2ConsentRequestSession()
                .idToken(new CustomClaims("write customize claim"));
    }

    public IntrospectedOAuth2Token introspectToken(String token) {
        try {
            return oAuth2Api.introspectOAuth2Token(token, null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public OAuth2RedirectTo acceptOAuth2LogoutRequest(String logoutChallenge) {
        try {
            return oAuth2Api.acceptOAuth2LogoutRequest(logoutChallenge);
        } catch (ApiException e) {
            switch (e.getCode()) {
                case 400, 404 -> throw new RuntimeException("code: " + e.getCode(), e); // jsonError
                default -> throw new RuntimeException("unhandled code: " + e.getCode(), e);
            }
        }
    }

}
