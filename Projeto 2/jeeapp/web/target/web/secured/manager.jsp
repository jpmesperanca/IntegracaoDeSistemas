<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Manager Webpage</title>
</head>
<body>

    <c:if test="${not empty errorMessage}">
        <p>${errorMessage}</p>
        <c:remove var="errorMessage" scope="session" /> 
    </c:if>
    &nbsp;
    <div>
        <strong>CREATE TRIP</strong>
        <form action="main" method="post">

            <strong style="display:inline" >Departure Date: </strong>
            <input name="departureDate" type="date" />

            <strong style="display:inline" >Departure Location: </strong>
            <input name="departurePoint" type="text" placeholder="departurePoint..." />

            <strong style="display:inline" >Destination: </strong>
            <input name="destination" type="text" placeholder="destination..." />

            <strong style="display:inline" >Capacity: </strong>
            <input name="capacity" type="text" placeholder="capacity" />

            <strong style="display:inline" >Ticket Price: </strong>
            <input name="price" type="text" placeholder="price">

            <input type="submit" name="createTrip" value="Create Trip">
        </form>
    </div>
    &nbsp;
    <div>
        <form action="main" method="get">
            <input type="submit" name="delete" value="Delete Account" />
        </form>
    </div> 
    &nbsp;
    <div>
        <form action="main" method="get">
            <input type="submit" name="logout" value="Logout" />
        </form>
    </div>  
</body>
</html>