package concise.oauth.consent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
public class AcceptConsentRequest {
    private String consentChallenge;
    private boolean remember;
    private List<String> grantAccessTokenAudience;
    private List<String> scopes;
    private Map<String, Object> context;
}
