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
<%
    if(pageContext.request.contextPath){

    }
%>
    <a  href="${pageContext.request.contextPath}">${pageContext.request.requestURL}</a>
    <a href="${pageContext.request.contextPath}/jsp/createChat.jsp">create new chat</a>
    <a href="${pageContext.request.contextPath}/jsp/getChat.jsp">enter in existed chat</a>
    <a href="${pageContext.request.contextPath}/logout">logout</a>
</nav>
</body>
</html>
