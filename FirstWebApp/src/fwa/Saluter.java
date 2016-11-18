import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Created by ibarcan on 15-Nov-16.
 */
public class Saluter extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            out.println("<html>");
            out.println("<head><title>First Web App</title></head>");
            out.println("<body>");

            // Be polite. Salute first.
            out.println("<h1>Howdy, World!</h1>");

            // Echo client's request information
            out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
            out.println("<p>Protocol: " + request.getProtocol() + "</p>");
            out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
            out.println("<p>Remote Address: " + request.getRemoteAddr() + "</p>");

            // Print timestamp
            out.println("<p>Now = " + LocalDateTime.now().toLocalTime() + ". But that's already history...</p>");

            out.println("</body>");
            out.println("</html>");
        }
        finally {
            out.close();
        }
    }
}
