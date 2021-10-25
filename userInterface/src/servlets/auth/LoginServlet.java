package servlets.auth;

import chatApp.domain.User;
import chatApp.domain.exceptions.InvalidAuthDataException;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private AuthService authService;
    private PersistenceUserService persistenceUserService;
    @Override
    public void init() throws ServletException {
        authService=new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()),new PasswordEncoderImpl());
        persistenceUserService=new PersistenceUserServiceImpl(new UserStorage());
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username="";
        String password="";
        Map<String,String[]> params=req.getParameterMap();
        try{
            username=params.get("username")[0];
            password=params.get("password")[0];
            try {
                User user = authService.login(username, password);
                Cookie usernameCookie=new Cookie("username",user.getName());
                usernameCookie.setMaxAge(999999);
                Cookie passwordCookie=new Cookie("password",user.getPassword());
                passwordCookie.setMaxAge(999999);

               resp.addCookie(usernameCookie);
                resp.addCookie(passwordCookie);

                req.getRequestDispatcher("/jsp/home.jsp").forward(req,resp);
            }
            catch (InvalidAuthDataException invalidAuthDataException){
                resp.getOutputStream().print("invalid auth data");
            }

        }
        catch (Exception ex){
            resp.getOutputStream().print("auth exception");
        }
    }
}
