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
        try {
            Chat chat=persistenceAddChatStrategy.getAddMethod(parameters).apply(parameters);
            if (chat != null) {
                req.setAttribute("chat", chat);
                req.setAttribute("chatType", chat.getType());
            }
            req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);
        } catch (ClassNotFoundException e) {
            resp.getOutputStream().print(e.getMessage());
        }

    }
}
