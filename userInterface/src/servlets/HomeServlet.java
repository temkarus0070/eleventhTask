package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {
    private final String pagePath="WEB-INF/jsp/home.jsp";
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("my kek","kek");
        RequestDispatcher requestDispatcher=req.getRequestDispatcher(pagePath);
        requestDispatcher.forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("my kek","BIG kek");
        RequestDispatcher requestDispatcher=req.getRequestDispatcher(pagePath);
        requestDispatcher.forward(req,resp);
    }
}
