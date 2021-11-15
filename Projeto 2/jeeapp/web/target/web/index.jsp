<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body> 
    <c:if test="${not empty errorMessage}">
        <p>${errorMessage}</p>
        <c:remove var="errorMessage" scope="session" /> 
    </c:if>
    &nbsp;
    <div>
        <strong>Login</strong>
        <form action="main" method="post">
            <input name="email" type="text" placeholder="email..." />
            <input name="key" type="password" placeholder="password..." />
            <input type="submit" name="login" value="Login">
        </form>
    </div>
    &nbsp;
    <div>
        <strong>Register</strong>
        <form action="main" method="post">
            <input name="email" type="email" placeholder="email..." />
            <input name="key" type="password" placeholder="password..." />
            <input name="name" type="text" placeholder="name..." />
            <input name="phone" type="number" placeholder="phone..." />
            <input type="submit" name="register" value="Register">
        </form>
    </div>
</body>
</html>