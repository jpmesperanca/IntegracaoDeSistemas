package servlet;

import java.io.IOException;

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

        if (email != null && key != null) {

            String role = slb.authenticate(email, key);

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
            }
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}