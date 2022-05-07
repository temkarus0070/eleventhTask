import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.temkarus0070.AppConfig;
import org.temkarus0070.WebConfig;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.security.WebSecurityConfig;
import org.temkarus0070.application.security.persistence.MyUserDetailsManager;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistenceUserServiceImpl;

import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {WebConfig.class, AppConfig.class, WebSecurityConfig.class})
@WebAppConfiguration
public class TestSecurity {
    @Autowired
    private GenericWebApplicationContext context;
    private MockMvc mvc;
    private UserStorage userStorage = Mockito.mock(UserStorage.class);
    private User newUser = null;

    @BeforeEach
    public void setup() {
        AutowireCapableBeanFactory bf = context.getAutowireCapableBeanFactory();
        MyUserDetailsManager bean2 = bf.getBean(MyUserDetailsManager.class);
        bean2.setUserStorage(userStorage);
        PersistenceUserServiceImpl bean = bf.getBean(PersistenceUserServiceImpl.class);
        bean.setUserRepository(userStorage);
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity())
                .build();

        User user = new User("artyomsin007", "$2a$10$s6gL0T0u4.c6lkNN5HD9K.mFdpX85.BiVrY57jX/MiUPuZAT8H/Oa", "ROLE_ADMIN");
        Mockito.when(userStorage.get("artyomsin007"))
                .thenReturn(user);
        User newUser1 = new User("art", "1234", "USER");
        doAnswer(invocationOnMock -> {
            Object argument = invocationOnMock.getArgument(0);
            newUser = (User) argument;
            return null;
        }).when(userStorage).add(newUser1);
        Mockito.when(userStorage.get("artyom")).thenReturn(null);
        Mockito.when(userStorage.get("art")).thenReturn(null);
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
        mvc.perform(formLogin().user("artyom").password("1234")).andExpect(unauthenticated());
    }

    @Test
    void testRegistrationAndLoginSuccess() throws Exception {
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("username", "art");
        map.add("password", "1234");
        mvc.perform(formLogin().user("art").password("1234")).andExpect(unauthenticated()).andReturn();
        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders.post("/register").params(map)).andReturn();
        Mockito.when(userStorage.get("art")).thenReturn(newUser);
        mvc.perform(formLogin().user("art").password("1234")).andExpect(authenticated().withUsername("art")).andReturn();
    }

}
