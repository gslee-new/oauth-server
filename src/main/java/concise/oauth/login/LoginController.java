package concise.oauth.login;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {
    @NonNull final LoginService loginService;

    @GetMapping("page")
    public ModelAndView checkClientAndReturnLoginPage() {
        val loginModleAndView = new ModelAndView();
        loginModleAndView.setViewName("/index");
        return loginModleAndView;
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
