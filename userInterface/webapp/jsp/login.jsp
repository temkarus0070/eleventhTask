<%--
  Created by IntelliJ IDEA.
  User: temkarus0070
  Date: 16.09.2021
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/register">register</a>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label for="username">username</label>
    <input required type="text" name="username" id="username"/>
    <label for="password">password</label>
    <input required type="password" id="password" name="password"/>
    <input type="submit" value="login" />
</form>

</body>
</html>
