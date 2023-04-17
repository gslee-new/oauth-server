package concise.oauth.login;

import concise.oauth.hydra.HydraAdminClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sh.ory.hydra.model.OAuth2RedirectTo;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService {
    @NonNull
    final HydraAdminClient hydraAdminClient;

    public OAuth2RedirectTo acceptOAuth2LogoutRequest(String logoutChallenge) {
        return hydraAdminClient.acceptOAuth2LogoutRequest(logoutChallenge);
    }
}
