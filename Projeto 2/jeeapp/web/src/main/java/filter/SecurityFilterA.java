package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/secured/admin.jsp")
public class SecurityFilterA implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        if (session != null && !(session.getAttribute("role").equals(""))) {

            if (session.getAttribute("role").equals("admin"))
                chain.doFilter(request, response);
            else
                request.getRequestDispatcher("/error.html").forward(request, response);
        }

        else {
            request.getRequestDispatcher("/error.html").forward(request, response);
        }
    }
}
