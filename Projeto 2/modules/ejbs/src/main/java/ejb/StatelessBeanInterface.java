package ejb;

//import jpa.*;
import javax.ejb.Remote;

@Remote
public interface StatelessBeanInterface {
    public String getName(int id);
}