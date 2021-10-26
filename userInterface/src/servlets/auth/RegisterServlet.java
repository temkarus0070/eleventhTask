package servlets.auth;

import chatApp.domain.User;
import chatApp.domain.exceptions.UsernameAlreadyExistException;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService=new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()),new PasswordEncoderImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try{
        String username=req.getParameterMap().get("username")[0];
        String password=req.getParameterMap().get("password")[0];
        User user=authService.register(username,password);
        resp.addCookie(new Cookie("username",user.getName()));
        resp.addCookie(new Cookie("password",user.getPassword()));
        req.getRequestDispatcher("/jsp/home.jsp").forward(req,resp);
    } catch (Exception usernameAlreadyExistException){
        resp.getOutputStream().print(usernameAlreadyExistException.getMessage());

    }
    }
}
