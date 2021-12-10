package book;

import java.io.Serializable;

public class ClientInfo implements Serializable {

    private String name;

    private Double balance;
    private Double payments;
    private Double credits;

    private Integer manager;

    public ClientInfo() {
        this.balance = 0.0;
        this.payments = 0.0;
        this.credits = 0.0;
    }

    public ClientInfo(String name, Integer manager) {
        this.name = name;
        this.manager = manager;
        this.balance = 0.0;
        this.payments = 0.0;
        this.credits = 0.0;
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

    public Integer getManager() {
        return this.manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }
}