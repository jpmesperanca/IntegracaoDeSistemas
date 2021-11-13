package DTOs;

import java.io.Serializable;

public class TripInfoDTO implements Serializable {

    private GregorianCalendarDTO departureDate;
    private String departurePoint;
    private String destinationPoint;
    private int capacity;
    private Double ticketPrice;
    private int tripId;

    public TripInfoDTO() {
    }

    public TripInfoDTO(GregorianCalendarDTO departureDate, String departurePoint, String destinationPoint, int capacity,
            Double ticketPrice, int tripId) {
        this.departureDate = departureDate;
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.tripId = tripId;
    }

    public GregorianCalendarDTO getDepartureDate() {
        return departureDate;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public int getCapacity() {
        return capacity;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public int getTripId() {
        return tripId;
    }

    public void setDepartureDate(GregorianCalendarDTO departureDate) {
        this.departureDate = departureDate;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        Integer m = this.getDepartureDate().getMonth() + 1;
        return "Trip ID [" + this.getTripId() + "]\nDeparture Date: " + this.getDepartureDate().getDay() + "/" + m + "/"
                + this.getDepartureDate().getYear() + "\nDeparture Point: " + this.getDeparturePoint()
                + "\nDestination Point: " + this.getDestinationPoint() + "\nCapacity: " + this.getCapacity()
                + "px\nTicket Price: " + this.getTicketPrice() + " EUR\n";
    }

}
