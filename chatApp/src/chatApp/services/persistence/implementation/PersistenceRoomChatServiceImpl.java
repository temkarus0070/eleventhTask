package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.RoomChat;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistenceRoomChatServiceImpl extends PersistenceChatServiceImpl<RoomChat>  implements PersistenceChatService<RoomChat>{
    private ChatRepository chatRepository;

    public PersistenceRoomChatServiceImpl(ChatRepository repository) {
        super(repository);
        this.chatRepository = repository;
    }

    public Optional<RoomChat> getChatByName(String name) throws Exception {
        return chatRepository.getChatByName(name).stream()
                .map(chat -> (RoomChat) chat)
                .findFirst();
    }


    @Override
    public Optional<RoomChat> getChat(int id) throws Exception {
        try {
            return Stream.of(chatRepository.get(id))
                    .map(chat -> (RoomChat) chat)
                    .findFirst();
        }
        catch (Exception ex){
            throw new Exception("chat not found exception");
        }
    }


    @Override
    public Collection<RoomChat> get() throws Exception {
        return this.chatRepository.get().stream()
                .map(chat -> (RoomChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(RoomChat chat) throws Exception {
        Optional<RoomChat> existedChat = getChat(chat.getId());
        if (existedChat.isPresent()){
            if (!chat.getName().equals(existedChat.get().getName())) {
            if (getChatByName(chat.getName()).isPresent()) {
                throw new ChatAlreadyExistsException();
            }
        }
        this.chatRepository.update(chat);
    }
}

    @Override
    public Collection<RoomChat> getChatsByUserName(String username) throws Exception {
        return this.chatRepository.getChatsByUser(username).stream()
                .map(chat -> (RoomChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void addChat(RoomChat chat) throws Exception {
        Optional<RoomChat> chatOptional = getChatByName(chat.getName());
        if (chatOptional.isPresent()) {
            throw new ChatAlreadyExistsException();
        } else {
            chatRepository.add(chat);
        }
    }
}
