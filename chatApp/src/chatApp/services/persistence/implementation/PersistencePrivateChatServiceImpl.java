package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.PrivateChat;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersistencePrivateChatServiceImpl implements PersistenceChatService<PrivateChat> {
    private ChatRepository chatRepository;

    public PersistencePrivateChatServiceImpl(ChatRepository repository) {
        this.chatRepository = repository;
    }

    @Override
    public Optional<PrivateChat> getChat(int id) {
        return chatRepository.get().stream()
                .filter(chat -> chat.getId() == id && chat.getType() == ChatType.PRIVATE)
                .map(chat -> (PrivateChat) chat)
                .findFirst();
    }

    @Override
    public void removeChat(int id) {
        Optional<PrivateChat> chatOptional = get().stream().filter(chat -> chat.getId() == id).findFirst();
        chatOptional.ifPresent(chat -> chatRepository.delete(chat));

    }

    @Override
    public Collection<PrivateChat> get() {
        return chatRepository.get().stream()
                .filter(chat -> chat.getType() == ChatType.PRIVATE)
                .map(chat -> (PrivateChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(PrivateChat chat) {
        this.chatRepository.update(chat);
    }

    @Override
    public void addChat(PrivateChat chat) {
        chatRepository.add(chat);
    }
}
