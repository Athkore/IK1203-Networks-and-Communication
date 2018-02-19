import java.net.*;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Files;

public class HTTPAsk {
    public static void main( String[] args) {
        String HTTP200 = "HTTP/1.1 200 OK\nContent-Length: ";
		String HTTP400 = "HTTP/1.1 400 Bad Request\n\n";
		String HTTP404 = "HTTP/1.1 404 Not Found\n\n";
		String HTTP501 = "HTTP/1.1 501 Not Implemented\n\n";
		String HTTP505 = "HTTP/1.1 505 HTTP Version Not Supported\n\n";	
    	try{
		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
			while(true){ 
				Socket connectionSocket = welcomeSocket.accept();
				String clientQuery = "";
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientQuery = inFromClient.readLine();
				String index = "<html><link rel=\"shortcut icon\" href=\"favicon.ico\" /><form action=\"/ask\">Hostname:<br><input type=\"text\" name=\"hostname\" value=\"\"><br>Port:<br><input type=\"text\" name=\"port\" value=\"\"><br><br><input type=\"submit\" value=\"Submit\"></form></html>\n";
				String ask = clientQuery.substring(clientQuery.indexOf('/')+1,clientQuery.lastIndexOf(' '));
				if(clientQuery.contains("GET")&&clientQuery.contains("HTTP/1.1")){
					if(ask.equals("")||ask.equals("ask?")||ask.equals("ask")){
						outToClient.writeBytes(HTTP200 + index.length() + "\n" + "Content-Type: text/html\n\n" + index + "\n\n");
						connectionSocket.close();
					}
					else if(ask.length()>4&& ask.substring(0,4).equals("ask?")){
						try{
								String hostname = ask.substring(ask.indexOf("hostname=")+9,ask.indexOf('&'));
								String portString = ask.substring(ask.indexOf("port=")+5,ask.length());
								int port = Integer.parseInt(portString);
								String answer = null;
								try{
									Socket askSocket = new Socket(hostname, port);
									BufferedReader inFromServer = new BufferedReader(new InputStreamReader(askSocket.getInputStream()));
									String inline = inFromServer.readLine();
									answer = inline + '\n';
									while(inFromServer.ready()){
										inline = inFromServer.readLine();
										answer = answer + inline + '\n';
										if(answer.length()>4196){
											askSocket.close();
											break;
										}
									}
									if(answer == null) answer = "";
									outToClient.writeBytes("HTTP/1.1 200 OK\nContent-Length: " + answer.length() + "\n\n" + answer);
								}
								catch(Exception e){
									outToClient.writeBytes(HTTP400);		
								}
								connectionSocket.close();
							}
						catch(Exception e){
							outToClient.writeBytes(HTTP404);
						}
					}
					else{
						try{
							byte[] file = Files.readAllBytes(Paths.get(ask));
							String sendfile = new String(file);
							outToClient.writeBytes(HTTP200 + sendfile.length() + '\n' + "Content-Type: icon/x-icon\n\n" + sendfile + '\n');
							connectionSocket.close();
						}
						catch(Exception e){
							outToClient.writeBytes(HTTP404);
						}
					}
				}
				else{
					if(!clientQuery.contains("HTTP/1.1"))outToClient.writeBytes(HTTP501);
					else if(!clientQuery.contains("GET"))outToClient.writeBytes(HTTP505);
				}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
    }
}