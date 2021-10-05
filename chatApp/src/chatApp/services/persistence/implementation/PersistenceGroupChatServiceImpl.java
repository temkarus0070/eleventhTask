package chatApp.services.persistence.implementation;

import chatApp.domain.chat.Chat;
import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.RoomChat;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersistenceGroupChatServiceImpl implements PersistenceChatService<GroupChat> {
    private final ChatRepository repository;
    public PersistenceGroupChatServiceImpl(ChatRepository repository) {
        this.repository=repository;
    }

    public Optional<GroupChat> getChatByName(String name) {
        return get().stream().filter(e->e.getName().equals(name)).findFirst();
    }




    @Override
    public Optional<GroupChat> getChat(int id) {
        return    this.repository.get().stream()
                .filter(chat->chat.getId()==id && chat.getType()==ChatType.GROUP)
                .map(chat->(GroupChat)chat)
                .findFirst();
    }

    @Override
    public void removeChat(int id) {
        Optional<Chat> chatOptional=this.repository.get().stream().filter(chat->chat.getId()==id).findFirst();
        chatOptional.ifPresent(this.repository::delete);
    }

    @Override
    public Collection<GroupChat> get() {
        return this.repository.get().stream().filter(chat->chat.getType()==ChatType.GROUP)
                .map(chat->(GroupChat)chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(GroupChat chat)  {
        this.repository.update(chat);
    }

    @Override
    public void addChat(GroupChat chat)throws ChatAlreadyExistsException {
        Optional<GroupChat> chatOptional=getChatByName(chat.getName());
        if(chatOptional.isPresent()){
            throw new ChatAlreadyExistsException();
        }
        else{
            repository.add(chat);
        }
    }
}
