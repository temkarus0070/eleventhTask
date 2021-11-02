<%--
  Created by IntelliJ IDEA.
  User: temkarus0070
  Date: 26.10.2021
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<nav>
    <a href="${pageContext.request.contextPath}/jsp/createChat.jsp">create new chat</a>
    <a href="../">home</a>
    <a href="${pageContext.request.contextPath}/jsp/getChat.jsp">enter in existed chat</a>
    <a href="${pageContext.request.contextPath}/logout">logout</a>
    <b>${cookie['username'].value}</b>

</nav>
