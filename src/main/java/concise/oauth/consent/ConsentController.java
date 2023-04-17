package concise.oauth.consent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("consent")
@RequiredArgsConstructor
public class ConsentController {

    @NonNull final ConsentService consentService;

    @GetMapping
    public ModelAndView consentEndpoint(@RequestParam("consent_challenge") final String consentChallenge) {
        ConsentResponse consentResponse = consentService.processInitialConsentRequest(consentChallenge);


        return ConsentModelAndViewMapper.map(consentResponse);

    }

    @PostMapping
    public ModelAndView submitConsentForm(final ConsentForm consentForm) {
        ConsentResponse consentResponse = consentService.processConsentForm(consentForm);

        return ConsentModelAndViewMapper.map(consentResponse);
    }


}
