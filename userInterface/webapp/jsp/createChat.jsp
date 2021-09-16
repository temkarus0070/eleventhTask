<%--
  Created by IntelliJ IDEA.
  User: temkarus0070
  Date: 13.09.2021
  Time: 21:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<link rel="stylesheet" href="../css/style.css"/>
    <script type="text/javascript" src="./../js/jquery-3.6.0.js"></script>
    <script type="module" src="./../js/createChat.js" ></script>
    <title>Title</title>
</head>
<body>
<form method="post" action="../chat">
    <fieldset>
        <select id="chatType" name="chatType">
            <option selected value="PrivateChat">private chat</option>
            <option value="GroupChat">group chat</option>
            <option value="RoomChat">room chat</option>
        </select>
    </fieldset>
    <input type="submit" value="create"/>
</form>
</body>
</html>
