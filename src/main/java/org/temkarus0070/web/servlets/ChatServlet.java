package org.temkarus0070.web.servlets;

import org.temkarus0070.web.ChatRequestParamExtractors.ChatParamExtractor;
import org.temkarus0070.web.ChatRequestParamExtractors.ChatRequestParamExtractorBuilder;
import org.temkarus0070.application.domain.User;
import org.temkarus0070.application.domain.chat.*;
import org.temkarus0070.application.domain.exceptions.ChatAppDatabaseException;
import org.temkarus0070.application.domain.exceptions.ChatAppException;
import org.temkarus0070.application.services.AuthService;
import org.temkarus0070.application.services.AuthServiceImpl;
import org.temkarus0070.application.services.PasswordEncoderImpl;
import org.temkarus0070.application.services.persistence.ChatStorage;
import org.temkarus0070.application.services.persistence.UserStorage;
import org.temkarus0070.application.services.persistence.implementation.*;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceUserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ChatServlet extends HttpServlet {
    private PersistenceChatServiceImpl persistenceChatService;
    private PersistenceUserService persistenceUserService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        try {
            persistenceChatService = new PersistenceChatServiceImpl(new ChatStorage(ChatType.ANY));
            authService = new AuthServiceImpl(new PersistenceUserServiceImpl(new UserStorage()), new PasswordEncoderImpl());
            persistenceUserService = new PersistenceUserServiceImpl(new UserStorage());
        } catch (ChatAppDatabaseException exception) {
            throw new ChatAppDatabaseException(exception);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ChatParamExtractor chatParamExtractor = ChatRequestParamExtractorBuilder.build(req.getParameterMap());
            Optional<Chat> chat = chatParamExtractor.extractChat(req.getParameterMap());
            User current = authService.getCurrentUser(req.getCookies());
            if (chat.isPresent()) {
                if (hasPermissions(current, chat.get())) {
                    Set<User> bannedUserSet = new HashSet<>(chat.get().getBannedUsers());
                    Set<User> currentUsers = new HashSet<>(chat.get().getUserList());
                    req.setAttribute("chat", chat.get());
                    req.setAttribute("users", persistenceUserService.getUsersNotAtThatChat(chat.get().getId()));
                    Collection<User> users = persistenceUserService.get().stream().filter(e -> !bannedUserSet.contains(e) && currentUsers.contains(e)).collect(Collectors.toList());
                    req.setAttribute("usersToBan", users);
                } else {
                    resp.getOutputStream().write(" you dont have permissions to participate at that chat".getBytes(StandardCharsets.UTF_8));
                    return;
                }
            } else {
                resp.getOutputStream().write("chat not found".getBytes(StandardCharsets.UTF_8));
                return;
            }
        } catch (Exception exception) {
            MyLogger.log(Level.SEVERE, exception.getMessage());
            resp.getOutputStream().write(exception.getMessage().getBytes(StandardCharsets.UTF_8));
            return;
        }
        getServletContext().getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);

    }

    private boolean hasPermissions(User user, Chat chat) {
        return chat.getUserList().contains(user);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChatParamExtractor chatParamExtractor = ChatRequestParamExtractorBuilder.build(req.getParameterMap());
        Optional<Chat> chat = chatParamExtractor.putChat(req.getParameterMap());
        try {
            User current = authService.getCurrentUser(req.getCookies());
            if (chat.isPresent()) {
                chat.get().setChatOwner(current);
                persistenceChatService.addChat(chat.get());
                req.setAttribute("chat", chat.get());
                req.setAttribute("chatType", chat.get().getType());
                req.setAttribute("id", chat.get().getId());
                resp.sendRedirect(String.format("../chat?chatType=%s&chatId=%d", chat.get().getType(), chat.get().getId()));
            }
        } catch (ChatAppException e) {
            MyLogger.log(Level.SEVERE, e.getMessage());
            resp.getOutputStream().write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }


    }
}
