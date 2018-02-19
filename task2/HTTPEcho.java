import java.net.*;
import java.io.*;

public class HTTPEcho {
    public static void main( String[] args){
		String HTTP = "HTTP/1.1 200 OK\nContent-Length: ";
    	try{
		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
			while(true){ 
				Socket connectionSocket = welcomeSocket.accept();
				String clientQuery = "";
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				while(inFromClient.ready())clientQuery = clientQuery + inFromClient.readLine() + '\n';
				outToClient.writeBytes(HTTP + Integer.toString(clientQuery.length()) + "\n\n" + clientQuery);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
    }
}

