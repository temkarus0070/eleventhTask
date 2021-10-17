package servlets;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.PrivateChat;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.ChatServiceImpl;
import chatApp.services.persistence.InMemoryChatStorage;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class HomePageServlet extends HttpServlet {
    private PersistenceRoomChatServiceImpl roomChatService;
    private PersistenceGroupChatServiceImpl groupChatService;
    private PersistencePrivateChatServiceImpl persistencePrivateChatService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        roomChatService=new PersistenceRoomChatServiceImpl(InMemoryChatStorage.getInstance());
        groupChatService=new PersistenceGroupChatServiceImpl(InMemoryChatStorage.getInstance());
        persistencePrivateChatService=new PersistencePrivateChatServiceImpl(InMemoryChatStorage.getInstance());
        authService=new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()),new PasswordEncoderImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("name","artyom");
        try {
            Collection<PrivateChat> collection = persistencePrivateChatService.get();
            String type = req.getParameter("chatType");
            ChatType chatType = null;
            if (type != null)
                chatType = ChatType.valueOf(type);
            switch (chatType) {
                case ROOM:
                    req.setAttribute("chats", roomChatService.get());
                    break;
                case GROUP:
                    req.setAttribute("chats", groupChatService.get());
                    break;
                default:
                    req.setAttribute("chats", persistencePrivateChatService.get());
                    break;
            }
            req.setAttribute("chatType", chatType.name());
            getServletContext().getRequestDispatcher("/jsp/home.jsp").forward(req, resp);
        }
        catch (Exception ex){
            resp.getOutputStream().print(ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
