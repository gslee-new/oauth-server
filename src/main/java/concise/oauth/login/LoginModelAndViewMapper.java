package concise.oauth.login;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@UtilityClass
@Slf4j
public class LoginModelAndViewMapper {

    public static ModelAndView toView(@NonNull final LoginResult loginResult,
                                      @NonNull final String loginChallenge) {
        log.info("111111111111111111");
        if (loginResult instanceof LoginAcceptedFollowRedirect acceptedFollowRedirect) {
            log.info("111111111111111111");
            return new ModelAndView(new RedirectView(acceptedFollowRedirect.redirectUrl(), false));
        }

        if (loginResult instanceof LoginDeniedInvalidCredentials) {
            log.info("222222222222222222222");
            val loginModelAndView = new ModelAndView();
            loginModelAndView.setViewName("login");
            loginModelAndView.addObject("loginChallenge", loginChallenge);
            loginModelAndView.addObject("error", "invalid credentials try again");

            return loginModelAndView;
        }

        if (loginResult instanceof LoginNotSkippableDisplayLoginUI) {
            log.info("33333333333333333333");
            val loginModelAndView = new ModelAndView();
            loginModelAndView.setViewName("login");
            loginModelAndView.addObject("loginChallenge", loginChallenge);

            return loginModelAndView;
        }

        if (loginResult instanceof LoginRequestNotFound) {
            log.info("444444444444444444");
            return new ModelAndView("home");
        }

        throw new IllegalStateException("Unknown response type: " + loginResult.getClass());
    }

}
