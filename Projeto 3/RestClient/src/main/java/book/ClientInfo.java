package book;

import java.io.Serializable;

public class ClientInfo implements Serializable {

    private String name;
    private Double balance;
    private Integer manager;

    public ClientInfo() {
    }

    public ClientInfo(String name, Double balance, Integer manager) {
        this.name = name;
        this.balance = balance;
        this.manager = manager;
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

    public Integer getManager() {
        return this.manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }
}