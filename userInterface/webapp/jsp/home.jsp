
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/style.css"/>
    <title>Home page</title>
</head>
<body>

<nav>
    <a href="${pageContext.request.contextPath}/jsp/createChat.jsp">create new chat</a>
    <a href="${pageContext.request.contextPath}/jsp/getChat.jsp">enter in existed chat</a>
    <a href="${pageContext.request.contextPath}/logout">logout</a>

</nav>
<form method="get" action="/">
<label for="chatType">chat type</label>
<select name="chatType" id="chatType">
    <option selected value="PRIVATE">private</option>
    <option <c:if test="${chatType.equals('ROOM')}" >selected</c:if> value="ROOM">room</option>
    <option <c:if test="${chatType.equals('GROUP')}" >selected</c:if> value="GROUP">group</option>
</select>
<input type="submit" value="update chat list" />
</form>
<ul>
<c:forEach items="${chats}" var="chat">
    <li><a href="../chat?chatType=${chat.getType()}&chatId=${chat.getId()}" >chat ${chat.getId()}</a></li>

</c:forEach>
</ul>
</body>
</html>
