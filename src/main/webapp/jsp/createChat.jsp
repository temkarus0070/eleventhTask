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
    <script type="text/javascript" src="./../js/createChat.js" ></script>
    <title>Title</title>
</head>
<body>
<jsp:include page="../WEB-INF/jsp/header.jsp"></jsp:include>
<form method="post" action="../chat">
    <fieldset>
        <select id="type" name="type">
            <option selected value="PRIVATE">private chat</option>
            <option value="GROUP">group chat</option>
            <option value="ROOM">room chat</option>
        </select>
    </fieldset>
    <input type="submit" value="create"/>
</form>
</body>
</html>
