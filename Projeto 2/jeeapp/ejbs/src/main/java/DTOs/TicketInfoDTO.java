package DTOs;

import java.io.Serializable;

public class TicketInfoDTO implements Serializable {

    private int ticketId;
    private int tripId;
    private int passengerId;

    public TicketInfoDTO() {
    }

    public TicketInfoDTO(int ticketId, int tripId, int passengerId) {
        this.ticketId = ticketId;
        this.tripId = tripId;
        this.passengerId = passengerId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getTripId() {
        return tripId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

}
