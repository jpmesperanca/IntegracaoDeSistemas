<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Passenger Webpage</title>
</head>
<body>
    <strong>
        <c:choose>
        <c:when test="${empty myListOfNumbers}">No items to be shown...</c:when>
        <c:when test="${myListOfNumbers.size()==1}">A single item...</c:when>
        <c:otherwise>Too many items...</c:otherwise>
        </c:choose>
    </strong>
    <c:forEach var="item" items="${myListOfNumbers}">
        <div>Content is ${item}</div>
    </c:forEach>
</body>
</html>