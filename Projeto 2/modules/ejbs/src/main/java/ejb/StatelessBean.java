package ejb;

import javax.ejb.Stateless;
//import javax.ejb.PostActivate;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import jpa.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Stateless(mappedName = "slb")
public class StatelessBean implements StatelessBeanInterface {

    EntityManager em;

    public StatelessBean() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TripDB");
        em = emf.createEntityManager();
    }

    /*
     * @PostActivate public void connectToDB() { EntityManagerFactory emf =
     * Persistence.createEntityManagerFactory("TripDB"); em =
     * emf.createEntityManager(); }
     */

    public String getName(int id) {

        TypedQuery<Person> q = em.createQuery("from Person p where p.id = :id", Person.class);

        q.setParameter("id", id);

        return q.getSingleResult().getName();
    }
}