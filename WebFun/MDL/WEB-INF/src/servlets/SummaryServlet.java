package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

// @WebServlet("/summary")
public class SummaryServlet extends HttpServlet {
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String summary = request.getParameter("sum");
		//if (summary != null) {
			// Add to database

			// temp
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Connection connection = null;
			Statement statement = null;

			try {
				connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/hike?useSSL=false", "root", "xxxx");

				statement = connection.createStatement();

				String sqlQuery = "UPDATE users SET summary = '" + summary + "' WHERE name = 'Incognito';";	// TODO: sanitize!
				int updatedCount = statement.executeUpdate(sqlQuery);

				out.println("<html><head><title>Query Response</title></head><body>");
		        out.println("<h3>Thank you for your query.</h3>");
		        out.println("<p>You query is: " + sqlQuery + "</p>"); // Echo for debugging
		        out.println("<p>Result: " + updatedCount + "</p>");
				out.println("</body></html>");

			} catch (SQLException ex) {
				ex.printStackTrace();
				
			} finally {
				out.close();
				try {
					if (statement != null) statement.close();
					if (connection != null) connection.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		// } else {
		// 	// Do not overwrite. Do nothing actually. Meh. Remove this else.
		// }
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}