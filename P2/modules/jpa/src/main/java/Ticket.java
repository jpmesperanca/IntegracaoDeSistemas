package jpaprimer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Trip trip;

    public Ticket() {}
    
    public Ticket(Person person, Trip trip) {
        this.person = person;
        this.trip = trip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
