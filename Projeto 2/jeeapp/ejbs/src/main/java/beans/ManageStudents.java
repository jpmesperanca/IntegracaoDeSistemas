package beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import data.Student;

@Stateless
public class ManageStudents {
    @PersistenceContext(unitName = "playAula")
    EntityManager em;

    public void addStudent(String name) {
        System.out.println("Adding student " + name + "...");
        Student s = new Student(name);
        em.persist(s);
    }

    public List<Student> listStudents() {
        System.out.println("Retrieving students from the database...");
        TypedQuery<Student> q = em.createQuery("from Student s", Student.class);
        List<Student> list = q.getResultList();
        return list;
    }
}