package chatApp.services.persistence.implementation;

import chatApp.domain.chat.ChatType;
import chatApp.domain.chat.RoomChat;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersistenceRoomChatServiceImpl implements PersistenceChatService<RoomChat> {
    private ChatRepository chatRepository;
    public PersistenceRoomChatServiceImpl(ChatRepository repository) {
       this.chatRepository=repository;
    }

    public Optional<RoomChat> getChatByName(String name) {
        return chatRepository.get().stream()
                .filter(chat -> chat.getType()==ChatType.ROOM &&((RoomChat)chat).getName().equals(name))
                .map(chat -> (RoomChat)chat)
                .findFirst();

    }



    @Override
    public Optional<RoomChat> getChat(int id) {
        return get().stream().filter(roomChat -> roomChat.getId()==id).findFirst();
    }

    @Override
    public void removeChat(int id) {
        Optional<RoomChat> roomChatOptional=getChat(id);
        roomChatOptional.ifPresent(roomChat -> chatRepository.delete(roomChat));

    }

    @Override
    public Collection<RoomChat> get() {
        return this.chatRepository.get().stream()
                .filter(chat -> chat.getType()==ChatType.ROOM)
                .map(chat -> (RoomChat)chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(RoomChat chat){
            this.chatRepository.delete(chat);
            this.chatRepository.add(chat);
    }

    @Override
    public void addChat(RoomChat chat)throws ChatAlreadyExistsException {
        Optional<RoomChat> chatOptional=getChatByName(chat.getName());
        if(chatOptional.isPresent()){
            throw new ChatAlreadyExistsException();
        }
        else{
            chatRepository.add(chat);
        }
    }
}
