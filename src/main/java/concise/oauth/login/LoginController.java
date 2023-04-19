package concise.oauth.login;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {
    @NonNull final LoginService loginService;

    @Value("${hydra.public-path}")
    private String hydraPublicPath;

    @GetMapping(value = "page")
    public ModelAndView checkClientAndReturnLoginPage(@RequestParam(name = "client_id") String clientId,
                                                      @RequestParam(name = "redirect_uri") String redirectUri,
                                                      @RequestParam(name = "response_type") String responseType,
                                                      @RequestParam(name = "scope") String scope,
                                                      @RequestParam(name = "state") String state) {
        val loginModelAndView = new ModelAndView();
        loginModelAndView.setViewName("index");
        loginModelAndView.addObject("clientId", clientId);
        loginModelAndView.addObject("redirectUri", redirectUri);
        loginModelAndView.addObject("responseType", responseType);
        loginModelAndView.addObject("scope", scope);
        loginModelAndView.addObject("state", state);

        loginModelAndView.addObject("hydraPublicPath", hydraPublicPath);
        return loginModelAndView;
    }

     @GetMapping
    public ModelAndView fetchesLoginInfo(@RequestParam("login_challenge") String loginChallenge) {
        LoginResult loginResult = loginService.processInitialLoginRequest(loginChallenge);
        return LoginModelAndViewMapper.toView(loginResult, loginChallenge);
    }

    @PostMapping("usernamePassword")
    public ModelAndView loginWithUsernamePasswordForm(LoginForm loginForm) {
        LoginResult loginResult = loginService.processSubmittedLoginRequest(loginForm.loginChallenge(), loginForm);
        return LoginModelAndViewMapper.toView(loginResult, loginForm.loginChallenge());
    }

    @GetMapping("logout")
    public String sessionLogout(@RequestParam("logout_challenge") String logoutChallenge) {
         return logoutChallenge;
    }
}
