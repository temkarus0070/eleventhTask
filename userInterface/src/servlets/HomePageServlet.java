package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.PrivateChat;
import chatApp.factories.ChatServiceFactory;
import chatApp.factories.PersistenceChatServiceFactory;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.InMemoryChatStorage;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.*;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }

}
