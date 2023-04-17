package concise.oauth.login;

import concise.oauth.hydra.HydraAdminClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.ory.hydra.model.OAuth2LoginRequest;
import sh.ory.hydra.model.OAuth2RedirectTo;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    @NonNull final HydraAdminClient hydraAdminClient;


    public  LoginResult processInitialLoginRequest(@NonNull String loginChallenge) {
        Optional<OAuth2LoginRequest> maybeLoginRequest = hydraAdminClient.getLoginRequest(loginChallenge);
        if (maybeLoginRequest.isEmpty()) {
            return new LoginRequestNotFound();
        }

        OAuth2LoginRequest oAuth2LoginRequest = maybeLoginRequest.get();

        if (oAuth2LoginRequest.getSkip()) {
            OAuth2RedirectTo oAuth2RedirectTo = hydraAdminClient.acceptLoginRequest(oAuth2LoginRequest);
            return new LoginAcceptedFollowRedirect(oAuth2RedirectTo.getRedirectTo());
        }

        return new LoginNotSkippableDisplayLoginUI(loginChallenge);
    }

    public LoginResult processSubmittedLoginRequest(@NonNull String loginChallenge, LoginForm loginForm) {
        Optional<OAuth2LoginRequest> maybeLoginRequest = hydraAdminClient.getLoginRequest(loginChallenge);
        if (maybeLoginRequest.isEmpty()) {
            return new LoginRequestNotFound();
        }

        OAuth2LoginRequest oAuth2LoginRequest = maybeLoginRequest.get();
        oAuth2LoginRequest.setSubject(String.valueOf(System.currentTimeMillis()));
        oAuth2LoginRequest.skip(loginForm.remember());

        OAuth2RedirectTo oAuth2RedirectTo = hydraAdminClient.acceptLoginRequest(oAuth2LoginRequest);

        return new LoginAcceptedFollowRedirect(oAuth2RedirectTo.getRedirectTo());
    }


    /**
     * db 사용자 인증
     * @param loginForm
     * @return
     */





}
