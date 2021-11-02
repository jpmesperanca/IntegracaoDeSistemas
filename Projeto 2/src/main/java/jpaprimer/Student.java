package jpaprimer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name, telephone;
    private int age;
    @ManyToOne(cascade = CascadeType.ALL)
    private Professor prof;

    public Student() {
    }

    public Student(String name, String telephone, int age, Professor prof) {
        this.name = name;
        this.telephone = telephone;
        this.age = age;
        this.prof = prof;
    }

    public String getTelephone() {
        return telephone;
    }

    public Professor getProf() {
        return prof;
    }

    public void setProf(Professor prof) {
        this.prof = prof;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String toString() {
        return this.name + ". Telephone: " + this.telephone + ". Age: " + this.age;
    }
}
