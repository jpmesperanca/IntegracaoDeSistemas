package jpaprimer;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Reader {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProfsAndStudents");
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("from Professor p");

        List<Professor> lp = q.getResultList();
        for (Professor p : lp) {
            System.out.println(p);
            for (Student s : p.getStudents())
                System.out.println(s);
        }
    }
    
}
