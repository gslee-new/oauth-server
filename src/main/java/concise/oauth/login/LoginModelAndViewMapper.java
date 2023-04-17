package concise.oauth.login;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@UtilityClass
public class LoginModelAndViewMapper {

    public static ModelAndView toView(@NonNull final LoginResult loginResult,
                                      @NonNull final String loginChallenge) {
        if (loginResult instanceof LoginAcceptedFollowRedirect acceptedFollowRedirect) {
            return new ModelAndView(new RedirectView(acceptedFollowRedirect.redirectUrl(), false));
        }

        if (loginResult instanceof LoginDeniedInvalidCredentials) {
            val loginModelAndView = new ModelAndView();
            loginModelAndView.setViewName("/login");
            loginModelAndView.addObject("loginChallenge", loginChallenge);
            loginModelAndView.addObject("error", "invalid credentials try again");

            return loginModelAndView;
        }

        if (loginResult instanceof LoginNotSkippableDisplayLoginUI) {
            val loginModelAndView = new ModelAndView();
            loginModelAndView.setViewName("/login");
            loginModelAndView.addObject("loginChallenge", loginChallenge);

            return loginModelAndView;
        }

        if (loginResult instanceof LoginRequestNotFound) {
            return new ModelAndView("/home");
        }

        throw new IllegalStateException("Unknown response type: " + loginResult.getClass());
    }

}
