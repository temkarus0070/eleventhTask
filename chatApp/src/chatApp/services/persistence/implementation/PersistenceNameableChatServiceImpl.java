package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.Nameable;
import chatApp.services.persistence.interfaces.PersistenceNameableChatService;

import java.util.Optional;

public class PersistenceNameableChatServiceImpl extends PersistenceChatServiceImpl implements PersistenceNameableChatService {

    @Override
    public Optional<Chat> getChatByName(String name) {
        return mockGetChatByName(name);
    }


    private Optional<Chat> mockGetChatByName(String name){
        return get().stream().filter(e->{
            if(e instanceof Nameable){
                Nameable chat =(Nameable) e;
                return chat.getName().equals(name);
            }
            return false;
        }).findFirst();
    }
}
