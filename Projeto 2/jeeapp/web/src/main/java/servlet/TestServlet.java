package servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTOs.GregorianCalendarDTO;
import DTOs.TripInfoDTO;
import DTOs.UserInfoDTO;
import beans.StatelessBean;
import data.Passenger;

@WebServlet("/webaccess")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private StatelessBean slb;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        slb.createData();

        response.getWriter().print("Dados criados!\n");

        List<Integer> field1List = slb.listPassengers().stream().map(Passenger::getId).collect(Collectors.toList());
        String result = "Passenger list: " + field1List + "\n";
        System.out.println(result);
        response.getWriter().print(result);

        // -------------------- 6 --------------------
        // As a user, I want to edit my personal information.
        response.getWriter().print("-------------------- 6 --------------------\n");
        response.getWriter().print("\"As a user, I want to edit my personal information.\"\n\n");

        // ir buscar dados iniciais deste user
        UserInfoDTO uInfo = slb.getPassengerInfoById(field1List.get(0));

        response.getWriter().print("Passenger ID  [" + field1List.get(0) + "].\nName: " + uInfo.getName() + "\nEmail: "
                + uInfo.getEmail() + "\nPhone Number: " + uInfo.getPhoneNumber() + "\n\n");

        // alterar o mail e nome
        slb.editPassenger(field1List.get(0), "novo@email.com", null, "Jose II", null);

        // ir buscar a info outra vez para ver se mudou
        uInfo = slb.getPassengerInfoById(field1List.get(0));
        response.getWriter().print("Did it work?\nPassenger ID  [" + field1List.get(0) + "].\nName: " + uInfo.getName()
                + "\nEmail: " + uInfo.getEmail() + "\nPhone Number: " + uInfo.getPhoneNumber() + "\n\n");

        // -------------------- 7 --------------------
        // As a user, I want to delete my account, thus erasing all traces of my
        // existence fromthe system, including my available items.

        response.getWriter().print("-------------------- 7 --------------------\n");
        response.getWriter().print(
                "\"As a user, I want to delete my account, thus erasing all traces of my existence from the system, including my available items.\"\n\n");

        // mostrar a atual lista de passengers
        response.getWriter().print("Passenger list: " + field1List + "\n");

        // apagar o primeiro passenger
        slb.deletePassenger(field1List.get(0));
        response.getWriter().print("Deleted user with ID = " + field1List.get(0) + "\n");

        // voltar a ir buscar e mostrar a lista de passengers
        field1List = slb.listPassengers().stream().map(Passenger::getId).collect(Collectors.toList());
        response.getWriter().print("Updated Passenger list: " + field1List + "\n\n");

        // -------------------- 8 --------------------
        // As a user, I want to list the available trips, providing date intervals.
        response.getWriter().print("-------------------- 8 --------------------\n");
        response.getWriter().print("\"As a user, I want to list the available trips, providing date intervals.\"\n\n");

        response.getWriter().print("Listing Available Trips between 1999 and 2001");
        List<TripInfoDTO> availableTrips = slb.listTripInfoBetweenStartEndDate(new GregorianCalendarDTO(1999, 0, 1),
                new GregorianCalendarDTO(2001, 0, 1));

        for (TripInfoDTO t : availableTrips) {
            response.getWriter().print("\n----------\n");
            int m = t.getDepartureDate().getMonth() + 1;
            response.getWriter()
                    .print("Departure Date: " + t.getDepartureDate().getDay() + "/" + m + "/"
                            + t.getDepartureDate().getYear() + "\nDeparture Point: " + t.getDeparturePoint()
                            + "\nDestination Point: " + t.getDestinationPoint() + "\nCapacity: " + t.getCapacity()
                            + "px\nTicket Price: " + t.getTicketPrice() + " EUR");
        }
        response.getWriter().print("\n----------\n");

        slb.eraseAllData();
        response.getWriter().print("\n\nDados apagados!\n");
    }

}