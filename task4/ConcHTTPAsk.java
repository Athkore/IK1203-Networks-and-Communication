/*
	Andreas Kinnunen
	kinnu@kth.se
	2018-02-27
*/
import java.io.*;
import java.net.*;

public class ConcHTTPAsk{
	    public static void main(String[] args) throws Exception {
       try{ 
		int portnumber;
		try{
			portnumber = Integer.parseInt(args[0]);
		}
		catch(Exception e){
			portnumber = 80;
		}
        ServerSocket welcomeSocket = new ServerSocket(portnumber);
        
        int counter = 0;
        
        while(true){
            counter++;
            
            Socket connectionSocket = welcomeSocket.accept();
            
            HTTPAskThread tST = new HTTPAskThread(connectionSocket, counter);
            
            tST.start();
            
            System.out.println("Thread Started");
        }
       }
       catch(Exception e){
           System.out.println(e);
       }
    }
}