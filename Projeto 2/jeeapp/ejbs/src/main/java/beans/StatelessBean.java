package beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import DTOs.GregorianCalendarDTO;
import DTOs.TripInfoDTO;
import DTOs.UserInfoDTO;
import data.Manager;
import data.Passenger;
import data.Ticket;
import data.Trip;

@Stateless
public class StatelessBean {

    @PersistenceContext(name = "TripDB")
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(StatelessBean.class);

    public StatelessBean() {
    }

    /*
     * Password hashing adapted from
     * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-
     * hash-md5-sha-pbkdf2-bcrypt-examples/
     */
    private String hashPassword(String password) {

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void createData() {

        Trip[] trips = {
                // meses de 0 - 11
                new Trip(new GregorianCalendar(2000, 1, 10), "Coimbra", "Ponta Delgada", 30, 29.99),
                new Trip(new GregorianCalendar(1995, 5, 15), "Lisboa", "Porto", 100, 19.99),
                new Trip(new GregorianCalendar(2000, 11, 1), "Faro", "Berlim", 30, 39.99) };

        Passenger[] passengers = { new Passenger("1@jospy.com", hashPassword("123"), "NotAdmin1", "933333331", 100.0),
                new Passenger("2@jospy.com", hashPassword("123"), "NotAdmin2", "933333332", 100.0),
                new Passenger("3@jospy.com", hashPassword("123"), "NotAdmin3", "933333333", 0.0) };

        Manager[] managers = { new Manager("4@jospy.com", hashPassword("123"), "Admin1", "933333333"),
                new Manager("5@jospy.com", hashPassword("123"), "Admin2", "933333334"),
                new Manager("6@jospy.com", hashPassword("admin"), "Admin3", "933333335") };

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

    public void eraseAllData() {

        for (Integer tId : em.createQuery("select t.id from Trip t", Integer.class).getResultList())
            deleteTrip(tId);

        for (Integer pId : em.createQuery("select p.id from Passenger p", Integer.class).getResultList())
            deletePassenger(pId);

        for (Integer mId : em.createQuery("select m.id from Manager m", Integer.class).getResultList())
            deleteManager(mId);

        logger.info("Dados apagados!");
    }

    public List<Passenger> listPassengers() {

        logger.info("O LOGGER ESTA A FUNCIONAR!");

        TypedQuery<Passenger> q = em.createQuery("from Passenger p", Passenger.class);
        List<Passenger> l = q.getResultList();

        return l;
    }

    public void addPassenger(UserInfoDTO userInfo) {

        Passenger p = new Passenger(userInfo.getEmail(), userInfo.getPassword(), userInfo.getName(),
                userInfo.getPhoneNumber(), 0.0);
        em.persist(p);
    }

    public void addManager(String email, String password, String name, String phoneNumber) {

        Manager m = new Manager(email, password, name, phoneNumber);
        em.persist(m);
    }

    public String authenticate(String email, String password) {

        TypedQuery<Integer> q = em.createQuery(
                "select p.id from Passenger p where p.email = :email and p.password = :password", Integer.class);

        q.setParameter("email", email);
        q.setParameter("password", password);

        try {
            q.getSingleResult();
            return "passenger";

        } catch (Exception e) {
            try {
                q = em.createQuery("select m.id from Manager m where m.email = :email and m.password = :password",
                        Integer.class);

                q.setParameter("email", email);
                q.setParameter("password", password);

                q.getSingleResult();

                return "manager";

            } catch (Exception ex) {
                return "";
            }
        }
    }

    public int getPassengerByEmail(String email) {

        TypedQuery<Passenger> q = em.createQuery("from Passenger p where p.email = :email", Passenger.class);

        q.setParameter("email", email);

        return q.getSingleResult().getId();
    }

    public UserInfoDTO getPassengerInfoById(Integer id) {

        TypedQuery<Passenger> q = em.createQuery("from Passenger p where p.id = :id", Passenger.class);

        q.setParameter("id", id);

        Passenger p = q.getSingleResult();

        return new UserInfoDTO(p.getEmail(), p.getName(), p.getPhoneNumber());
    }

    public int getManagerByEmail(String email) {

        TypedQuery<Manager> q = em.createQuery("from Manager m where m.email = :email", Manager.class);

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

    public void deleteManager(int id) {
        em.remove(em.find(Manager.class, id));
    }

    public List<Integer> listTripsBetweenStartEndDate(GregorianCalendar start, GregorianCalendar end) {

        TypedQuery<Integer> q = em.createQuery(
                "select t.id from Trip t where t.departureDate >= :start and t.departureDate <= :end", Integer.class);

        q.setParameter("start", start);
        q.setParameter("end", end);

        return q.getResultList();
    }

    public TripInfoDTO getTripInfo(Integer id) {

        TypedQuery<Trip> q = em.createQuery("from Trip t where t.id :id", Trip.class);

        q.setParameter("id", id);

        Trip t = q.getSingleResult();

        return new TripInfoDTO(
                new GregorianCalendarDTO(t.getDepartureDate().get(GregorianCalendar.YEAR),
                        t.getDepartureDate().get(GregorianCalendar.MONTH),
                        t.getDepartureDate().get(GregorianCalendar.DAY_OF_MONTH)),
                t.getDeparturePoint(), t.getDestinationPoint(), t.getCapacity(), t.getTicketPrice());
    }

    public List<TripInfoDTO> listTripInfoBetweenStartEndDate(GregorianCalendarDTO start, GregorianCalendarDTO end) {
        TypedQuery<Trip> q = em.createQuery("from Trip t where t.departureDate >= :start and t.departureDate <= :end",
                Trip.class);

        GregorianCalendar startGreg = new GregorianCalendar(start.getYear(), start.getMonth(), start.getDay());
        GregorianCalendar endGreg = new GregorianCalendar(end.getYear(), end.getMonth(), end.getDay());

        q.setParameter("start", startGreg);
        q.setParameter("end", endGreg);

        List<Trip> trips = q.getResultList();
        List<TripInfoDTO> tripsDTO = new ArrayList<TripInfoDTO>();
        for (Trip t : trips) {
            GregorianCalendar departureDateGreg = t.getDepartureDate();
            GregorianCalendarDTO departureDateDTO = new GregorianCalendarDTO(
                    departureDateGreg.get(GregorianCalendar.YEAR), departureDateGreg.get(GregorianCalendar.MONTH),
                    departureDateGreg.get(GregorianCalendar.DAY_OF_MONTH));
            tripsDTO.add(new TripInfoDTO(departureDateDTO, t.getDeparturePoint(), t.getDestinationPoint(),
                    t.getCapacity(), t.getTicketPrice()));
        }

        return tripsDTO;
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

    public List<Integer> listTripsByPassengerId(int passengerId) {

        Passenger p = em.find(Passenger.class, passengerId);

        List<Ticket> tickets = p.getTickets();
        List<Integer> t = new ArrayList<>();

        for (Ticket ticket : tickets)
            t.add(ticket.getTrip().getId());

        return t;
    }

    public void addTrip(GregorianCalendar departureDate, String departurePoint, String destinationPoint, int capacity,
            Double ticketPrice) {
        Trip t = new Trip(departureDate, departurePoint, destinationPoint, capacity, ticketPrice);
        em.persist(t);
    }

    public void deleteTrip(int id) {
        Trip t = em.find(Trip.class, id);

        List<Ticket> tickets = t.getTickets();
        int pId;

        for (Ticket ticket : tickets) {
            pId = ticket.getPassenger().getId();
            refundTicket(pId, ticket.getId());
            em.remove(ticket);
        }
        em.remove(t);
    }

    public List<Integer> listTop5Passengers() {
        TypedQuery<Integer> q = em.createQuery(
                "select t.passenger_id from Ticket t group by (t.passenger_id) order by count(t.passenger_id) DESC LIMIT 5",
                Integer.class);

        return q.getResultList();
    }

    public List<Integer> listTripsByDepartureDate(GregorianCalendar departureDate) {

        TypedQuery<Integer> q = em.createQuery("select t.id from Trip t where t.departureDate = :departureDate",
                Integer.class);

        q.setParameter("departureDate", departureDate);

        return q.getResultList();
    }

    public List<Integer> listPassengersByTripId(int tripId) {
        /*
         * Trip t = em.find(Trip.class, tripId); List<Ticket> allTripTickets =
         * t.getTickets();
         * 
         * List<Integer> passengerIds = new ArrayList<Integer>();
         * 
         * for (Ticket ticket : allTripTickets)
         * passengerIds.add(ticket.getPassenger().getId());
         */

        // Same thing, less lines. O de cima é mais readable mas o de baixo tem menos
        // linhas a inuteis

        List<Integer> passengerIds = new ArrayList<Integer>();

        for (Ticket ticket : em.find(Trip.class, tripId).getTickets())
            passengerIds.add(ticket.getPassenger().getId());

        return passengerIds;
    }
}