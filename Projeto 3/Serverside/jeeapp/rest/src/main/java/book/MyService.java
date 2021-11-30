package book;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@RequestScoped
@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {

    @GET
    @Path("/test")
    public String method1() {
        System.out.println("M1 executing...."); // Nao sei para onde isto vai
        return "M1 baixo...";
    }

    @GET
    @Path("/addManager")
    public Map<String, Object> addManager() {

        JSONObject jo = new JSONObject();
        jo.put("name", "jon doe");
        jo.put("age", "22");
        jo.put("city", "chicago");

        return jo.toMap(); // Works
    }

    @GET
    @Path("/addManager2")
    public JSONObject addManager2() {

        JSONObject jo = new JSONObject();
        jo.put("name", "jon doe");
        jo.put("age", "22");
        jo.put("city", "chicago");

        return jo; // returns {"empty": false}
    }

    /*
     * @GET
     * 
     * @Path("/add")
     * public String method2() {
     * System.out.println("M2 executing....");
     * String name = "name_" + new Time(Calendar.getInstance().getTimeInMillis());
     * // manageStudents.addStudent(name);
     * return name;
     * }
     * 
     * 
     * @GET
     * 
     * @Path("/list")
     * public List<Student> method3() {
     * System.out.println("M3 executing....");
     * List<Student> list = new ArrayList<>();
     * // manageStudents.listStudents();
     * return list;
     * }
     */
}
