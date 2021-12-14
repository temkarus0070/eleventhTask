package servlets;

import chatApp.domain.chat.ChatType;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ban"})
public class BanServlet extends HttpServlet {
    PersistenceChatService persistenceChatService = new PersistenceChatServiceImpl(new ChatStorage(ChatType.ANY));

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String chatType = req.getParameter("chatType");
        Integer chatId = Integer.parseInt(req.getParameter("chatId"));
        persistenceChatService.banUserInChat(username, chatId);
        resp.sendRedirect(String.format("../chat?chatType=%s&chatId=%s", chatType, chatId));
    }
}
