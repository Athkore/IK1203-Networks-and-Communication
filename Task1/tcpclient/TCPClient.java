package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
		String serverOutput;
		if(ToServer==null){
			serverOutput = askServer(hostname, port);
		}
		else{
			Socket clientSocket = new Socket(hostname, port);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(ToServer + '\n');
			String inline = inFromServer.readLine() + '\n';
			serverOutput = inline;
			while(inFromServer.ready()){
				inline = inFromServer.readLine();
				serverOutput = serverOutput+inline + '\n';
			}
			clientSocket.close();
		}
		return serverOutput;
    }

    public static String askServer(String hostname, int port) throws  IOException {
		Socket clientSocket = new Socket(hostname, port);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inline = inFromServer.readLine();
		String serverOutput = inline;
		while(inFromServer.ready()){
			inline = inFromServer.readLine();
			serverOutput = serverOutput+inline;
			if (serverOutput.length()>10000)break;
		}
		clientSocket.close();
		return serverOutput;
    }
}

