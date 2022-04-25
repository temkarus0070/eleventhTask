import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.temkarus0070.AppConfig;
import org.temkarus0070.WebConfig;
import org.temkarus0070.application.security.WebSecurityConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class, AppConfig.class, WebSecurityConfig.class})
@WebAppConfiguration
public class TestSecurity {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext homePageController;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(homePageController).apply(springSecurity())
                .build();
    }

    @Test
    void testRedirectIfNotAuthorized() throws Exception {
        var res = mvc.perform(MockMvcRequestBuilders.get("/index"));
        res.andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "artyomsin007", password = "1234", roles = {"USER"})
    void testAuthorize() throws Exception {
        MvcResult mvcResult = mvc.perform(formLogin().user("artyomsin007").password("1234")).andExpect(authenticated().withUsername("artyomsin007")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(302, status);
        mvc
                .perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(status().isOk());


    }

    @Test
    void testAuthenticationErrorWithInvalidLoginPassword() throws Exception {
        mvc.perform(formLogin().user("art").password("1234")).andExpect(unauthenticated());
    }

}
