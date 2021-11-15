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
            <input name="departureTime" type="time" />
            <p></p>

            <strong style="display:inline" >Departure Location: </strong>
            <input name="departurePoint" type="text" placeholder="departurePoint..." />
            <p></p>

            <strong style="display:inline" >Destination: </strong>
            <input name="destination" type="text" placeholder="destination..." />
            <p></p>

            <strong style="display:inline" >Capacity: </strong>
            <input name="capacity" type="text" placeholder="capacity" />
            <p></p>

            <strong style="display:inline" >Ticket Price: </strong>
            <input name="price" type="text" placeholder="price">
            <p></p>

            <input type="submit" name="createTrip" value="Create Trip">
        </form>
    </div>
    &nbsp;
    <div>
        <strong>SEARCH TRIPS BETWEEN DATES</strong>
        <form action="main" method="post">
            <strong style="display:inline" >Start Date: </strong>
            <input type="date" name="startDate">
            <input type="time" name="startTime">
            <p></p>

            <strong style="display:inline" >End Date: </strong>
            <input type="date" name="endDate">
            <input type="time" name="endTime">
            <p></p>

            <input type="submit" name="tripsBetweenDatesM" value="Search">
        </form>
        <c:choose>
            <c:when test="${empty searchTrips}">
                <p>No trips were found</p>
            </c:when>
            <c:otherwise>
                <p></p>
                <strong>-- Trips Found --</strong>
                <c:forEach var="item" items="${searchTrips}">
                    <p></p>

                    <strong style="display:inline" >Departure Date: </strong>
                    <div style="display:inline">${item.getDepartureDate().getDay()} / </div>
                    <div style="display:inline">${item.getDepartureDate().getMonth() + 1} / </div>
                    <div style="display:inline">${item.getDepartureDate().getYear()} @ </div>
                    <div style="display:inline">${item.getDepartureDate().getHours()} :</div>
                    <div style="display:inline">${item.getDepartureDate().getMinutes()}</div>
                    <p></p>

                    <strong style="display:inline" >Departure Point: </strong>
                    <div style="display:inline">${item.getDeparturePoint()} </div>
                    <p></p>

                    <strong style="display:inline" >Destination Point: </strong>
                    <div style="display:inline">${item.getDestinationPoint()} </div>
                    <p></p>
                
                    <strong style="display:inline" >Capacity: </strong>
                    <div style="display:inline">${item.getCapacity()} </div>
                    <p></p>
                
                    <strong style="display:inline" >Ticket price: </strong>
                    <div style="display:inline">${item.getTicketPrice()} EUR</div>
                    <p></p>
                    <p>-------------</p>
                </c:forEach>
                
                <strong>CHOOSE TRIP TO DELETE</strong>
                <form action="main" method="post">
                    <select name="tripToDelete">
                        <c:forEach var="item" items="${searchTrips}">
                            <option value="${item.getTripId()}">${item.getDestinationPoint()}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" name="deleteTrip" value="Delete" />
                </form>

                <strong>CHOOSE TRIP TO SHOW PASSENGERS</strong>
                <form action="main" method="post">
                    <select name="trip">
                        <c:forEach var="item" items="${searchTrips}">
                            <option value="${item.getTripId()}">${item.getDestinationPoint()}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" name="showTripPassengers" value="Show" />
                </form>
                <c:if test="${not empty passengers}">
                    <strong>-- Passengers --</strong>
                    <c:forEach var="item" items="${passengers}">
                        <p></p>

                        <strong style="display:inline" >Name: </strong>
                        <div style="display:inline">${item.getName()} </div>
                        <p></p>

                        <strong style="display:inline" >Email: </strong>
                        <div style="display:inline">${item.getEmail()} </div>
                        <p></p>

                        <strong style="display:inline" >Phone Number: </strong>
                        <div style="display:inline">${item.getPhoneNumber()} </div>
                        <p></p>
                    
                        <strong style="display:inline" >Wallet: </strong>
                        <div style="display:inline">${item.getBalance()} EUR </div>
                        <p></p>

                        <p>-------------</p>
                    </c:forEach>
                </c:if>

            </c:otherwise>
        </c:choose>
                
    </div>
    &nbsp;
    <div>
        <strong>SEARCH TRIPS SPECIFIC DATE</strong>
        <form action="main" method="post">
            <strong style="display:inline" >Date: </strong>
            <input type="date" name="date">
            <p></p>
            <input type="submit" name="tripsSpecificDateM" value="Search">
        </form>
        <c:choose>
            <c:when test="${empty specificSearchTrips}">
                <p>No trips were found</p>
            </c:when>
            <c:otherwise>
                <p></p>
                <strong>-- Trips Found --</strong>
                <c:forEach var="item" items="${specificSearchTrips}">
                    <p></p>

                    <strong style="display:inline" >Departure Date: </strong>
                    <div style="display:inline">${item.getDepartureDate().getDay()} / </div>
                    <div style="display:inline">${item.getDepartureDate().getMonth() + 1} / </div>
                    <div style="display:inline">${item.getDepartureDate().getYear()} @ </div>
                    <div style="display:inline">${item.getDepartureDate().getHours()} :</div>
                    <div style="display:inline">${item.getDepartureDate().getMinutes()}</div>
                    <p></p>

                    <strong style="display:inline" >Departure Point: </strong>
                    <div style="display:inline">${item.getDeparturePoint()} </div>
                    <p></p>

                    <strong style="display:inline" >Destination Point: </strong>
                    <div style="display:inline">${item.getDestinationPoint()} </div>
                    <p></p>
                
                    <strong style="display:inline" >Capacity: </strong>
                    <div style="display:inline">${item.getCapacity()} </div>
                    <p></p>
                
                    <strong style="display:inline" >Ticket price: </strong>
                    <div style="display:inline">${item.getTicketPrice()} EUR</div>
                    <p></p>
                    <p>-------------</p>
                </c:forEach>
                
                <strong>CHOOSE TRIP TO DELETE</strong>
                <form action="main" method="post">
                    <select name="tripToDelete">
                        <c:forEach var="item" items="${specificSearchTrips}">
                            <option value="${item.getTripId()}">${item.getDestinationPoint()}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" name="deleteTripSpecific" value="Delete" />
                </form>
                
                <strong>CHOOSE TRIP TO SHOW PASSENGERS</strong>
                <form action="main" method="post">
                    <select name="trip">
                        <c:forEach var="item" items="${specificSearchTrips}">
                            <option value="${item.getTripId()}">${item.getDestinationPoint()}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" name="showTripPassengersSpecific" value="Show" />
                </form>
                <c:if test="${not empty passengersSpecific}">
                    <strong>-- Passengers --</strong>
                    <c:forEach var="item" items="${passengersSpecific}">
                        <p></p>

                        <strong style="display:inline" >Name: </strong>
                        <div style="display:inline">${item.getName()} </div>
                        <p></p>

                        <strong style="display:inline" >Email: </strong>
                        <div style="display:inline">${item.getEmail()} </div>
                        <p></p>

                        <strong style="display:inline" >Phone Number: </strong>
                        <div style="display:inline">${item.getPhoneNumber()} </div>
                        <p></p>
                    
                        <strong style="display:inline" >Wallet: </strong>
                        <div style="display:inline">${item.getBalance()} EUR </div>
                        <p></p>

                        <p>-------------</p>
                    </c:forEach>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
    &nbsp;
    <div>
        <strong>SHOW TOP 5 FREQUENT PASSENGERS</strong>
        <form action="main" method="post">
            <input type="submit" name="listTop" value="Show">
        </form>
        <c:choose>
            <c:when test="${empty top}">
                <p></p>
            </c:when>
            <c:otherwise>
                <p></p>
                <strong>-- Passengers Found --</strong>
                <c:forEach var="item" items="${top}">
                    <p></p>

                    <strong style="display:inline" >Name: </strong>
                    <div style="display:inline">${item.getName()} </div>
                    <p></p>

                    <strong style="display:inline" >Tickets Bought: </strong>
                    <div style="display:inline">${item.getNumberOfTickets()} </div>
                    <p></p>

                    <strong style="display:inline" >Email: </strong>
                    <div style="display:inline">${item.getEmail()} </div>
                    <p></p>

                    <strong style="display:inline" >Phone Number: </strong>
                    <div style="display:inline">${item.getPhoneNumber()} </div>
                    <p></p>
                
                    <strong style="display:inline" >Wallet: </strong>
                    <div style="display:inline">${item.getBalance()} EUR </div>
                    <p></p>

                    <p>-------------</p>
                </c:forEach>
            </c:otherwise>
        </c:choose>
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