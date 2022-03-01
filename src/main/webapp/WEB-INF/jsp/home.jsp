<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<html>
<head>
    <link rel="stylesheet" href="../css/style.css"/>
    <title>Home page</title>
</head>
<body>


<jsp:include page="header.jsp"/>

<form method="get" action="./home">
    <label for="chatType">chat type</label>
    <select name="chatType" id="chatType">
        <option selected value="ANY">ANY</option>
        <option value="PRIVATE">private</option>
        <option
                <c:if test="${chatType.equals('ROOM')}">selected</c:if> value="ROOM">room
        </option>
        <option
                <c:if test="${chatType.equals('GROUP')}">selected</c:if> value="GROUP">group
        </option>
    </select>
    <input type="submit" value="update chat list"/>
</form>
<ul>
    <c:forEach items="${chats}" var="chat">
        <li><a href="../chat?chatType=${chat.getType()}&chatId=${chat.getId()}">
                ${chat.toString()}
        </a></li>

    </c:forEach>
</ul>
</body>

</html>