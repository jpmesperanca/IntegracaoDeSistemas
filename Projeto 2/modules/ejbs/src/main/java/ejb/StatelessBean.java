package ejb;

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

    public String getName(int id) {

        TypedQuery<Person> q = em.createQuery("from Person p where p.id = :id", Person.class);

        q.setParameter("id", id);

        Person p = q.getSingleResult();
        // Person p = em.find(Person.class, id); -> este funciona pq é por id

        return p.getName();
    }
}