package serviceGateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FacebookGatewayI implements IServiceGateway {
	
    private static final String FACEBOOK_SERVER_HOST = "localhost";
    private static final int FACEBOOK_SERVER_PORT = 9091;
    
	public boolean authenticate(String email, String password) {
		try (Socket socket = new Socket(FACEBOOK_SERVER_HOST, FACEBOOK_SERVER_PORT);
	             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

	            // Send authentication request
	            out.println("AUTHENTICATE");
	            out.println(email);
	            out.println(password);

	            // Read the response
	            String response = in.readLine();
	            return "AUTH_SUCCESS".equalsIgnoreCase(response);
	        } catch (Exception e) {
	            System.err.println("Error authenticating with Facebook: " + e.getMessage());
	            return false;
	        }
	}
}
