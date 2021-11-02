package jpaprimer;

import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class App 
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TripDB");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        Trip[] trips = { 
            // meses de 0 - 11
            new Trip(new GregorianCalendar(2000, 1 , 10), "Coimbra", "Ponta Delgada", 30, 29.99), 
            new Trip(new GregorianCalendar(1995, 5 , 15), "Lisboa", "Porto", 100, 19.99), 
            new Trip(new GregorianCalendar(2000, 11 , 1), "Faro", "Berlim", 30, 39.99)
        };

        Person[] Persons = { 
            new Person("1@jospy.com", "123", "Admin1", "933333331", 100, true),
            new Person("2@jospy.com", "123", "Admin2", "933333332", 100, true),
            new Person("3@jospy.com", "123", "Admin3", "933333333", 0, true),
            new Person("4@jospy.com", "123", "NotAdmin1", "933333333", 100, false),
            new Person("5@jospy.com", "123", "NotAdmin2", "933333334", 100, false),
            new Person("6@jospy.com", "123", "NotAdmin3", "933333335", 0, false)
        };
        
        Ticket[] tickets = { 
            new Ticket(Persons[0], trips[0]), 
            new Ticket(Persons[1], trips[1]), 
            new Ticket(Persons[2], trips[2])
        };

        et.begin();

        for (Trip t : trips)
            em.persist(t);

        for (Person u : Persons)
            em.persist(u);

        et.commit();

        et.begin();

        for (Ticket t : tickets)
            em.persist(t);

        et.commit();
    }
}
