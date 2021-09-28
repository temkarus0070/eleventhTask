package servlets;

import chatApp.domain.chat.*;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.ChatServiceSelector;
import chatApp.services.PersistenceChatServiceSelector;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.ChatServiceImpl;
import chatApp.services.chat.GroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
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

public class ChatServlet extends HttpServlet {
    private ChatServiceSelector chatServiceSelector;
    private PersistenceChatServiceSelector persistenceChatServiceSelector;

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
        AuthService authService=new AuthServiceImpl();
        try {
            authService.register("artyomsin", "1234");
        }
        catch (Exception ex){

        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PersistenceChatService persistenceChatService=null;
        try{
         persistenceChatService=persistenceChatServiceSelector.getPersistenceChatService(req.getParameterMap());
        }
        catch (ClassNotFoundException classNotFoundException){
            resp.getOutputStream().print("invalid chat type");
        }
        try {
            String type = req.getParameterMap().get("chatType")[0];
            Optional<Chat> chat=Optional.empty();

            switch (type) {
                case "GroupChat":
                    PersistenceGroupChatServiceImpl groupChatService=(PersistenceGroupChatServiceImpl) persistenceChatService;
                    chat=groupChatService.getChatByName(req.getParameterMap().get("chatName")[0]);
                    break;
                case "PrivateChat":
                    int id = Integer.parseInt(req.getParameterMap().get("chatId")[0]);
                    chat = persistenceChatService.getChat(id);
                    break;
                case "RoomChat":
                    PersistenceRoomChatServiceImpl persistenceRoomChatService=(PersistenceRoomChatServiceImpl) persistenceChatService;
                    chat=persistenceRoomChatService.getChatByName(req.getParameterMap().get("chatName")[0]);
                    break;
            }
            chat.ifPresent(value -> req.setAttribute("chat", value));
        } catch (Exception exception) {
            resp.getOutputStream().print("invalid chat identifier or name");
        }
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameters=req.getParameterMap();
        PersistenceChatService persistenceChatService=null;
        try{
            persistenceChatService=persistenceChatServiceSelector.getPersistenceChatService(parameters);
        }
        catch (ClassNotFoundException exception){
            resp.getOutputStream().print("invalid chat type");
        }
        String chatType=parameters.get("chatType")[0];
        Chat chat=null;
        switch (chatType){
            case "PrivateChat":
                chat=new PrivateChat();
                persistenceChatService.addChat(chat);
                break;
            case "RoomChat":
               chat=new RoomChat();
                ((RoomChat) chat).setName(chatName);
                persistenceChatService.addChat(chat);
                break;
            case "GroupChat":
                chat=new GroupChat();
                ((GroupChat) chat).setName(chatName);
                try {
                    ((GroupChat) chat).setUsersCount(Integer.parseInt(parameters.get("usersCount")[0]));
                }
                catch (Exception ex){
                    resp.getOutputStream().print("users count is required");
                    return;
                }
                persistenceChatService.addChat(chat);
                break;
        }
        if(chat!=null) {
            req.setAttribute("chat", chat);
            req.setAttribute("chatType",chatType);
        }
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req,resp);
    }
}
