package ejb;

//import jpa.*;
import javax.ejb.Remote;

@Remote
public interface StatelessBeanInterface {
    public int getPassengerByEmail(String email);

    public void addPassenger(String email, String password, String name, String phoneNumber);

    public void addManager(String email, String password, String name, String phoneNumber);

    public void editPassenger(int id, String email, String password, String name, String phoneNumber);

    public void deletePassenger(int id);
}