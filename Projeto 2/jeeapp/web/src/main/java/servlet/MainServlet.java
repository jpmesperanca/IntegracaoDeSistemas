package servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.StatelessBean;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private StatelessBean slb;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String key = request.getParameter("key");

        String destination = "/error.html";
        String role = slb.authenticate(email, hashPassword(key));

        if (role.equals("passenger")) {
            request.getSession(true).setAttribute("role", "passenger");
            request.getSession(true).setAttribute("uId", slb.getPassengerByEmail(email));
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

        request.getRequestDispatcher(destination).forward(request, response);
    }

    /*
     * Password hashing adapted from
     * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-
     * hash-md5-sha-pbkdf2-bcrypt-examples/
     */
    private String hashPassword(String password) {

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