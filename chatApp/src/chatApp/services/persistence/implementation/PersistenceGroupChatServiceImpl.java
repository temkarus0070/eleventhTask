package chatApp.services.persistence.implementation;

import chatApp.domain.chat.*;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistenceGroupChatServiceImpl implements PersistenceChatService<GroupChat> {
    private final ChatRepository repository;
    public PersistenceGroupChatServiceImpl(ChatRepository repository) {
        this.repository=repository;
    }

    public Optional<GroupChat> getChatByName(String name)throws Exception {
        return repository.getChatByName(name)
                .stream().filter(chat -> chat.getType()==ChatType.GROUP)
                .map(chat -> (GroupChat) chat)
                .findFirst();
    }




    @Override
    public Optional<GroupChat> getChat(int id) throws Exception{
        return Stream.of(repository.get(id))
                .filter(chat -> chat.getId() == id && chat.getType() == ChatType.GROUP)
                .map(chat -> (GroupChat) chat)
                .findFirst();
    }

    @Override
    public void removeChat(int id) throws Exception{
        Optional<Chat> chatOptional=this.repository.get().stream().filter(chat->chat.getId()==id).findFirst();
        if(chatOptional.isPresent()){
            this.repository.delete(chatOptional.get().getId());
        }
    }

    @Override
    public Collection<GroupChat> get()throws Exception {
        return this.repository.get().stream().filter(chat->chat.getType()==ChatType.GROUP)
                .map(chat->(GroupChat)chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(GroupChat chat) throws Exception {
        this.repository.update(chat);
    }

    @Override
    public Collection<GroupChat> getChatsByName(String username)throws Exception {
        return this.repository.getChatsByUser(username).stream().filter(chat->chat.getType()==ChatType.GROUP)
                .map(chat->(GroupChat)chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void addChat(GroupChat chat)throws ChatAlreadyExistsException,Exception {
        Optional<GroupChat> chatOptional=getChatByName(chat.getName());
        if(chatOptional.isPresent()){
            throw new ChatAlreadyExistsException();
        }
        else{
            repository.add(chat);
        }
    }
}
