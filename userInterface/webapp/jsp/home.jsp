<%--
  Created by IntelliJ IDEA.
  User: temkarus0070
  Date: 08.09.2021
  Time: 13:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<link rel="stylesheet" href="../css/style.css"/>
    <title>Home page</title>
</head>
<body>
<nav>
    <a href="${pageContext.request.contextPath}/jsp/createChat.jsp">create new chat</a>
    <a href="${pageContext.request.contextPath}/jsp/getChat.jsp">enter in existed chat</a>
</nav>
</body>
</html>
