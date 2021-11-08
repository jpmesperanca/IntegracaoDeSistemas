package Client;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import EJBs.*;

public class Client {

    public static void main(String[] args) throws NamingException {

        InitialContext ctx = new InitialContext();

        // StatelessBean slb = (StatelessBeanInterface) ctx.lookup("slb");
        StatelessBeanInterface slb = (StatelessBeanInterface) ctx.lookup("slb");

        System.out.println("\nname of ID 1752" + slb.getName(1752) + "\n");
    }
}
