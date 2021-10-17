package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.GroupChat;
import chatApp.domain.chat.PrivateChat;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistencePrivateChatServiceImpl implements PersistenceChatService<PrivateChat> {
    private ChatRepository chatRepository;

    public PersistencePrivateChatServiceImpl(ChatRepository repository) {
        this.chatRepository = repository;
    }

    @Override
    public Optional<PrivateChat> getChat(int id)throws Exception {
        return Stream.of(chatRepository.get(id))
                .filter(chat -> chat.getId() == id && chat.getType() == ChatType.PRIVATE)
                .map(chat -> (PrivateChat) chat)
                .findFirst();
    }

    @Override
    public void removeChat(int id) throws Exception {
        chatRepository.delete(id);
    }

    @Override
    public Collection<PrivateChat> get()throws Exception {
        return chatRepository.get().stream()
                .filter(chat -> chat.getType() == ChatType.PRIVATE)
                .map(chat -> (PrivateChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(PrivateChat chat)throws Exception {
        this.chatRepository.update(chat);
    }

    @Override
    public Collection<PrivateChat> getChatsByName(String username)throws Exception {
        return this.chatRepository.getChatsByUser(username).stream().filter(chat->chat.getType()==ChatType.PRIVATE)
                .map(chat->(PrivateChat)chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void addChat(PrivateChat chat)throws Exception {
        chatRepository.add(chat);
    }
}
