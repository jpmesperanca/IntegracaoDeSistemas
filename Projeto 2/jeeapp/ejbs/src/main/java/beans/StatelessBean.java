package beans;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import data.Manager;
import data.Passenger;
import data.Ticket;
import data.Trip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(mappedName = "slb")
public class StatelessBean {

    @PersistenceContext(name = "TripDB")
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(StatelessBean.class);

    public StatelessBean() {
    }

    public void createData() {

        Trip[] trips = {
                // meses de 0 - 11
                new Trip(new GregorianCalendar(2000, 1, 10), "Coimbra", "Ponta Delgada", 30, 29.99),
                new Trip(new GregorianCalendar(1995, 5, 15), "Lisboa", "Porto", 100, 19.99),
                new Trip(new GregorianCalendar(2000, 11, 1), "Faro", "Berlim", 30, 39.99) };

        Passenger[] passengers = { new Passenger("1@jospy.com", "123", "NotAdmin1", "933333331", 100.0),
                new Passenger("2@jospy.com", "123", "NotAdmin2", "933333332", 100.0),
                new Passenger("3@jospy.com", "123", "NotAdmin3", "933333333", 0.0) };

        Manager[] managers = { new Manager("4@jospy.com", "123", "Admin1", "933333333"),
                new Manager("5@jospy.com", "123", "Admin2", "933333334"),
                new Manager("6@jospy.com", "123", "Admin3", "933333335") };

        Ticket[] tickets = { new Ticket(passengers[0], trips[0]), new Ticket(passengers[1], trips[1]),
                new Ticket(passengers[2], trips[2]) };

        for (Trip t : trips)
            em.persist(t);

        for (Passenger u : passengers)
            em.persist(u);

        for (Manager m : managers)
            em.persist(m);

        for (Ticket t : tickets)
            em.persist(t);
    }

    public List<Passenger> listPassengers() {

        logger.info("O LOGGER ESTA A FUNCIONAR!");

        TypedQuery<Passenger> q = em.createQuery("from Passenger p", Passenger.class);
        List<Passenger> l = q.getResultList();

        return l;
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