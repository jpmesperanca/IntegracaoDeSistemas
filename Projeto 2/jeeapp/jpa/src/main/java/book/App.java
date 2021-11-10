package book;

import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import data.Manager;
import data.Passenger;
import data.Ticket;
import data.Trip;

public class App {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TripDB");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

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

        et.begin();

        for (Trip t : trips)
            em.persist(t);

        for (Passenger u : passengers)
            em.persist(u);

        for (Manager m : managers)
            em.persist(m);

        et.commit();

        et.begin();

        for (Ticket t : tickets)
            em.persist(t);

        et.commit();
    }
}