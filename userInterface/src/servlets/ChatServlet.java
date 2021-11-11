package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.*;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.factories.ChatServiceFactory;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.InMemoryChatStorage;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class ChatServlet extends HttpServlet {
    private PersistencePrivateChatServiceImpl persistencePrivateChatService;
    private PersistenceGroupChatServiceImpl persistenceGroupChatService;
    private PersistenceRoomChatServiceImpl persistenceRoomChatService;
    private PersistenceUserService persistenceUserService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        persistencePrivateChatService = new PersistencePrivateChatServiceImpl(new ChatStorage(ChatType.PRIVATE));
        persistenceGroupChatService = new PersistenceGroupChatServiceImpl(new ChatStorage(ChatType.GROUP));
        persistenceRoomChatService = new PersistenceRoomChatServiceImpl(new ChatStorage(ChatType.ROOM));
        authService = new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()), new PasswordEncoderImpl());
        persistenceUserService = new PersistenceUserServiceImpl(new UserStorage());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String chatName;
        Chat anyChat = null;
        int id = 0;
        try {
            ChatType type = ChatType.valueOf(req.getParameterMap().get("chatType")[0]);
            if (req.getParameterMap().get("chatId") != null)
                id = Integer.parseInt(req.getParameterMap().get("chatId")[0]);
            switch (type) {
                case PRIVATE:
                    Optional<PrivateChat> chat = Optional.empty();

                    chat = persistencePrivateChatService.getChat(id);
                    anyChat = chat.get();
                    break;
                case GROUP:
                    Optional<GroupChat> groupChat = Optional.empty();
                    if (req.getParameterMap().get("chatName") != null) {
                        chatName = req.getParameterMap().get("chatName")[0];
                        if (chatName != null)
                            groupChat = persistenceGroupChatService.getChatByName(chatName);
                    } else
                        groupChat = persistenceGroupChatService.getChat(id);
                    anyChat = groupChat.get();
                    break;
                case ROOM:
                    Optional<RoomChat> roomChat = Optional.empty();
                    if (req.getParameterMap().get("chatName") != null) {
                        chatName = req.getParameterMap().get("chatName")[0];
                        if (chatName != null)
                            roomChat = persistenceRoomChatService.getChatByName(chatName);
                    } else
                        roomChat = persistenceRoomChatService.getChat(id);
                    anyChat = roomChat.get();
                    break;
            }
            User current = authService.getCurrentUser(req.getCookies());
            if (anyChat != null && hasPermissions(current, anyChat)) {
                req.setAttribute("chat", anyChat);
                req.setAttribute("users", persistenceUserService.getUsersNotAtThatChat(anyChat.getId()));
            } else {
                resp.getOutputStream().write(" you dont have permissions to participate at that chat or chat not found".getBytes(StandardCharsets.UTF_8));
                return;
            }
        } catch (Exception exception) {
            resp.getOutputStream().write(exception.getMessage().getBytes(StandardCharsets.UTF_8));
            return;
        }
        getServletContext().getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);

    }

    private boolean hasPermissions(User user, Chat chat) {
        return chat.getUserList().contains(user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameters = req.getParameterMap();
        ChatType chatType = ChatType.valueOf(parameters.get("chatType")[0]);
        User current = null;
        try {
            current = authService.getCurrentUser(req.getCookies());
        } catch (Exception ex) {
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
            return;
        }
        String chatName = "";
        try {
            chatName = parameters.get("chatName")[0];
        } catch (Exception ex) {
            if (chatType == ChatType.ROOM || chatType == ChatType.GROUP) {
                resp.getOutputStream().write("chat name is required".getBytes(StandardCharsets.UTF_8));
                return;
            }
        }
        Chat anyChat = null;
        try {
            switch (chatType) {
                case PRIVATE:
                    PrivateChat privateChat = new PrivateChat();
                    privateChat.setChatOwner(current);
                    persistencePrivateChatService.addChat(privateChat);
                    anyChat = privateChat;
                    break;
                case ROOM:
                    RoomChat roomChat = new RoomChat();
                    roomChat.setName(chatName);
                    roomChat.setChatOwner(current);
                    persistenceRoomChatService.addChat(roomChat);
                    anyChat = roomChat;
                    break;
                case GROUP:

                    GroupChat groupChat = new GroupChat();
                    groupChat.setName(chatName);
                    groupChat.setChatOwner(current);
                    try {
                        groupChat.setUsersCount(Integer.parseInt(parameters.get("usersCount")[0]));
                    } catch (Exception ex) {
                        resp.getOutputStream().write("users count is required".getBytes(StandardCharsets.UTF_8));
                        return;
                    }
                    persistenceGroupChatService.addChat(groupChat);
                    anyChat = groupChat;
                    break;
                default:
                    resp.getOutputStream().write("chat type not found exception".getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
            resp.getOutputStream().write(ex.getMessage().getBytes(StandardCharsets.UTF_8));
            return;
        }
        req.setAttribute("chat", anyChat);
        req.setAttribute("chatType", chatType);
        req.setAttribute("id", anyChat.getId());
        resp.sendRedirect(String.format("../chat?chatType=%s&chatId=%d", chatType, anyChat.getId()));
    }
}
