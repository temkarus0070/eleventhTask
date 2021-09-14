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

    <script type="text/javascript" src="./../js/jquery-3.6.0.js"></script>
    <script type="module" src="./../js/createChat.js" ></script>
    <title>Title</title>
</head>
<body>
<form>
    <fieldset>
        <select id="chatType" name="chatType">
            <option selected value="privateChat">private chat</option>
            <option value="groupChat">group chat</option>
            <option value="roomChat">room chat</option>
        </select>
        <label for="chatId">chat identifier</label>
        <input type="number" id="chatId" name="chatId" />
    </fieldset>
</form>
</body>
</html>
