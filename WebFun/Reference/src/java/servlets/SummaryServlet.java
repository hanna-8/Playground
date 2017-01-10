package servlets;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

//@WebServlet("/summary")
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
			PreparedStatement statement = null;

			try {
				connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/hike?useSSL=false", "root", "xxxx");

				String sqlQuery = "UPDATE users SET summary = ? WHERE name = 'Incognito';";	// TODO: sanitize!
				statement = connection.prepareStatement(sqlQuery);
				statement.setString(1, summary);

				int updatedCount = statement.executeUpdate();

				 out.println("<html><head><title>Query Response</title></head><body>");
		         out.println("<h3>Thank you for your query.</h3>");
		         out.println("<p>You query is: " + statement.toString() + "</p>"); // Echo for debugging
		         out.println("<p>Result: " + updatedCount + "</p>");
				 out.println("</body></html>");

			} catch (Exception ex) {
				ex.printStackTrace(out);
				
			} finally {
				// null'; update users set name='dummy
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