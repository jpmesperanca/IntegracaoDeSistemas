package book;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.Student;

@RequestScoped
@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {

    @GET
    @Path("/test")
    public String method1() {
        System.out.println("M1 executing....");
        return "M1 executing...";
    }

    @GET
    @Path("/add")
    public String method2() {
        System.out.println("M2 executing....");
        String name = "name_" + new Time(Calendar.getInstance().getTimeInMillis());
        // manageStudents.addStudent(name);
        return name;
    }

    @GET
    @Path("/list")
    public List<Student> method3() {
        System.out.println("M3 executing....");
        List<Student> list = new ArrayList<>();
        // manageStudents.listStudents();
        return list;
    }
}
