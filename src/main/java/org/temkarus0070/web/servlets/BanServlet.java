package org.temkarus0070.web.servlets;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.services.persistence.ChatStorage;
import org.temkarus0070.application.services.persistence.implementation.PersistenceChatServiceImpl;
import org.temkarus0070.application.services.persistence.interfaces.PersistenceChatService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BanServlet extends HttpServlet {
    PersistenceChatService persistenceChatService = new PersistenceChatServiceImpl(new ChatStorage(ChatType.ANY));

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String chatType = req.getParameter("chatType");
        Integer chatId = Integer.parseInt(req.getParameter("chatId"));
        persistenceChatService.banUserInChat(username, chatId);
        resp.sendRedirect(String.format("../chat?chatType=%s&chatId=%s", chatType, chatId));
    }
}
