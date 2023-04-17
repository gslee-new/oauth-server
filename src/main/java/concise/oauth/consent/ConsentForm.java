package concise.oauth.consent;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConsentForm {
    private String submit;
    private String consentChallenge;
    private Boolean remember;
    private List<String> scopes;

    public boolean isRemember() {
        if (remember == null) {
            return false;
        }
        return true;
    }
}
