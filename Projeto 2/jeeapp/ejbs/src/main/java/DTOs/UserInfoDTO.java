package DTOs;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private Double balance;
    private Integer numberOfTickets;

    public UserInfoDTO() {
    }

    public UserInfoDTO(String email, String name, String phoneNumber, Double balance) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    public UserInfoDTO(String email, String name, String phoneNumber, String password) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UserInfoDTO(String email, String name, String phoneNumber, Double balance, Integer numberOfTickets) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.numberOfTickets = numberOfTickets;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nEmail: " + this.email + "\nPhone Number: " + this.phoneNumber + "\n\n";
    }
}
