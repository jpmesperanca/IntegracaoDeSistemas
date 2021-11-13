<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Manager Webpage</title>
</head>
<body>
    <strong>Current logged user is ${role}</strong>
    &nbsp;
    <div>
        <form action="main" method="get">
            <input type="submit" name="logout" value="Logout" />
        </form>
    </div>  
</body>
</html>