package beans;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import DTOs.GregorianCalendarDTO;
import DTOs.TicketInfoDTO;
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

    @Resource(lookup = "java:jboss/mail/gmail") // Nome do Recurso que criamos no Wildfly

    private Session mailSession;
    private String emailFrom = "integracaodesistemas2021@gmail.com";

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
                new Trip(new GregorianCalendar(2021, 11, 1, 12, 30), "Coimbra", "Ponta Delgada", 30, 29.99),
                new Trip(new GregorianCalendar(2021, 5, 15, 11, 45), "Lisboa", "Porto", 100, 19.99),
                new Trip(new GregorianCalendar(2020, 11, 1, 23, 30), "Faro", "Berlim", 30, 39.99) };

        Passenger[] passengers = {
                new Passenger("1@teste.com", hashPassword("123"), "TestPassenger1", "933333331", 100.0),
                new Passenger("goncalomarcos@hotmail.com", hashPassword("123"), "Goncalo", "933333332", 100.0),
                new Passenger("2@teste.com", hashPassword("123"), "TestPassenger2", "933333332", 100.0),
                new Passenger("3@teste.com", hashPassword("123"), "TestPassenger3", "933333333", 0.0) };

        Manager[] managers = { new Manager("4@teste.com", hashPassword("123"), "TestManager1", "933333334"),
                new Manager("5@teste.com", hashPassword("123"), "TestManager2", "933333335"),
                new Manager("6@teste.com", hashPassword("123"), "TestManager3", "933333336") };

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

        logger.info("Dados criados!");
    }

    public void eraseAllData() {

        for (Integer tId : em.createQuery("select t.id from Trip t", Integer.class).getResultList())
            deleteTripForEraseAllData(tId);

        for (Integer pId : em.createQuery("select p.id from Passenger p", Integer.class).getResultList())
            deletePassenger(pId);

        for (Integer mId : em.createQuery("select m.id from Manager m", Integer.class).getResultList())
            deleteManager(mId);

        logger.info("Dados apagados!");
    }

    public List<Passenger> listPassengers() {

        logger.info("Listing passengers...");

        TypedQuery<Passenger> q = em.createQuery("from Passenger p", Passenger.class);

        List<Passenger> l = q.getResultList();

        logger.info("Passengers listed successfully!");

        return l;
    }

    public int addPassenger(UserInfoDTO userInfo) {

        logger.info("Adding passenger...");

        TypedQuery<Passenger> q = em.createQuery("from Passenger p where p.email = :email", Passenger.class);
        q.setParameter("email", userInfo.getEmail());

        try {
            q.getSingleResult();
            logger.info("Passenger already exists, aborting!");
            return 1;
        } catch (NoResultException e) {
            Passenger p = new Passenger(userInfo.getEmail(), userInfo.getPassword(), userInfo.getName(),
                    userInfo.getPhoneNumber(), 0.0);
            em.persist(p);
            logger.info("Passengers added successfully!");
            return 0;
        }

    }

    public String authenticate(String email, String password) {

        TypedQuery<Integer> q = em.createQuery(
                "select p.id from Passenger p where p.email = :email and p.password = :password", Integer.class);

        q.setParameter("email", email);
        q.setParameter("password", password);

        logger.info("Authenticating...");

        try {
            q.getSingleResult();
            logger.info("Logged in passenger!");
            return "passenger";

        } catch (Exception e) {
            try {
                q = em.createQuery("select m.id from Manager m where m.email = :email and m.password = :password",
                        Integer.class);

                q.setParameter("email", email);
                q.setParameter("password", password);

                q.getSingleResult();

                logger.info("Logged in manager!");

                return "manager";

            } catch (Exception ex) {
                logger.error("LINE 199 ERROR!!");
                return "";
            }
        }
    }

    public int getPassengerByEmail(String email) {

        TypedQuery<Passenger> q = em.createQuery("from Passenger p where p.email = :email", Passenger.class);

        q.setParameter("email", email);

        logger.info("Got passenger's id by email!");

        return q.getSingleResult().getId();
    }

    public UserInfoDTO getPassengerInfoById(Integer id) {

        TypedQuery<Passenger> q = em.createQuery("from Passenger p where p.id = :id", Passenger.class);

        q.setParameter("id", id);

        Passenger p = q.getSingleResult();

        logger.info("Got passenger's info by id!");

        return new UserInfoDTO(p.getEmail(), p.getName(), p.getPhoneNumber(), p.getBalance());
    }

    public int getManagerByEmail(String email) {

        TypedQuery<Manager> q = em.createQuery("from Manager m where m.email = :email", Manager.class);

        q.setParameter("email", email);

        logger.info("Got manager's id by email!");

        return q.getSingleResult().getId();
    }

    public void editPassenger(Integer id, UserInfoDTO userInfo) {

        String email = userInfo.getEmail();
        String password = userInfo.getPassword();
        String name = userInfo.getName();
        String phoneNumber = userInfo.getPhoneNumber();

        Passenger p = em.find(Passenger.class, id);

        if (email != null) {
            p.setEmail(email);
            logger.info("Changed passenger " + id + "'s email!");
        }

        if (password != null) {
            p.setPassword(password);
            logger.info("Changed passenger " + id + "'s password!");
        }

        if (name != null) {
            p.setName(name);
            logger.info("Changed passenger " + id + "'s name!");
        }

        if (phoneNumber != null) {
            p.setPhoneNumber(phoneNumber);
            logger.info("Changed passenger " + id + "'s phoneNumber!");
        }
    }

    public void deletePassenger(int id) {
        logger.info("Passenger " + id + " deleted");
        em.remove(em.find(Passenger.class, id));
    }

    public void deleteManager(int id) {
        logger.info("Manager " + id + " deleted");
        em.remove(em.find(Manager.class, id));
    }

    public TripInfoDTO getTripInfo(Integer id) {

        TypedQuery<Trip> q = em.createQuery("from Trip t where t.id = :id", Trip.class);

        q.setParameter("id", id);

        Trip t = q.getSingleResult();

        logger.info("Trip " + id + "'s info successfully returned!");

        return new TripInfoDTO(new GregorianCalendarDTO(t.getDepartureDate().get(GregorianCalendar.YEAR),
                t.getDepartureDate().get(GregorianCalendar.MONTH),
                t.getDepartureDate().get(GregorianCalendar.DAY_OF_MONTH),
                t.getDepartureDate().get(GregorianCalendar.HOUR), t.getDepartureDate().get(GregorianCalendar.MINUTE)),
                t.getDeparturePoint(), t.getDestinationPoint(), t.getCapacity(), t.getTicketPrice(), t.getId());
    }

    public List<TripInfoDTO> listTripInfoBetweenStartEndDate(GregorianCalendarDTO start, GregorianCalendarDTO end) {
        TypedQuery<Trip> q = em.createQuery(
                "from Trip t where t.departureDate >= :start and t.departureDate <= :end order by t.departureDate asc",
                Trip.class);

        GregorianCalendar startGreg = new GregorianCalendar(start.getYear(), start.getMonth(), start.getDay(),
                start.getHours(), start.getMinutes());
        GregorianCalendar endGreg = new GregorianCalendar(end.getYear(), end.getMonth(), end.getDay(), end.getHours(),
                end.getMinutes());

        q.setParameter("start", startGreg);
        q.setParameter("end", endGreg);

        List<Trip> trips = q.getResultList();
        List<TripInfoDTO> tripsDTO = new ArrayList<TripInfoDTO>();
        for (Trip t : trips) {
            GregorianCalendar departureDateGreg = t.getDepartureDate();
            GregorianCalendarDTO departureDateDTO = new GregorianCalendarDTO(
                    departureDateGreg.get(GregorianCalendar.YEAR), departureDateGreg.get(GregorianCalendar.MONTH),
                    departureDateGreg.get(GregorianCalendar.DAY_OF_MONTH),
                    departureDateGreg.get(GregorianCalendar.HOUR), departureDateGreg.get(GregorianCalendar.MINUTE));
            tripsDTO.add(new TripInfoDTO(departureDateDTO, t.getDeparturePoint(), t.getDestinationPoint(),
                    t.getCapacity(), t.getTicketPrice(), t.getId()));
        }

        logger.info("Trips between dates listed successfully!");

        return tripsDTO;
    }

    public List<TripInfoDTO> listFutureTripInfoBetweenStartEndDate(GregorianCalendarDTO start,
            GregorianCalendarDTO end) {
        TypedQuery<Trip> q = em.createQuery(
                "from Trip t where t.departureDate >= :start and t.departureDate <= :end order by t.departureDate asc",
                Trip.class);

        GregorianCalendar startGreg = new GregorianCalendar(start.getYear(), start.getMonth(), start.getDay(),
                start.getHours(), start.getMinutes());
        GregorianCalendar endGreg = new GregorianCalendar(end.getYear(), end.getMonth(), end.getDay(), end.getHours(),
                end.getMinutes());

        q.setParameter("start", startGreg);
        q.setParameter("end", endGreg);

        List<Trip> trips = q.getResultList();
        List<TripInfoDTO> tripsDTO = new ArrayList<TripInfoDTO>();
        Calendar nowCal = new GregorianCalendar();

        for (Trip t : trips) {

            if (t.getDepartureDate().compareTo(nowCal) >= 0) {
                GregorianCalendar departureDateGreg = t.getDepartureDate();
                GregorianCalendarDTO departureDateDTO = new GregorianCalendarDTO(
                        departureDateGreg.get(GregorianCalendar.YEAR), departureDateGreg.get(GregorianCalendar.MONTH),
                        departureDateGreg.get(GregorianCalendar.DAY_OF_MONTH),
                        departureDateGreg.get(GregorianCalendar.HOUR), departureDateGreg.get(GregorianCalendar.MINUTE));
                tripsDTO.add(new TripInfoDTO(departureDateDTO, t.getDeparturePoint(), t.getDestinationPoint(),
                        t.getCapacity(), t.getTicketPrice(), t.getId()));
            }
        }

        logger.info("Future trips between dates listed successfully!");

        return tripsDTO;
    }

    public void chargeWallet(int id, Double amount) {

        Passenger p = em.find(Passenger.class, id);
        p.addBalance(amount);

        logger.info("Passenger " + id + "'s wallet charged with " + amount + " EUR.");
    }

    // 0 == successful, 1 == unsuccessful
    public int purchaseTicket(int passengerId, int tripId) {

        Passenger p = em.find(Passenger.class, passengerId);
        Trip trip = em.find(Trip.class, tripId);

        Double ticketPrice = trip.getTicketPrice();
        Calendar nowCal = new GregorianCalendar();

        if (p.getBalance() >= ticketPrice && trip.getDepartureDate().compareTo(nowCal) > 0 && trip.getCapacity() > 0) {

            p.addBalance(ticketPrice * -1);
            Ticket t = new Ticket(p, trip);

            em.persist(t);

            trip.setCapacity(trip.getCapacity() - 1);
            logger.info("Ticket purchase sucessful");
            return 0;
        }
        logger.info("Ticket purchase unsuccessful - not enough money, no capacity or past trip.");
        return 1;
    }

    public int refundTicket(int passengerId, int ticketId) {

        Passenger p = em.find(Passenger.class, passengerId);
        Ticket t = em.find(Ticket.class, ticketId);

        Trip trip = t.getTrip();
        Calendar nowCal = new GregorianCalendar();

        if (trip.getDepartureDate().compareTo(nowCal) >= 0) {
            p.addBalance(t.getTrip().getTicketPrice());
            em.remove(t);
            logger.info("Ticket refund sucessful");
            return 0;
        }
        logger.info("Ticket refund unsuccessful - past trip.");
        return 1;
    }

    public List<Integer> listTripsByPassengerId(int passengerId) {

        Passenger p = em.find(Passenger.class, passengerId);

        List<Ticket> tickets = p.getTickets();
        List<Integer> t = new ArrayList<>();

        for (Ticket ticket : tickets)
            t.add(ticket.getTrip().getId());

        logger.info("Trips retrieved successfully!");
        return t;
    }

    public int addTrip(TripInfoDTO tripInfo) {

        GregorianCalendarDTO depCal = tripInfo.getDepartureDate();
        String departurePoint = tripInfo.getDeparturePoint();
        String destinationPoint = tripInfo.getDestinationPoint();
        int capacity = tripInfo.getCapacity();
        double ticketPrice = tripInfo.getTicketPrice();

        GregorianCalendar g = new GregorianCalendar(depCal.getYear(), depCal.getMonth(), depCal.getDay(),
                depCal.getHours(), depCal.getMinutes());
        Calendar nowCal = new GregorianCalendar();

        if (g.compareTo(nowCal) >= 0) {
            Trip t = new Trip(g, departurePoint, destinationPoint, capacity, ticketPrice);
            em.persist(t);
            logger.info("Trip added successfully!");
            return 0;
        }

        logger.info("Trips added unsuccessfully - past trip.");
        return 1;
    }

    public int deleteTrip(int id) {
        Trip t = em.find(Trip.class, id);

        List<Ticket> tickets = t.getTickets();
        int pId;
        String emailSubject, emailContent;
        Passenger p;

        Calendar nowCal = new GregorianCalendar();

        if (t.getDepartureDate().compareTo(nowCal) >= 0) {
            for (Ticket ticket : tickets) {
                pId = ticket.getPassenger().getId();
                refundTicket(pId, ticket.getId());

                // send email
                p = em.find(Passenger.class, pId);
                emailSubject = "IS2021 Refund Ticket ->  " + t.getDeparturePoint() + " to " + t.getDestinationPoint();
                emailContent = "Greetings " + p.getName().split(" ")[0]
                        + ",\n\nA manager has just deleted a future bus trip you had planned. Here's the info about the trip:\n"
                        + t.toString() + "\nYou will be refunded with " + t.getTicketPrice()
                        + " EUR.\nSorry for the inconvenience.\n\nBest Regards,\nIS2021 Team";
                sendEmail(p.getEmail(), this.emailFrom, emailSubject, emailContent);

                em.remove(ticket);
            }
            em.remove(t);
            logger.info("Trip deleted successfully!");
            return 0;
        }

        logger.info("Can't delete past trip.");
        return 1;
    }

    public void deleteTripForEraseAllData(int id) {
        Trip t = em.find(Trip.class, id);

        List<Ticket> tickets = t.getTickets();
        int pId;

        for (Ticket ticket : tickets) {
            pId = ticket.getPassenger().getId();
            refundTicket(pId, ticket.getId());

            em.remove(ticket);
        }
        em.remove(t);
        logger.info("Trip deleted successfully!");
    }

    public List<UserInfoDTO> listTop5Passengers() {
        TypedQuery<Integer> q = em.createQuery(
                "select t.passenger.id from Ticket t group by (t.passenger.id) order by count(t.passenger.id) DESC",
                Integer.class).setMaxResults(5);

        List<Integer> top = q.getResultList();

        TypedQuery<Long> qq = em.createQuery(
                "select count(t.passenger.id) from Ticket t group by (t.passenger.id) order by count(t.passenger.id) DESC",
                Long.class).setMaxResults(5);

        List<Long> ticketCount = qq.getResultList();
        List<UserInfoDTO> topInfo = new ArrayList<>();

        for (int i = 0; i < top.size(); i++) {

            UserInfoDTO userInfo = getPassengerInfoById(top.get(i));
            userInfo.setNumberOfTickets(ticketCount.get(i).intValue());
            topInfo.add(userInfo);
        }

        logger.info("Top 5 listed successfully!");
        return topInfo;
    }

    public Integer getNumberOfTickets(Integer uId) {

        TypedQuery<Integer> q = em.createQuery(
                "select count(t.passenger.id) from Ticket t group by (t.passenger.id) having t.passenger.id = :uId order by count(t.passenger.id) DESC",
                Integer.class);

        q.setParameter("uId", uId);

        logger.info("Calculated number of tickets successfully!");

        return q.getSingleResult();
    }

    public List<Integer> listTripsByDepartureDate(GregorianCalendar departureDate) {

        TypedQuery<Integer> q = em.createQuery("select t.id from Trip t where t.departureDate = :departureDate",
                Integer.class);

        q.setParameter("departureDate", departureDate);

        logger.info("Retrieved trips by date successfully!");

        return q.getResultList();
    }

    // Does not use hours and minutes!
    public List<TripInfoDTO> listTripInfoByDepartureDate(GregorianCalendarDTO departureDate) {

        TypedQuery<Trip> q = em.createQuery(
                "from Trip t where t.departureDate >= :departureStartDate and t.departureDate <= :departureEndDate",
                Trip.class);

        GregorianCalendar departureStartDateGregorian = new GregorianCalendar(departureDate.getYear(),
                departureDate.getMonth(), departureDate.getDay(), 0, 0);

        GregorianCalendar departureEndDateGregorian = new GregorianCalendar(departureDate.getYear(),
                departureDate.getMonth(), departureDate.getDay(), 23, 59);

        q.setParameter("departureStartDate", departureStartDateGregorian);
        q.setParameter("departureEndDate", departureEndDateGregorian);

        List<Trip> trips = q.getResultList();
        List<TripInfoDTO> tripsDTO = new ArrayList<TripInfoDTO>();

        for (Trip t : trips) {
            GregorianCalendar departureDateGreg = t.getDepartureDate();
            GregorianCalendarDTO departureDateDTO = new GregorianCalendarDTO(
                    departureDateGreg.get(GregorianCalendar.YEAR), departureDateGreg.get(GregorianCalendar.MONTH),
                    departureDateGreg.get(GregorianCalendar.DAY_OF_MONTH),
                    departureDateGreg.get(GregorianCalendar.HOUR), departureDateGreg.get(GregorianCalendar.MINUTE));
            tripsDTO.add(new TripInfoDTO(departureDateDTO, t.getDeparturePoint(), t.getDestinationPoint(),
                    t.getCapacity(), t.getTicketPrice(), t.getId()));
        }

        logger.info("Retrieved trip info by date successfully!");

        return tripsDTO;
    }

    public List<UserInfoDTO> listPassengersByTripId(int tripId) {
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
        List<UserInfoDTO> passengerInfos = new ArrayList<>();

        for (Ticket ticket : em.find(Trip.class, tripId).getTickets())
            passengerIds.add(ticket.getPassenger().getId());

        for (Integer uId : passengerIds)
            passengerInfos.add(getPassengerInfoById(uId));

        logger.info("Retrieved user info by trip id successfully!");

        return passengerInfos;
    }

    public List<TripInfoDTO> listTripsByPassengerId(Integer id) {
        TypedQuery<Trip> q = em.createQuery(
                "Select tr FROM Trip tr INNER JOIN Ticket ti ON ti.trip.id = tr.id WHERE ti.passenger.id = :id",
                Trip.class);
        q.setParameter("id", id);

        List<Trip> trips = q.getResultList();
        List<TripInfoDTO> tripsDTO = new ArrayList<TripInfoDTO>();
        for (Trip t : trips) {
            GregorianCalendar departureDateGreg = t.getDepartureDate();
            GregorianCalendarDTO departureDateDTO = new GregorianCalendarDTO(
                    departureDateGreg.get(GregorianCalendar.YEAR), departureDateGreg.get(GregorianCalendar.MONTH),
                    departureDateGreg.get(GregorianCalendar.DAY_OF_MONTH),
                    departureDateGreg.get(GregorianCalendar.HOUR), departureDateGreg.get(GregorianCalendar.MINUTE));
            tripsDTO.add(new TripInfoDTO(departureDateDTO, t.getDeparturePoint(), t.getDestinationPoint(),
                    t.getCapacity(), t.getTicketPrice(), t.getId()));
        }

        logger.info("Retrieved all trips for passenger " + id + " successfully!");
        return tripsDTO;
    }

    public List<TicketInfoDTO> listTicketsByPassengerId(Integer id) {
        TypedQuery<Ticket> q = em.createQuery("FROM Ticket t WHERE t.passenger.id = :id", Ticket.class);
        q.setParameter("id", id);

        List<Ticket> tickets = q.getResultList();
        List<TicketInfoDTO> ticketsDTO = new ArrayList<TicketInfoDTO>();

        for (Ticket t : tickets)
            ticketsDTO.add(new TicketInfoDTO(t.getId(), t.getTrip().getId(), t.getPassenger().getId()));

        logger.info("Retrieved tickets by passenger id successfully!");
        return ticketsDTO;
    }

    public Integer getTicketFromTrip(Integer userId, Integer tripId) {

        TypedQuery<Integer> q = em.createQuery(
                "SELECT t.id FROM Ticket t WHERE t.passenger.id = :userId and t.trip.id = :tripId", Integer.class);

        q.setMaxResults(1);
        q.setParameter("userId", userId);
        q.setParameter("tripId", tripId);

        logger.info("Retrieved ticket from trip successfully");

        return q.getSingleResult();
    }

    /*
     * Código adaptado de
     * http://bhtecnonerd.blogspot.com/2014/12/configuracao-de-servico-de-email-no.
     * html
     */
    @Asynchronous
    public void sendEmail(String to, String from, String subject, String content) {

        logger.info("Email enviado por " + from + " para " + to + " : " + subject);
        try {
            // Criação de uma mensagem simples
            Message message = new MimeMessage(mailSession);
            // Cabeçalho do Email
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            // Corpo do email
            message.setText(content);

            // Envio da mensagem
            Transport.send(message);
            logger.info("Email enviado");
        } catch (MessagingException e) {
            logger.error("Erro a enviar o email : " + e.getMessage());
        }
    }

    @Schedule(hour = "3", minute = "15", second = "0")
    public void dailyRevenueReport() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime today = LocalDateTime.now();
        GregorianCalendar today00 = new GregorianCalendar(today.getYear(), today.getMonthValue() - 1,
                today.getDayOfMonth(), 00, 00);
        GregorianCalendar tomorrow00 = new GregorianCalendar(today.getYear(), today.getMonthValue() - 1,
                today.getDayOfMonth() + 1, 23, 59);

        TypedQuery<Manager> q1 = em.createQuery("from Manager m", Manager.class);
        List<Manager> managers = q1.getResultList();

        Query q2 = em.createQuery(
                "SELECT SUM(tr.ticketPrice) FROM Ticket ti INNER JOIN Trip tr ON tr.id = ti.trip.id WHERE tr.departureDate >= :today and tr.departureDate <= :tomorrow");

        q2.setParameter("today", today00);
        q2.setParameter("tomorrow", tomorrow00);

        Number totalRevenue = (Number) q2.getSingleResult();

        if (totalRevenue == null)
            totalRevenue = 0;

        String emailSubject, emailContent;
        emailSubject = "Daily Revenue Report -> " + dtf.format(today);

        for (Manager m : managers) {
            emailContent = "Greetings " + m.getName().split(" ")[0] + ",\n\nToday's total revenue was: " + totalRevenue
                    + "EUR.\n\nBest Regards,\nIS2021 Team";
            sendEmail(m.getEmail(), "integracaodesistemas2021@gmail.com", emailSubject, emailContent);
            logger.info("Email timed event completed successfully!");
        }
    }
}