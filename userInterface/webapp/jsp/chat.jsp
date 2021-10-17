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

<h2>Departments and Employees</h2>
<form action="../messages" method="post">
    <input type="hidden" value="${chat.getType()}" name="chatType" id="chatType"/>
    <input type="hidden" value="${chat.getId()}" name="chatId" id="chatId"/>
    <label for="message">text</label>
    <textarea name="message" id="message" required>

    </textarea>
    <br/>
    <input type="submit" value="sendMessage" />
</form>


<c:forEach items="${chat.getMessages()}" var="message">
    <h3>${message.getContent()}</h3>


</c:forEach>


</body>
</html>