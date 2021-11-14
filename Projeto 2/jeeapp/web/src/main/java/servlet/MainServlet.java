package servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTOs.GregorianCalendarDTO;
import DTOs.TripInfoDTO;
import DTOs.UserInfoDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beans.StatelessBean;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(StatelessBean.class);
    @EJB
    private StatelessBean slb;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("register") != null) {

            String email = request.getParameter("email");
            String key = request.getParameter("key");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");

            // Add passenger to database
            UserInfoDTO userInfo = new UserInfoDTO(email, name, phone, hashPassword(key));
            slb.addPassenger(userInfo);

            Integer uId = slb.getPassengerByEmail(email);

            request.getSession(true).setAttribute("role", "passenger");
            request.getSession(true).setAttribute("uId", uId);

            // Set display variables
            request.getSession(true).setAttribute("email", email);
            request.getSession(true).setAttribute("name", name);
            request.getSession(true).setAttribute("phone", phone);
            request.getSession(true).setAttribute("balance", 0.0);

            request.getRequestDispatcher("/secured/passenger.jsp").forward(request, response);
        }

        else if (request.getParameter("createData") != null) {
            slb.createData();
            request.getRequestDispatcher("/secured/admin.jsp").forward(request, response);
        }

        else if (request.getParameter("eraseData") != null) {
            slb.eraseAllData();
            request.getRequestDispatcher("/secured/admin.jsp").forward(request, response);
        }

        else if (request.getParameter("changeInfo") != null) {

            String email = request.getParameter("email");
            String key = request.getParameter("key");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");

            if (email.equals(""))
                email = null;
            else
                request.getSession(true).setAttribute("email", email);

            if (key.equals(""))
                key = null;

            if (name.equals(""))
                name = null;
            else
                request.getSession(true).setAttribute("name", name);

            if (phone.equals(""))
                phone = null;
            else
                request.getSession(true).setAttribute("phone", phone);

            UserInfoDTO userInfo = new UserInfoDTO(email, name, phone, hashPassword(key));
            Integer userId = (Integer) request.getSession(false).getAttribute("uId");

            slb.editPassenger(userId, userInfo);

            request.getRequestDispatcher("/secured/passenger.jsp").forward(request, response);
        }

        else if (request.getParameter("tripsBetweenDates") != null) {

            String[] startDate = request.getParameter("startDate").split("-");
            String[] endDate = request.getParameter("endDate").split("-");

            GregorianCalendarDTO startCal = new GregorianCalendarDTO(Integer.parseInt(startDate[0]),
                    Integer.parseInt(startDate[1]) - 1, Integer.parseInt(startDate[2]));
            GregorianCalendarDTO endCal = new GregorianCalendarDTO(Integer.parseInt(endDate[0]),
                    Integer.parseInt(endDate[1]) - 1, Integer.parseInt(endDate[2]));

            List<TripInfoDTO> trips = slb.listTripInfoBetweenStartEndDate(startCal, endCal);

            request.getSession(true).setAttribute("searchTrips", trips);

            request.getRequestDispatcher("/secured/passenger.jsp").forward(request, response);
        }

        else if (request.getParameter("charge") != null) {

            double amount = Double.parseDouble(request.getParameter("chargeAmount"));

            Integer userId = (Integer) request.getSession(false).getAttribute("uId");

            slb.chargeWallet(userId, amount);

            // Refresh local display data
            UserInfoDTO uInfo = slb.getPassengerInfoById(userId);
            request.getSession(true).setAttribute("balance", uInfo.getBalance());

            request.getRequestDispatcher("/secured/passenger.jsp").forward(request, response);
        }

        else if (request.getParameter("purchase") != null) {

            Integer tripId = Integer.parseInt(request.getParameter("tripToBuy"));
            Integer userId = (Integer) request.getSession(false).getAttribute("uId");

            slb.purchaseTicket(userId, tripId);

            UserInfoDTO uInfo = slb.getPassengerInfoById(userId);
            request.getSession(true).setAttribute("balance", uInfo.getBalance());

            List<TripInfoDTO> myTrips = slb.listTripsByPassengerId(userId);
            request.getSession(true).setAttribute("myTrips", myTrips);

            request.getRequestDispatcher("/secured/passenger.jsp").forward(request, response);
        }

        else if (request.getParameter("tripToRefund") != null) {

            Integer tripId = Integer.parseInt(request.getParameter("tripToRefund"));

            Integer userId = (Integer) request.getSession(false).getAttribute("uId");

            slb.refundTicket(userId, slb.getTicketFromTrip(userId, tripId));

            // Update balance and trips
            UserInfoDTO uInfo = slb.getPassengerInfoById(userId);
            request.getSession(true).setAttribute("balance", uInfo.getBalance());

            List<TripInfoDTO> myTrips = slb.listTripsByPassengerId(userId);
            request.getSession(true).setAttribute("myTrips", myTrips);

            request.getRequestDispatcher("/secured/passenger.jsp").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("login") != null) {
            String email = request.getParameter("email");
            String key = request.getParameter("key");

            String destination = "/error.html";
            String hashedPw = hashPassword(key);

            // Credentials: admin@admin.com - admin
            if (email.equals("admin@admin.com") && hashedPw.equals("21232f297a57a5a743894a0e4a801fc3")) {
                request.getSession(true).setAttribute("role", "admin");
                destination = "/secured/admin.jsp";
            }

            else {
                String role = slb.authenticate(email, hashedPw);
                if (role.equals("passenger")) {

                    Integer uId = slb.getPassengerByEmail(email);

                    request.getSession(true).setAttribute("role", "passenger");
                    request.getSession(true).setAttribute("uId", uId);

                    // Get and set display variables from DTO
                    UserInfoDTO uInfo = slb.getPassengerInfoById(uId);
                    List<TripInfoDTO> myTrips = slb.listTripsByPassengerId(uId);

                    request.getSession(true).setAttribute("email", uInfo.getEmail());
                    request.getSession(true).setAttribute("name", uInfo.getName());
                    request.getSession(true).setAttribute("phone", uInfo.getPhoneNumber());
                    request.getSession(true).setAttribute("balance", uInfo.getBalance());

                    request.getSession(true).setAttribute("myTrips", myTrips);

                    destination = "/secured/passenger.jsp";
                }

                else if (role.equals("manager")) {
                    request.getSession(true).setAttribute("role", "manager");
                    request.getSession(true).setAttribute("uId", slb.getManagerByEmail(email));
                    destination = "/secured/manager.jsp";
                }

                else {
                    request.getSession(true).removeAttribute("role");
                    request.getSession(true).removeAttribute("uId");
                }
            }
            request.getRequestDispatcher(destination).forward(request, response);
        }

        else if (request.getParameter("logout") != null) {
            request.getSession().invalidate();
            request.getRequestDispatcher("/login.html").forward(request, response);
        }

        else if (request.getParameter("delete") != null) {

            Integer userId = (Integer) request.getSession(false).getAttribute("uId");
            slb.deletePassenger(userId);

            // Logout after deletion
            request.getSession().invalidate();
            request.getRequestDispatcher("/login.html").forward(request, response);
        }
    }

    /*
     * Password hashing adapted from
     * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-
     * hash-md5-sha-pbkdf2-bcrypt-examples/
     */
    private String hashPassword(String password) {

        if (password == null)
            return null;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}