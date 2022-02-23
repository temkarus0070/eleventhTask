package org.temkarus0070.application.services.persistence;


import org.temkarus0070.application.domain.chat.Chat;
import org.temkarus0070.application.domain.chat.ChatType;
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
    public void update(Chat entity) {


    }

    @Override
    public Chat get(Integer id, ChatType chatType) throws ChatAppDatabaseException {
        return null;
    }

    @Override
    public Collection<Chat> getChatsByUser(String username, ChatType chatType) throws ChatAppDatabaseException {
        return null;
    }

    @Override
    public void add(Chat chat) {
        chat.setId(maxId);
        chats.add(chat);
        maxId++;
    }

    @Override
    public Collection<Chat> get(ChatType chatType) throws ChatAppDatabaseException {
        return null;
    }

    @Override
    public Optional<Chat> getChatByName(String name, ChatType chatType) throws ChatAppDatabaseException {
        return Optional.empty();
    }

    @Override
    public void delete(Integer chat) {
        chats.remove(chat);
    }


}
