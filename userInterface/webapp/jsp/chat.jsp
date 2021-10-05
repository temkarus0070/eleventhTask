<%@ page import="chatApp.domain.chat.Message" %>
<%@ page import="chatApp.domain.chat.Chat" %><%--
  Created by IntelliJ IDEA.
  User: temkarus0070
  Date: 13.09.2021
  Time: 22:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chat ${chat.getId()}</title>
    <meta charset="UTF-8" />
</head>
<body>
<h2>messages</h2>
<%
    Chat chat=(Chat) request.getAttribute("chat");
%>
<form action="../messages" method="post">
    <input type="hidden" value="<%=chat.getType()%>" name="chatType" id="chatType" />
    <input type="hidden" value="<%=chat.getId()%>" name="chatId" id="chatId" />
    <label for="message">text</label>
    <textarea name="message" id="message" required>

    </textarea>
    <br/>
    <input type="submit" value="sendMessage" />
</form>
<ul>
<%

    if(chat!=null)
        for(int i=0;i<chat.getMessages().size();i++){
%>



    <li>
    <p><%=chat.getMessages().get(i).getSender().getName()%></p>
        <p><%= chat.getMessages().get(i).getContent() %></p>
    </li>
    <%}%>
</ul>
</body>
</html>
