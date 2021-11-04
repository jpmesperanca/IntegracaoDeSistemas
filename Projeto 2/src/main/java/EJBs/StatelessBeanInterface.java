package EJBs;

//import jpaprimer.*;

import javax.ejb.Remote;

@Remote
public interface StatelessBeanInterface {
    public String getName(int id);
}