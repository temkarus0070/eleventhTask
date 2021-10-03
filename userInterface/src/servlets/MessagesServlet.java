package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.factories.ChatServiceFactory;
import chatApp.factories.PersistenceChatServiceFactory;
import chatApp.services.chat.ChatService;
import chatApp.services.chat.ChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceChatServiceImpl;
import chatApp.services.persistence.implementation.PersistenceUserServiceImpl;
import chatApp.services.persistence.interfaces.PersistenceChatService;
import chatApp.services.persistence.interfaces.PersistenceNameableChatService;
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
    private PersistenceNameableChatService persistenceNameableChatService;
    private PersistenceUserService persistenceUserService;

    @Override
    public void init() throws ServletException {
        persistenceUserService=new PersistenceUserServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String[]> params=req.getParameterMap();
        try {
            int id=Integer.parseInt(params.get("chatId")[0]);
            Optional<Chat> chat=persistenceChatService.getChat(id);
            if(chat.isPresent()){
                Optional<User> userOptional=persistenceUserService.getUser(Arrays.stream(req.getCookies()).filter(e->e.getName().equals("username")).findFirst().get().getValue());
               if(userOptional.isPresent()){
                   String messageText = params.get("message")[0];
                   persistenceChatService=PersistenceChatServiceFactory.create(chat.get().getType());
                   ChatService chatService= ChatServiceFactory.create(chat.get().getType());
                   req.setAttribute("chat",chat.get());
                   Message message=new Message(messageText, userOptional.get());
                   chatService.sendMessage(message,chat.get());
                   persistenceChatService.updateChat(chat.get());
               }
               else{
                   resp.getOutputStream().print("user not found exception");
               }

            }
            else {
                resp.getOutputStream().print("chat not found exception");
            }
            String chatType=chat.get().getClass().getSimpleName();
                resp.sendRedirect(String.format("/chat?chatType=%s&chatId=%d",chatType,chat.get().getId()));
        }
        catch (Exception ex){
            resp.getOutputStream().print("chat id exception");
        }

    }
}
