package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.ChatType;
import chatApp.factories.PersistenceChatServiceFactory;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UserServlet extends HttpServlet {
    PersistenceChatService persistenceChatService;
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String username = req.getParameter("username");
            Integer chatId = Integer.parseInt(req.getParameter("chatId"));
            String chatType=req.getParameter("chatType");
            persistenceChatService= PersistenceChatServiceFactory.create(ChatType.valueOf(chatType),new ChatStorage());
            persistenceChatService.addUser(username, chatId);
            resp.sendRedirect(String.format("../chat?chatType=%s&chatId=%s",chatType,chatId));
        }
        catch (Exception ex){
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
