package data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private Double balance;
    private Double payments;
    private Double credits;

    @ManyToOne
    private Manager manager;

    public Client() {
        this.balance = 0.0;
        this.payments = 0.0;
        this.credits = 0.0;
    }

    public Client(String name, Manager manager) {
        this.name = name;
        this.manager = manager;
        this.balance = 0.0;
        this.payments = 0.0;
        this.credits = 0.0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getPayments() {
        return this.payments;
    }

    public void setPayments(Double amount) {
        this.payments = amount;
    }

    public Double getCredits() {
        return this.credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

}