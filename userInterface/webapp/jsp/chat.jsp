<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat ${chat.getId()}</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<form action="../messages" method="post">
    <input type="hidden" value="${chat.getType()}" name="chatType" id="chatType"/>
    <input type="hidden" value="${chat.getId()}" name="chatId" id="chatId"/>
    <label for="message">text</label>
    <textarea name="message" id="message" required>

    </textarea>
    <br/>
    <input type="submit" value="sendMessage" />
</form>

<form action="../user" method="post">
    <input type="hidden" value="${chat.getType()}" name="chatType"/>
    <input type="hidden" value="${chat.getId()}" name="chatId">
    <select name="username">
        <c:forEach items="${users}" var="user">
            <option value="${user.getName()}">${user.getName()}</option>
        </c:forEach>
    </select>
    <input type="submit" value="add user" />
</form>




<c:forEach items="${chat.getMessages()}" var="message">
    <h2>${message.getSender().getName()}</h2>
    <span>${message.getContent()}</span>


</c:forEach>


</body>
</html>