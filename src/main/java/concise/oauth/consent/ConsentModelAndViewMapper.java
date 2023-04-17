package concise.oauth.consent;

import lombok.NonNull;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ConsentModelAndViewMapper {

    public static ModelAndView map(@NonNull final ConsentResponse response) {
        if (response instanceof ConsentResponseSkip accepted) {
            return new ModelAndView(new RedirectView(accepted.redirectTo(), false));
        }
        if (response instanceof ConsentResponseRequiresUIDisplay uiRedirect) {
            return new ModelAndView("consent")
                    .addObject("consentChallenge", uiRedirect.consentChallenge())
                    .addObject("scopes", uiRedirect.requestedScopes());
        }
        if (response instanceof ConsentResponseAccepted accepted) {
            return new ModelAndView(new RedirectView(accepted.redirectTo(), false));
        }

        throw new IllegalStateException("Unknown response type: " + response.getClass());
    }

}
