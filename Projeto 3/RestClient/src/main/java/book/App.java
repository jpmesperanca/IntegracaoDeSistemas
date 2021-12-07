package book;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class App {
    public static void main(String[] args) {

        Client client = ClientBuilder.newClient();

        WebTarget target = client.target("http://localhost:8080/rest/services/myservice/listManagers");
        Response response = target.request().get();
        List<ManagerInfo> personList = response.readEntity(new GenericType<List<ManagerInfo>>() {
        });
        for (ManagerInfo m : personList) {
            System.out.println("Name: " + m.getName());
        }
        response.close();

        /*
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

    }
}