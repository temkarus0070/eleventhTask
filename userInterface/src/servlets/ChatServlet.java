package servlets;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.PrivateChat;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.ChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceNameableChatService;
import chatApp.services.persistence.implementation.PersistenceNameableChatServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ChatServlet extends HttpServlet {

    private ChatService chatService;
    private PersistenceChatService persistenceChatService;
    private PersistenceNameableChatService persistenceNameableChatService;

    @Override
    public void init() throws ServletException {
        this.chatService = new ChatServiceImpl();
        this.persistenceChatService = new PersistenceChatServiceImpl();
        this.persistenceNameableChatService = new PersistenceNameableChatServiceImpl();
        preLoad();
    }

    private void preLoad() {
        GroupChat groupChat = new GroupChat();
        groupChat.setName("myChat");

        PrivateChat privateChat = new PrivateChat();
        privateChat.setId(0);
        Message message = new Message();

        message.setContent("hello world");
        try {
            chatService.sendMessage(message, privateChat);
            chatService.sendMessage(message, groupChat);
        } catch (Exception EX) {

        }
        this.persistenceChatService.addChat(privateChat);
        this.persistenceNameableChatService.addChat(groupChat);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String type = req.getParameterMap().get("chatType")[0];
            Optional<Chat> chat;
            switch (type) {
                case "privateChat":
                    int id = Integer.parseInt(req.getParameterMap().get("chatId")[0]);
                    chat = persistenceChatService.getChat(id);
                    break;
                default:
                    String chatName = req.getParameterMap().get("chatName")[0];
                    chat = persistenceNameableChatService.getChatByName(chatName);
                    break;
            }
            chat.ifPresent(value -> req.setAttribute("chat", value));
        } catch (Exception exception) {
            resp.getOutputStream().print("invalid chat identifier or name");
        }
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);

    }
}
