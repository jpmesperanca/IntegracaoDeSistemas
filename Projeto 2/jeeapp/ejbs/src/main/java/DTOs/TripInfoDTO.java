package DTOs;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class TripInfoDTO implements Serializable {

    private GregorianCalendar departureDate;
    private String departurePoint;
    private String destinationPoint;
    private int capacity;
    private Double ticketPrice;

    public TripInfoDTO() {
    }

    public TripInfoDTO(GregorianCalendar departureDate, String departurePoint, String destinationPoint, int capacity,
            Double ticketPrice) {
        this.departureDate = departureDate;
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
    }

    public GregorianCalendar getDepartureDate() {
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

    public void setDepartureDate(GregorianCalendar departureDate) {
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

}
