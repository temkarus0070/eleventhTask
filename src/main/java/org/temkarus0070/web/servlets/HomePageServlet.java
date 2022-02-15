package org.temkarus0070.web.servlets;

import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.factories.PersistenceChatServiceFactory;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.AuthServiceImpl;
import org.temkarus0070.application.services.PasswordEncoderImpl;
import org.temkarus0070.application.services.persistence.ChatStorage;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.*;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class HomePageServlet extends HttpServlet {
    private PersistenceRoomChatServiceImpl roomChatService;
    private PersistenceGroupChatServiceImpl groupChatService;
    private PersistencePrivateChatServiceImpl persistencePrivateChatService;
    private PersistenceChatServiceImpl persistenceChatService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {

        roomChatService = new PersistenceRoomChatServiceImpl(new ChatStorage(ChatType.ROOM));
        groupChatService = new PersistenceGroupChatServiceImpl(new ChatStorage(ChatType.GROUP));
        persistencePrivateChatService = new PersistencePrivateChatServiceImpl(new ChatStorage(ChatType.PRIVATE));
        authService = new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()), new PasswordEncoderImpl());
        persistenceChatService = new PersistenceChatServiceImpl(new ChatStorage(ChatType.ANY));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.getCurrentUser(req.getCookies());
            String type = req.getParameter("chatType");
            ChatType chatType = null;
            if (type == null) {
                chatType = ChatType.ANY;
            } else
                chatType = ChatType.valueOf(type);
            PersistenceChatService persistenceChatService = PersistenceChatServiceFactory.create(chatType, new ChatStorage(chatType));
            req.setAttribute("chats", persistenceChatService.getChatsByUserName(user.getName()));
            req.setAttribute("chatType", chatType.name());
            getServletContext().getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
        } catch (Exception ex) {
            MyLogger.log(Level.SEVERE, ex.getMessage());
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }

}
