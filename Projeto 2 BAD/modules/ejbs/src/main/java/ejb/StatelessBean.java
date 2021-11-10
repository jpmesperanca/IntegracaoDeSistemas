package ejb;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import jpa.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Stateless(mappedName = "slb")
public class StatelessBean implements StatelessBeanInterface {

    @PersistenceContext(name = "TripDB")
    EntityManager em;

    public StatelessBean() {
    }

    public void addPassenger(String email, String password, String name, String phoneNumber) {

        Passenger p = new Passenger(email, password, name, phoneNumber, 0.0);
        em.persist(p);
    }

    public void addManager(String email, String password, String name, String phoneNumber) {

        Manager m = new Manager(email, password, name, phoneNumber);
        em.persist(m);
    }

    public int getPassengerByEmail(String email) {

        TypedQuery<Passenger> q = em.createQuery("from Passenger p where p.email = :email", Passenger.class);

        q.setParameter("email", email);

        return q.getSingleResult().getId();
    }

    public void editPassenger(int id, String email, String password, String name, String phoneNumber) {

        Passenger p = em.find(Passenger.class, id);

        if (email != null)
            p.setEmail(email);

        if (password != null)
            p.setPassword(password);

        if (name != null)
            p.setName(name);

        if (phoneNumber != null)
            p.setPhoneNumber(phoneNumber);
    }

    public void deletePassenger(int id) {
        em.remove(em.find(Passenger.class, id));
    }

    public List<Integer> listTrips(GregorianCalendar start, GregorianCalendar end) {

        TypedQuery<Integer> q = em.createQuery(
                "select t.id from Trip t where t.departureDate >= :start and t.departureDate <= :end", Integer.class);

        q.setParameter("start", start);
        q.setParameter("end", end);

        return q.getResultList();
    }

    public void chargeWallet(int id, Double amount) {

        Passenger p = em.find(Passenger.class, id);
        p.addBalance(amount);
    }

    // 0 == successful, 1 == unsuccessful
    public int purchaseTicket(int passengerId, int tripId) {

        Passenger p = em.find(Passenger.class, passengerId);
        Trip trip = em.find(Trip.class, tripId);

        Double ticketPrice = trip.getTicketPrice();

        if (p.getBalance() > ticketPrice) {

            p.addBalance(ticketPrice * -1);
            Ticket t = new Ticket(p, trip);

            em.persist(t);
            return 0;
        }

        return 1;
    }

    public void refundTicket(int passengerId, int ticketId) {

        Passenger p = em.find(Passenger.class, passengerId);
        Ticket t = em.find(Ticket.class, ticketId);

        p.addBalance(t.getTrip().getTicketPrice());
        em.remove(t);
    }

    public List<Integer> listTrips(int passengerId) {

        Passenger p = em.find(Passenger.class, passengerId);

        List<Ticket> tickets = p.getTickets();
        List<Integer> t = new ArrayList<>();

        for (Ticket ticket : tickets)
            t.add(ticket.getTrip().getId());

        return t;
    }

}