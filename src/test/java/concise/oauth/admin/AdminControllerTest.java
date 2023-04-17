package concise.oauth.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 클라이언트생성테스트() throws Exception {
        String clientName = "oauth-client";
        String clientSecret = "k0d9391382acd";
        String clientDomain = "http://localhost:8081";
        String oauthDomain = "http://localhost:8080";
        List<String> redirectUris = List.of(clientDomain + "/token/code", clientDomain + "/token/logout" , oauthDomain + "/logout");
        List<String> postLogoutRedirectUris = List.of(oauthDomain + "/logout",clientDomain + "/token/logout");
        List<String> grantType = List.of("authorization_code","refresh_token", "client_credentials", "implicit");
        List<String> responseTypes = List.of("code");
        List<String> scopes = List.of("offline", "openid");

        String postBody = objectMapper.writeValueAsString(ClientsForm.builder()
                .clientName(clientName)
                .clientSecret(clientSecret)
                .redirectUris(redirectUris)
                .grantTypes(grantType)
                .responseTypes(responseTypes)
                .scopes(scopes)
                .postLogoutRedirectUris(postLogoutRedirectUris)
                .build());

        mockMvc.perform(post("/admin/client")
                .content(postBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}