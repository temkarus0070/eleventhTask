package servlets;

import chatApp.domain.chat.*;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.ChatServiceSelector;
import chatApp.strategies.PersistenceAddChatStrategy;
import chatApp.strategies.PersistenceGetChatStrategy;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.ChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceNameableChatService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ChatServlet extends HttpServlet {
    private PersistenceGetChatStrategy persistenceGetStrategy;
    private PersistenceAddChatStrategy persistenceAddChatStrategy;

    @Override
    public void init() throws ServletException {
        persistenceAddChatStrategy=new PersistenceAddChatStrategy();
        persistenceGetStrategy=new PersistenceGetChatStrategy();
    }




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Chat> chat = Optional.empty();
        try {
            Function<Map<String, String[]>, Optional<Chat>> getFunction = persistenceGetStrategy.takeGetMethod(req.getParameterMap());
            chat = getFunction.apply(req.getParameterMap());
        } catch (ClassNotFoundException classNotFoundException) {
            resp.getOutputStream().print("invalid chat type");
        }
        chat.ifPresent(value -> req.setAttribute("chat", value));
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameters = req.getParameterMap();
        Chat chat=persistenceAddChatStrategy.getAddMethod(parameters).apply(parameters);
        PersistenceChatService persistenceChatService = null;
        try {
            persistenceChatService = persistenceGetStrategy.takeGetMethod(parameters);
        } catch (ClassNotFoundException exception) {
            resp.getOutputStream().print("invalid chat type");
        }
        String chatType = parameters.get("chatType")[0];
        Chat chat = null;

        switch (chatType) {
            case "PrivateChat":
                chat = new PrivateChat();
                persistenceChatService.addChat(chat);
                break;
            case "RoomChat":
                chat = new RoomChat();
                ((RoomChat) chat).setName(chatName);
                persistenceChatService.addChat(chat);
                break;
            case "GroupChat":
                chat = new GroupChat();
                ((GroupChat) chat).setName(chatName);
                try {
                    ((GroupChat) chat).setUsersCount(Integer.parseInt(parameters.get("usersCount")[0]));
                } catch (Exception ex) {
                    resp.getOutputStream().print("users count is required");
                    return;
                }
                persistenceChatService.addChat(chat);
                break;
        }
        if (chat != null) {
            req.setAttribute("chat", chat);
            req.setAttribute("chatType", chatType);
        }
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);
    }
}
