import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


/**
 * Created by ibarcan on 17-Nov-16.
 */
@WebServlet("/query")
// @WebServlet(name = "Querier")
public class Querier extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookshop?useSSL=false",
                    "ibarcan", "xxxx");
            stmt = conn.createStatement();
            String sqlStr = "select * from books where author = "
                    + "'" + request.getParameter("author") + "'"
                    + "and qty > 0 order by price desc";

            out.println("<html><head><title>Query Response</title></head>");
            out.println("<body>");
            out.println("<p>The query is: " + sqlStr + "</p>");

            ResultSet rset = stmt.executeQuery(sqlStr);

            int count = 0;
            while (rset.next()) {
                out.println("<p>" + rset.getString("author")
                        + ", " + rset.getString("title")
                        + ", $" + rset.getDouble("price") + "</p>");
                count++;
            }

            out.println("<p>==== " + count + " records found. ====</p>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            out.close();
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
