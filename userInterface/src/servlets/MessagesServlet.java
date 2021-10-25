package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.RoomChat;
import chatApp.domain.exceptions.MessageSenderNotFoundException;
import chatApp.factories.ChatServiceFactory;
import chatApp.factories.PersistenceChatServiceFactory;
import chatApp.services.persistence.ChatStorage;
import chatApp.services.persistence.InMemoryChatStorage;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.UserStorage;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.interfaces.PersistenceUserService;
import chatApp.services.persistence.interfaces.Repository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class MessagesServlet extends HttpServlet {
    private PersistenceChatServiceImpl persistenceChatService;
    private PersistenceUserService persistenceUserService;
    private ChatRepository repository;

    @Override
    public void init() throws ServletException {
        persistenceUserService=new PersistenceUserServiceImpl(new UserStorage());
        repository=new ChatStorage(ChatType.ROOM);
        persistenceChatService=new PersistenceChatServiceImpl(new ChatStorage());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Chat chat=null;
        ChatType chatType=null;
        Map<String,String[]> params=req.getParameterMap();
        try {
            chatType=ChatType.valueOf(params.get("chatType")[0]);
            int id=Integer.parseInt(params.get("chatId")[0]);

            if(chatType!=null){
                Optional<User> userOptional=persistenceUserService.getUser(Arrays.stream(req.getCookies()).filter(e->e.getName().equals("username")).findFirst().get().getValue());
               if(userOptional.isPresent()){
                   String messageText = params.get("message")[0];
                   Optional<Chat> chatOptional=persistenceChatService.getChat(id);
                   if(chatOptional.isPresent()) {
                       chat=chatOptional.get();
                       Message message = new Message(messageText, userOptional.get());
                       persistenceChatService.addMessage(message,chat.getId());
                       req.setAttribute("chat", chat);
                       chat = chatOptional.get();
                   }
               }
               else{
                   resp.getOutputStream().print("user not found exception");
               }

            }
            else {
                resp.getOutputStream().print("chat not found exception");
            }
                resp.sendRedirect(String.format("/chat?chatType=%s&chatId=%d",chatType,chat.getId()));
        }
        catch (Exception ex){
            resp.getOutputStream().print(ex.getMessage());
        }

    }
}
