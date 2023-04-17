package concise.oauth.login;

public record LoginForm(String loginEmail,
                        String loginPassword,
                        String loginChallenge,
                        boolean remember) {}
