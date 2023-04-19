package concise.oauth.login;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {
    @NonNull final LoginService loginService;

    @Value("${hydra.public-path}")
    private String hydraPublicPath;

    @GetMapping(value = "page")
    public ModelAndView checkClientAndReturnLoginPage(@RequestParam(name = "client_id") String client_id,
                                                      @RequestParam(name = "redirect_uri") String redirect_uri,
                                                      @RequestParam(name = "response_type") String response_type,
                                                      @RequestParam(name = "scope") String scope,
                                                      @RequestParam(name = "state") String state) {
        val loginModelAndView = new ModelAndView(new RedirectView(hydraPublicPath + "/oauth2/auth"));
//        loginModelAndView.setViewName("index");
        loginModelAndView.addObject("client_idd", client_id);
        loginModelAndView.addObject("redirect_uri", redirect_uri);
        loginModelAndView.addObject("response_type", response_type);
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
