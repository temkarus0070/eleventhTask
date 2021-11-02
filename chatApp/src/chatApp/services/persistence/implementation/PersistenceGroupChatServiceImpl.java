package chatApp.services.persistence.implementation;

import chatApp.domain.chat.*;
import chatApp.domain.exceptions.ChatAlreadyExistsException;
import chatApp.domain.exceptions.ChatUsersOverflowException;
import chatApp.services.persistence.interfaces.ChatRepository;
import chatApp.services.persistence.interfaces.PersistenceChatService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistenceGroupChatServiceImpl extends PersistenceChatServiceImpl<GroupChat> implements PersistenceChatService<GroupChat> {
    private final ChatRepository repository;

    public PersistenceGroupChatServiceImpl(ChatRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Optional<GroupChat> getChatByName(String name) throws Exception {
        return repository.getChatByName(name)
                .stream()
                .map(chat -> (GroupChat) chat)
                .findFirst();
    }


    @Override
    public Optional<GroupChat> getChat(int id) throws Exception {
        try {
            return Stream.of(repository.get(id))
                    .map(chat -> (GroupChat) chat)
                    .findFirst();
        } catch (Exception ex) {
            throw new Exception("chat not found exception");
        }
    }

    @Override
    public Collection<GroupChat> get() throws Exception {
        return this.repository.get().stream()
                .map(chat -> (GroupChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateChat(GroupChat chat) throws Exception {
        Optional<GroupChat> existedChat = getChat(chat.getId());
        if (existedChat.isPresent()) {
            if (chat.getUsersCount() < chat.getUserList().size()) {
                throw new Exception("Your chat size will be less than current users count. Remove users or set greater value");
            } else if (!chat.getName().equals(existedChat.get().getName())) {
                if (getChatByName(chat.getName()).isPresent()) {
                    throw new ChatAlreadyExistsException();
                }
            }
            this.repository.update(chat);
        }
    }


    @Override
    public void addChat(GroupChat chat) throws Exception {
        Optional<GroupChat> chatOptional = getChatByName(chat.getName());
        if (chatOptional.isPresent()) {
            throw new ChatAlreadyExistsException();
        } else {
            repository.add(chat);
        }
    }

    @Override
    public Collection<GroupChat> getChatsByUserName(String username) throws Exception {
        return this.repository.getChatsByUser(username).stream()
                .map(chat -> (GroupChat) chat)
                .collect(Collectors.toSet());
    }

    @Override
    public void addUser(String username, int chatId) throws Exception {
        Optional<GroupChat> groupChat = getChat(chatId);
        if (groupChat.isPresent()) {
            if (groupChat.get().getUsersCount() >= groupChat.get().getUserList().size() + 1)
                repository.addUserToChat(username, chatId);
            else throw new ChatUsersOverflowException();
        }
    }
}
