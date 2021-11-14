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
    <strong>PERSONAL INFORMATION</strong>
    <div>
        <div>
            <strong style="display:inline" >Email: </strong>
            <c:out value="${email}"/>
        </div>
        <div>
            <strong style="display:inline" >Name: </strong>
            <c:out value="${name}"/>
        </div>
        <div>
            <strong style="display:inline" >Phone number: </strong>
            <c:out value="${phone}"/>
        </div>
        <div> 
            <strong style="display:inline" >Balance: </strong>
            <c:out value="${balance}"/>
        </div>
    </div>
    &nbsp;
    <div>
        <strong>CHANGE INFORMATION</strong>
        <form action="main" method="post">
            <strong style="display:inline" >Email: </strong>
            <input name="email" type="text" />

            <strong style="display:inline" >Password: </strong>
            <input name="key" type="password" />

            <strong style="display:inline" >Name: </strong>
            <input name="name" type="text" />

            <strong style="display:inline" >Phone number: </strong>
            <input name="phone" type="text" />

            <input type="submit" name="changeInfo" value="Change Info">
        </form>
    </div>
    &nbsp;
    <div>
        <strong>SEARCH TRIPS BETWEEN DATES</strong>
        <form action="main" method="post">
            <strong style="display:inline" >Start Date: </strong>
            <input type="date" name="startDate">

            <strong style="display:inline" >End Date: </strong>
            <input type="date" name="endDate">

            <input type="submit" name="tripsBetweenDates" value="Search">
        </form>
        <c:choose>
            <c:when test="${empty searchTrips}">
                <p></p>
            </c:when>
            <c:otherwise>
                <p></p>
                <strong>-- Trips Found --</strong>
                <c:forEach var="item" items="${searchTrips}">
                    <p></p>

                    <strong style="display:inline" >Departure Date: </strong>
                    <div style="display:inline">${item.getDepartureDate().getDay()} / </div>
                    <div style="display:inline">${item.getDepartureDate().getMonth() + 1} / </div>
                    <div style="display:inline">${item.getDepartureDate().getYear()}</div>
                    <p></p>

                    <strong style="display:inline" >Departure Point: </strong>
                    <div style="display:inline">${item.getDeparturePoint()} </div>
                    <p></p>

                    <strong style="display:inline" >DestinationPoint: </strong>
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

                <strong>CHOOSE TRIP TO BUY</strong>
                <form action="main" method="post">
                    <select name="tripToBuy">
                        <c:forEach var="item" items="${searchTrips}">
                        <option value="${item.getTripId()}">${item.getDestinationPoint()}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" name="purchase" value="Buy Ticket" />
                </form>

            </c:otherwise>
        </c:choose>
    </div>
    &nbsp;
    <div>
        <strong>MY TRIPS</strong>
        <c:choose>
            <c:when test="${empty myTrips}">
                <p>No trips found</p>
            </c:when>
            <c:otherwise>
                <p></p>
                <c:forEach var="item" items="${myTrips}">
                    <p></p>

                    <strong style="display:inline" >Departure Date: </strong>
                    <div style="display:inline">${item.getDepartureDate().getDay()} / </div>
                    <div style="display:inline">${item.getDepartureDate().getMonth() + 1} / </div>
                    <div style="display:inline">${item.getDepartureDate().getYear()}</div>
                    <p></p>

                    <strong style="display:inline" >Departure Point: </strong>
                    <div style="display:inline">${item.getDeparturePoint()} </div>
                    <p></p>

                    <strong style="display:inline" >DestinationPoint: </strong>
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

                <strong>CHOOSE TRIP TO REFUND</strong>
                <form action="main" method="post">
                    <select name="tripToRefund">
                        <c:forEach var="item" items="${myTrips}">
                            <option value="${item.getTripId()}">${item.getDestinationPoint()}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" name="refundMyTrip" value="Refund" />
                </form>

            </c:otherwise>
        </c:choose>
    </div>
    &nbsp;
    <div>
        <form action="main" method="post">
            <select name="chargeAmount">
                <option value="10">10 EUR</option>
                <option value="25">25 EUR</option>
                <option value="50">50 EUR</option>
                <option value="100">100 EUR</option>
              </select>
            <input type="submit" name="charge" value="Charge Wallet" />
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
