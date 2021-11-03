package EJBs;

import java.util.List;
import jpaprimer.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class NotBean {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TripDB");
        EntityManager em = emf.createEntityManager();

        // Logger logger = LoggerFactory.getLogger(NotBean.class);

        TypedQuery<Person> q = em.createQuery("from Person p", Person.class);

        List<Person> lp = q.getResultList();

        for (Person p : lp) {

            // logger.info(p.getName());

            for (Ticket t : p.getTickets())
                System.out.println(t.getTrip().getDestinationPoint());
        }
    }
}
