<%--
  Created by IntelliJ IDEA.
  User: temkarus0070
  Date: 08.09.2021
  Time: 14:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="../css/style.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/getChat.js"></script>

    <title>Get chat</title>
</head>
<body>
<form method="get" action="../chat">
    <fieldset>
    <select id="chatType" name="chatType">
        <option selected value="PRIVATE">private chat</option>
        <option value="GROUP">group chat</option>
        <option value="ROOM">room chat</option>
    </select>
    <label for="chatId">chat identifier</label>
    <input type="number" id="chatId" name="chatId" />
    </fieldset>
    <input type="submit" value="get"/>
</form>
</body>
</html>
