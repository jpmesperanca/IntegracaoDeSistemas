package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String key = request.getParameter("key");
        String destination = "/error.html";

        if (name != null && key != null) {

            boolean auth = name.equals("john") && key.equals("11");

            if (auth) {
                request.getSession(true).setAttribute("auth", name);
                destination = "/secured/passenger.jsp";
            } else {
                request.getSession(true).removeAttribute("auth");
            }
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}