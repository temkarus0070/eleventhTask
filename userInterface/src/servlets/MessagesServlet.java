package servlets;

import chatApp.domain.User;
import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Message;
import chatApp.domain.chat.Nameable;
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
        chatService=new ChatServiceImpl();
        persistenceChatService=new PersistenceChatServiceImpl();
        persistenceNameableChatService=new PersistenceNameableChatServiceImpl();
        persistenceUserService=new PersistenceUserServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String[]> params=req.getParameterMap();
        try {
            int id=Integer.parseInt(params.get("chatId")[0]);
            Optional<Chat> chat=persistenceChatService.getChat(id);
            String messageText="";
            try {
                messageText = params.get("message")[0];
            }
            catch (Exception ex){
                resp.getOutputStream().print("message text not found exception");
            }
            if(chat.isPresent()){
                req.setAttribute("chat",chat.get());
                Message message=new Message();
                message.setContent(messageText);
                Optional<User> userOptional=persistenceUserService.getUser(Arrays.stream(req.getCookies()).filter(e->e.getName().equals("username")).findFirst().get().getValue());
                if(userOptional.isPresent()) {
                    message.setSender(userOptional.get());
                    chatService.sendMessage(message, chat.get());
                }
                else{
                    resp.getOutputStream().print("user not found exception");
                }
            }
            else{
                resp.getOutputStream().print("chat not found exception");
            }
            String chatType=chat.get().getClass().getSimpleName();
            if(chatType.equals("PrivateChat"))
                resp.sendRedirect(String.format("/chat?chatType=%s&chatId=%d",chatType,chat.get().getId()));
            else
                resp.sendRedirect(String.format("/chat?chatType=%s&chatName=%s",chatType,((Nameable)chat.get()).getName()));
        }
        catch (Exception ex){
            resp.getOutputStream().print("chat id exception");
            return;
        }

    }
}
