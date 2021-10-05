package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.Message;
import chatApp.factories.ChatServiceFactory;
import chatApp.factories.PersistenceChatServiceFactory;
import chatApp.services.chat.ChatService;
import chatApp.services.persistence.InMemoryChatStorage;
import chatApp.services.persistence.InMemoryUserStorage;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class MessagesServlet extends HttpServlet {
    private ChatService chatService;
    private PersistenceChatService persistenceChatService;
    private PersistenceUserService persistenceUserService;

    @Override
    public void init() throws ServletException {
        persistenceUserService=new PersistenceUserServiceImpl(InMemoryUserStorage.getInstance());
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
                   persistenceChatService= PersistenceChatServiceFactory.create(chatType, InMemoryChatStorage.getInstance());
                   Optional<Chat> chatOptional=persistenceChatService.getChat(id);
                   ChatService chatService= ChatServiceFactory.create(chatOptional.get().getType());
                   req.setAttribute("chat",chatOptional.get());
                   Message message=new Message(messageText, userOptional.get());
                   chatService.sendMessage(message,chatOptional.get());
                   persistenceChatService.updateChat(chatOptional.get());
                   chat=chatOptional.get();
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
            resp.getOutputStream().print("chat id exception");
        }

    }
}
