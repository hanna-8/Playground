import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * The first part on a simple client-server application over the web.
 */

public class SimpleHTTPServer {

    public static void go() {
        try {

            final int PORT = 8080;
            final ServerSocket server = new ServerSocket(PORT);
            System.out.println("Listening for connections on port " + PORT);

            while(true) {
                final Socket clientSocket = server.accept();

                // 1. Read HTTP request from the client socket
                InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader reader = new BufferedReader(isr);

                String line = reader.readLine();
                int i = 0;
                while (!line.isEmpty()) {
                    System.out.println(i++ + " " + line);
                    line = reader.readLine();
                }

                // 2. Prepare an HTTP response.
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;

                // 3. Send HTTP response to the client.
                clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));

                // 4. Close the socket.
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
