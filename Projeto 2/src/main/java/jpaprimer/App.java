package jpaprimer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("school");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        Professor[] myprofs = { 
            new Professor("José", "D3.1"), 
            new Professor("Paulo", "135"), 
            new Professor("Estrela", "180")
        };
        Student[] mystudents = { 
            new Student("Paula", "91999991", 21, myprofs[1]),
            new Student("Artur", "91999992", 21, myprofs[1]),
            new Student("Rui", "91999993", 19, myprofs[0]),
            new Student("Luísa", "91999994", 20, myprofs[0]),
            new Student("Alexandra", "91999995", 21, myprofs[0]),
            new Student("Carlos", "91999995", 22, myprofs[2])
        };

        et.begin();

        for (Student s : mystudents)
            em.persist(s);
        et.commit();
    }
}
