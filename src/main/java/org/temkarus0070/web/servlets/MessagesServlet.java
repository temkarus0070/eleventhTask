package org.temkarus0070.web.servlets;

import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.services.persistence.ChatStorage;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.implementation.PersistenceUserServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class MessagesServlet extends HttpServlet {
    private PersistenceChatServiceImpl persistenceChatService;
    private PersistenceUserService persistenceUserService;
    private ChatRepository repository;

    @Override
    public void init() throws ServletException {
        persistenceUserService = new PersistenceUserServiceImpl(new UserStorage());
        repository = new ChatStorage(ChatType.ROOM);
        persistenceChatService = new PersistenceChatServiceImpl(new ChatStorage(ChatType.ANY));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChatType chatType = null;
        Optional<Chat> chatOptional = Optional.empty();
        Map<String, String[]> params = req.getParameterMap();
        try {
            chatType = ChatType.valueOf(params.get("chatType")[0]);
            int id = Integer.parseInt(params.get("chatId")[0]);

            if (chatType != null) {
                Optional<User> userOptional = persistenceUserService.getUser(Arrays.stream(req.getCookies()).filter(e -> e.getName().equals("username")).findFirst().get().getValue());
                if (userOptional.isPresent()) {
                    String messageText = params.get("message")[0];
                    chatOptional = persistenceChatService.getChat(id);
                    if (chatOptional.isPresent()) {
                        Message message = new Message(messageText, userOptional.get());
                        persistenceChatService.addMessage(message, chatOptional.get().getId());
                        req.setAttribute("chat", chatOptional.get());
                    }
                } else {
                    resp.getOutputStream().write("user not found exception".getBytes(StandardCharsets.UTF_8));
                }

            } else {
                resp.getOutputStream().write("chat not found exception".getBytes(StandardCharsets.UTF_8));
            }
            resp.sendRedirect(String.format("/chat?chatType=%s&chatId=%d", chatType, chatOptional.get().getId()));
        } catch (Exception ex) {
            MyLogger.log(Level.SEVERE, ex.getMessage());
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
        }

    }
}
