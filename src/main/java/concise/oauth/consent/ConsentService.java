package concise.oauth.consent;

import concise.oauth.hydra.HydraAdminClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sh.ory.hydra.model.OAuth2ConsentRequest;
import sh.ory.hydra.model.OAuth2RedirectTo;

@Service
@RequiredArgsConstructor
public class ConsentService {

    @NonNull final HydraAdminClient hydraAdminClient;

    public ConsentResponse processInitialConsentRequest(@NonNull final String consentChallenge) {
        OAuth2ConsentRequest consentRequest = hydraAdminClient.getConsentRequest(consentChallenge);

        /* 사용자 동의를 받지 않기에 true 고정
           향후 로직이 변경된다면 always 변수 제거
        */
        boolean always =  true;
        if (always || Boolean.TRUE.equals(consentRequest.getSkip())) {
            AcceptConsentRequest acceptConsentRequest = AcceptConsentRequest.builder()
                    .consentChallenge(consentChallenge)
                    .remember(true)
                    .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                    .scopes(consentRequest.getRequestedScope())
                    .build();
            OAuth2RedirectTo oAuth2RedirectTo = hydraAdminClient.acceptConsentRequest(acceptConsentRequest);
            return new ConsentResponseSkip(oAuth2RedirectTo.getRedirectTo());
        }

        return new ConsentResponseRequiresUIDisplay(consentRequest.getRequestedScope(), consentChallenge);
    }

    public ConsentResponse processConsentForm(@NonNull final ConsentForm consentForm) {
        String consentChallenge = consentForm.getConsentChallenge();
        OAuth2ConsentRequest consentRequest = hydraAdminClient.getConsentRequest(consentChallenge);

        AcceptConsentRequest acceptConsentRequest = AcceptConsentRequest.builder()
                .consentChallenge(consentChallenge)
                .remember(consentForm.isRemember())
                .grantAccessTokenAudience(consentRequest.getRequestedAccessTokenAudience())
                .scopes(consentForm.getScopes())
                .build();

        OAuth2RedirectTo oAuth2RedirectTo = hydraAdminClient.acceptConsentRequest(acceptConsentRequest);

        return new ConsentResponseAccepted(oAuth2RedirectTo.getRedirectTo());
    }

}
