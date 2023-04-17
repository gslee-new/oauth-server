package concise.oauth.login;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import sh.ory.hydra.model.OAuth2RedirectTo;

@Slf4j
@RestController
@RequestMapping("logout")
@RequiredArgsConstructor
public class LogoutController {

    @NonNull final LogoutService logoutService;

    @GetMapping
    public ModelAndView sessionLogout(@RequestParam("logout_challenge") String logoutChallenge) {
        OAuth2RedirectTo oAuth2RedirectTo = logoutService.acceptOAuth2LogoutRequest(logoutChallenge);
        return new ModelAndView(new RedirectView(oAuth2RedirectTo.getRedirectTo(), false));
    }
}
