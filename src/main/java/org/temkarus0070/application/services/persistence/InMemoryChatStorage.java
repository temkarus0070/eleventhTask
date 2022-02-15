package org.temkarus0070.application.services.persistence;


import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.Message;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.services.persistence.interfaces.ChatRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class InMemoryChatStorage implements ChatRepository {
  private    static int maxId=0;
  private    static InMemoryChatStorage inMemoryChatStorage;

    private InMemoryChatStorage(){

    }

    public static InMemoryChatStorage getInstance(){
        if(inMemoryChatStorage ==null){
            inMemoryChatStorage =new InMemoryChatStorage();
            chats= new HashSet<>();
        }
        return inMemoryChatStorage;
    }

    private static Collection<Chat> chats;



    @Override
    public Collection<Chat> get() {
        return chats;
    }

    @Override
    public Collection<Chat> getChatsByUser(String username) {
        return null;
    }

    @Override
    public Optional<Chat> getChatByName(String name) {
        return Optional.empty();
    }

    @Override
    public void removeUserFromChat(String user, Integer chatId) throws ChatAppDatabaseException {

    }

    @Override
    public void addUserToChat(String user, Integer chatId) throws ChatAppDatabaseException {

    }

    @Override
    public void banUserInChat(String user, Integer chatId) throws ChatAppDatabaseException {

    }

    @Override
    public void addMessage(Message message, Integer chatId) throws ChatAppDatabaseException {

    }

    @Override
    public Chat get(Integer integer) {
        return null;
    }


    @Override
    public void update(Chat entity) {
        Chat chat=get().stream().filter(chat1 -> chat1.getId()==entity.getId()).findFirst().get();
        if(chat!=null) {
            chats.remove(chat);
            chats.add(entity);
        }

    }

    @Override
    public void add(Chat chat){
        chat.setId(maxId);
        chats.add(chat);
        maxId++;
    }

    @Override
    public void delete(Integer chat) {
        chats.remove(chat);
    }


}
