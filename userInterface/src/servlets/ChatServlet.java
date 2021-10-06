package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.*;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.factories.ChatServiceFactory;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.PasswordEncoderImpl;
import chatApp.services.chat.ChatService;
import chatApp.services.persistence.InMemoryChatStorage;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.implementation.PersistenceGroupChatServiceImpl;
import chatApp.services.persistence.implementation.PersistencePrivateChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceRoomChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ChatServlet extends HttpServlet {
    private PersistencePrivateChatServiceImpl persistencePrivateChatService;
    private PersistenceGroupChatServiceImpl persistenceGroupChatService;
    private PersistenceRoomChatServiceImpl persistenceRoomChatService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        persistencePrivateChatService=new PersistencePrivateChatServiceImpl(InMemoryChatStorage.getInstance());
        persistenceGroupChatService=new PersistenceGroupChatServiceImpl(InMemoryChatStorage.getInstance());
        persistenceRoomChatService=new PersistenceRoomChatServiceImpl(InMemoryChatStorage.getInstance());
        authService=new AuthServiceImpl(new PersistenceUserServiceImpl(InMemoryUserStorage.getInstance()),new PasswordEncoderImpl());
    }




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String chatName;
        Chat anyChat=null;
        try {
            ChatType type = ChatType.valueOf(req.getParameterMap().get("chatType")[0]);
            switch (type) {
                case PRIVATE:
                    Optional<PrivateChat> chat;
                    int id = Integer.parseInt(req.getParameterMap().get("chatId")[0]);
                    chat = persistencePrivateChatService.getChat(id);
                    anyChat=chat.get();
                    break;
                case GROUP:
                    chatName = req.getParameterMap().get("chatName")[0];
                    Optional<GroupChat> groupChat= persistenceGroupChatService.getChatByName(chatName);
                    anyChat=groupChat.get();
                    break;
                case ROOM:
                    chatName = req.getParameterMap().get("chatName")[0];
                    Optional<RoomChat> roomChat= persistenceRoomChatService.getChatByName(chatName);
                    anyChat=roomChat.get();
                    break;
            }
            ChatService chatService= ChatServiceFactory.create(type);
            User current= authService.getCurrentUser(req.getCookies());
            if(anyChat!=null && chatService.hasPermissions(current,anyChat) ){
                req.setAttribute("chat",anyChat);
            }
            else {
                resp.getOutputStream().print("chat not found exception");
                return;
            }
        } catch (Exception exception) {
            resp.getOutputStream().print("invalid chat identifier or name");
            return;
        }
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameters=req.getParameterMap();
        ChatType chatType=ChatType.valueOf(parameters.get("chatType")[0]);
        String chatName="";
        try {
            chatName=parameters.get("chatName")[0];
        }
        catch (Exception ex){
            if(chatType.equals("roomChat")||chatType.equals("groupChat")){
                resp.getOutputStream().print("chat name is required");
                return;
            }
        }
        Chat anyChat=null;
        switch (chatType){
            case PRIVATE:
                PrivateChat privateChat=new PrivateChat();
                persistencePrivateChatService.addChat(privateChat);
                anyChat=privateChat;
                break;
            case ROOM:
                RoomChat roomChat=new RoomChat();
                roomChat.setName(chatName);
                try {
                    persistenceRoomChatService.addChat(roomChat);
                } catch (ChatAlreadyExistsException e) {
                    resp.getOutputStream().print("chat with that name  already exists");
                    return;
                }
                anyChat=roomChat;
                break;
            case GROUP:

                GroupChat groupChat=new GroupChat();
                groupChat.setName(chatName);
                try {
                    groupChat.setUsersCount(Integer.parseInt(parameters.get("usersCount")[0]));
                }
                catch (Exception ex){
                    resp.getOutputStream().print("users count is required");
                    return;
                }
                try {
                    persistenceGroupChatService.addChat(groupChat);
                } catch (ChatAlreadyExistsException e) {
                    resp.getOutputStream().print("chat with that name  already exists");
                    return;
                }
                anyChat=groupChat;
                break;
        }
        if(anyChat!=null) {
            try {
                ChatService chatService = ChatServiceFactory.create(chatType);
                User current = authService.getCurrentUser(req.getCookies());
            }
            catch (Exception ex){
                resp.getOutputStream().print("chat type not found exception");
            }
            req.setAttribute("chat", anyChat);
            req.setAttribute("chatType",chatType);
        }
        else {
            resp.getOutputStream().print("chat creation error");
            return;
        }
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req,resp);






    }
}
