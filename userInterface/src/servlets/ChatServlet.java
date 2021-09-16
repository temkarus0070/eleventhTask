package servlets;

import chatApp.domain.chat.*;
import chatApp.services.AuthService;
import chatApp.services.AuthServiceImpl;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.ChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceNameableChatService;
import chatApp.services.persistence.implementation.PersistenceNameableChatServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
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
        AuthService authService=new AuthServiceImpl();
        try {
            authService.register("artyomsin", "1234");
        }
        catch (Exception ex){

        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String type = req.getParameterMap().get("chatType")[0];
            Optional<Chat> chat;
            switch (type) {
                case "PrivateChat":
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameters=req.getParameterMap();
        String chatType=parameters.get("chatType")[0];
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
