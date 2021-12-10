package book;

import java.util.List;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class App {
    public static void main(String[] args) {

        /*
         * // Client client = ClientBuilder.newClient();
         * 
         * // WebTarget target =
         * //
         * client.target("http://localhost:8080/rest/services/myservice/listManagers");
         * // Response response = target.request().get();
         * // List<ManagerInfo> personList = response.readEntity(new
         * // GenericType<List<ManagerInfo>>() {
         * // });
         * // for (ManagerInfo m : personList) {
         * // System.out.println("Name: " + m.getName());
         * // }
         * // response.close();
         * 
         * 
         * WebTarget target =
         * client.target("http://localhost:8080/restws/rest/myservice/person1");
         * Response response = target.request().get();
         * String value = response.readEntity(String.class);
         * System.out.println("RESPONSE1: " + value);
         * response.close();
         * 
         * target =
         * client.target("http://localhost:8080/restws/rest/myservice/person2");
         * target = target.queryParam("name", "xpto");
         * response = target.request().get();
         * value = response.readEntity(String.class);
         * System.out.println("RESPONSE2: " + value);
         * response.close();
         * 
         * target =
         * client.target("http://localhost:8080/restws/rest/myservice/person3/xpto");
         * response = target.request().get();
         * value = response.readEntity(String.class);
         * System.out.println("RESPONSE3: " + value);
         * response.close();
         * 
         * target =
         * client.target("http://localhost:8080/restws/rest/myservice/person5");
         * // response = target.request().post(input);
         * // p = response.readEntity(Person.class);
         * // System.out.println("RESPONSE5: " + p.getName() + " " + p.getAge());
         * response.close();
         * 
         * target =
         * client.target("http://localhost:8080/restws/rest/myservice/person6");
         * response = target.request().get();
         * // List<Person> personList = response.readEntity(new
         * GenericType<List<Person>>()
         * // {
         * // });
         * // System.out.println("RESPONSE6: " + personList);
         * response.close();
         */

        /*
         * WebTarget target =
         * client.target("http://localhost:8080/rest/services/myservice/addManager");
         * ManagerInfo m = new ManagerInfo("ManagerTesteR");
         * Entity<ManagerInfo> input = Entity.entity(m, MediaType.APPLICATION_JSON);
         * System.out.println(input);
         * Response response = target.request().post(input);
         * String value = response.readEntity(String.class);
         * System.out.println("RESPONSE4: " + value);
         * response.close();
         * }
         * 
         * 
         * WebTarget target =
         * client.target("http://localhost:8080/rest/services/myservice/listManagers");
         * Response response = target.request().get();
         * List<ManagerInfo> personList = response.readEntity(new
         * GenericType<List<ManagerInfo>>() {
         * });
         * System.out.println("RESPONSE6: " + personList);
         * response.close();
         */

        String _div = "--------------------------------------------------";
        String _lotsOfWhiteSpaces = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
        Client client = ClientBuilder.newClient();

        Scanner scan = new Scanner(System.in);
        int num, i;

        do {

            System.out.println("-------- ADMIN CLI --------");
            System.out.println("1. Add manager");
            System.out.println("2. Add client");
            System.out.println("3. Add curency");
            System.out.println();
            System.out.println("4. List managers");
            System.out.println("5. List clients");
            System.out.println("6. List currencies");
            System.out.println();
            System.out.println("7. Get credit per client");
            System.out.println("8. Get payments per client");
            System.out.println("9. Get current balance of a client");
            System.out.println("10. Get total sum of credits");
            System.out.println("11. Get total sum of payments");
            System.out.println("12. Get total balance");
            System.out.println("13. Compute bill of each client for the last \"month\"");
            System.out.println("14. Get list of clients without payments (last two \"months\")");
            System.out.println("15. Get person with highest outstanding debt");
            System.out.println("16. Get manager with highest revenue in payments");

            System.out.println();
            System.out.println("17. Exit");
            System.out.print("Choose an option: ");

            try {
                num = scan.nextInt();
            } catch (Exception e) {
                num = -1;
            }

            switch (num) {
                case 1: // Add Manager
                    // To simplify managers cannot be deleted and optionally not changed.
                    System.out.println(_lotsOfWhiteSpaces);

                    System.out.println("-------- New Manager --------\n");
                    System.out.print("Name: ");
                    // TODO - protecao contra nomes ilegais
                    scan.nextLine();
                    String managerName = scan.nextLine();
                    ManagerInfo newManager = new ManagerInfo(managerName);
                    addManager(client, newManager);

                    System.out.println("* Manager sucessfuly added! *");

                    // Enter to continue
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 2: // Add Client
                    // Again, these cannot be deleted and optionally not changed. Each client has a
                    // manager
                    System.out.println(_lotsOfWhiteSpaces);
                    int managerId;

                    System.out.println("-------- New Client --------\n");
                    System.out.print("Name: ");
                    scan.nextLine();
                    String clientName = scan.nextLine(); // TODO - protecao contra nomes ilegais
                    System.out.println("List of available managers:");
                    List<ManagerInfo> lManagers = listManagers(client);
                    i = 1;
                    for (ManagerInfo m : lManagers)
                        System.out.println(i++ + ". " + m.getName() + " -> ID = " + m.getId());

                    System.out.print("Select the client's manager (insert number): ");
                    do {
                        try {
                            managerId = scan.nextInt();
                            scan.nextLine();
                        } catch (Exception e) {
                            managerId = -1;
                        }
                        if (managerId <= 0 || managerId > lManagers.size())
                            managerId = -1;

                    } while (managerId == -1);

                    ClientInfo newClient = new ClientInfo(clientName, lManagers.get(managerId - 1).getId());
                    // TODO balance set a 0 por default

                    addClient(client, newClient);

                    System.out.println("* Client sucessfuly added! *");

                    // Enter to continue
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 3: // Add Currency
                    // Add a currency and respective exchange rate for the euro to the database.
                    System.out.println(_lotsOfWhiteSpaces);

                    System.out.println("-------- New currency --------\n");
                    System.out.print("Name: ");
                    // TODO - protecao contra nomes ilegais
                    scan.nextLine();
                    String currencyName = scan.nextLine();
                    Double conversionRate = -1.0;

                    do {
                        try {
                            System.out.print("Conversion Rate: ");
                            conversionRate = scan.nextDouble();
                        } catch (Exception e) {
                            System.out.println("* Ilegal input *");
                            conversionRate = -1.0;
                        }

                    } while (conversionRate == -1.0);

                    CurrencyInfo newCurrency = new CurrencyInfo(currencyName, conversionRate);
                    addCurrency(client, newCurrency);

                    System.out.println("* Manager sucessfuly added! *");

                    // Enter to continue
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 4: // List Managers
                    // List managers from the database.
                    System.out.println(_lotsOfWhiteSpaces);

                    lManagers = listManagers(client);
                    System.out.println(_div);
                    System.out.println("List of Managers:");
                    i = 1;
                    for (ManagerInfo m : lManagers)
                        System.out.println(i++ + ". " + m.getName());
                    System.out.println(_div);

                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;
                case 5: // List clients
                    // List clients from the database.
                    System.out.println(_lotsOfWhiteSpaces);

                    List<ClientInfo> lClients = listClients(client);
                    System.out.println(_div);
                    System.out.println("List of Clients:");
                    i = 1;
                    for (ClientInfo c : lClients)
                        System.out.println(i++ + ". " + c.getName());
                    System.out.println(_div);

                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;
                case 6: // List currencies
                    // List currencies from the database.
                    System.out.println(_lotsOfWhiteSpaces);

                    List<CurrencyInfo> lCurrencies = listCurrencies(client);
                    System.out.println(_div);
                    System.out.println("List of Currencies:");
                    i = 1;
                    for (CurrencyInfo cur : lCurrencies)
                        System.out.println(i++ + ". " + cur.getName() + ": 1.00" + cur.getName() + " = "
                                + cur.getConversionRate() + "EUR");
                    System.out.println(_div);

                    // Enter to continue
                    scan.nextLine();
                    System.out.println("Press \"ENTER\" to continue...");
                    scan.nextLine();
                    System.out.println(_lotsOfWhiteSpaces);
                    break;

                case 7: // Get credit per client
                    // Get the credit per client (students should compute this and the following
                    // values in euros).
                    break;
                case 8: // Get payments per client
                    // Get the payments (i.e., credit reimbursements) per client.
                    break;
                case 9: // Get the current balance of a client.
                    break;
                case 10: // Get total sum of credits
                    // Get the total (i.e., sum of all persons) credits.
                    break;
                case 11: // Get total sum of payments
                    // Get the total payments.
                    break;
                case 12: // Get total balance.
                    // Get the total balance.
                    break;
                case 13: // Compute bill of each client for the last "month"
                    // Compute the bill for each client for the last month1 (use a tumbling time
                    // window).
                    break;
                case 14: // Get list of clients without payments (last two \"months\")
                    // Get the list of clients without payments for the last two months.
                    break;
                case 15: // Get person with highest outstanding debt
                    // Get the data of the person with the highest outstanding debt (i.e., the most
                    // negative current balance).
                    break;
                case 16: // Get manager with highest revenue in payments
                    // Get the data of the manager who has made the highest revenue in payments from
                    // his or her clients.
                    break;

                case 17:
                    break;

                default:
                    System.out.print("\n\n****** Illegal option selected. ******\n\n");
                    break;
            }

        } while (num != 17);

        scan.close();
    }

    public static List<ManagerInfo> listManagers(Client client) {

        WebTarget target = client.target("http://wildfly:8080/rest/services/myservice/listManagers");
        Response response = target.request().get();
        List<ManagerInfo> personList = response.readEntity(new GenericType<List<ManagerInfo>>() {
        });
        response.close();
        return personList;
    }

    public static List<ClientInfo> listClients(Client client) {

        WebTarget target = client.target("http://wildfly:8080/rest/services/myservice/listClients");
        Response response = target.request().get();
        List<ClientInfo> personList = response.readEntity(new GenericType<List<ClientInfo>>() {
        });
        response.close();
        return personList;
    }

    public static List<CurrencyInfo> listCurrencies(Client client) {

        WebTarget target = client.target("http://wildfly:8080/rest/services/myservice/listCurrency");
        Response response = target.request().get();
        List<CurrencyInfo> currencyList = response.readEntity(new GenericType<List<CurrencyInfo>>() {
        });
        response.close();
        return currencyList;
    }

    public static void addManager(Client client, ManagerInfo m) {

        WebTarget target = client.target("http://wildfly:8080/rest/services/myservice/addManager");

        Entity<ManagerInfo> input = Entity.entity(m, MediaType.APPLICATION_JSON);
        // System.out.println(input);
        Response response = target.request().post(input);
        // String value = response.readEntity(String.class);
        // System.out.println("RESPONSE4: " + value);
        response.close();
    }

    public static void addClient(Client client, ClientInfo c) {

        WebTarget target = client.target("http://wildfly:8080/rest/services/myservice/addClient");

        Entity<ClientInfo> input = Entity.entity(c, MediaType.APPLICATION_JSON);
        // System.out.println(input);
        Response response = target.request().post(input);
        // String value = response.readEntity(String.class);
        // System.out.println("RESPONSE4: " + value);
        response.close();
    }

    public static void addCurrency(Client client, CurrencyInfo cur) {

        WebTarget target = client.target("http://wildfly:8080/rest/services/myservice/addCurrency");

        Entity<CurrencyInfo> input = Entity.entity(cur, MediaType.APPLICATION_JSON);
        // System.out.println(input);
        Response response = target.request().post(input);
        // String value = response.readEntity(String.class);
        // System.out.println("RESPONSE4: " + value);
        response.close();
    }

}