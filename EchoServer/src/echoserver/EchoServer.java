/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;
import java.net.*;
import java.io.*;
import java.sql.*;
/**
 *
 * @author yzhu
 */
public class EchoServer extends Thread {
    protected Socket clientSocket = null;
    int client_id = -1;
    static int[] all_ids = new int[5];
    static int count = 0;
 public static void main(String[] args) throws IOException {
    
     ServerSocket serverSocket = null;
    
    
    try {
         serverSocket = new ServerSocket(10007);
         
       }
    catch (IOException e)
        {
         System.err.println("Could not listen on port: 10007.");
         System.exit(1);
        }
    
        System.out.println ("Waiting for connection.....");
        



    System.out.println ("Connection successful");
    System.out.println ("Waiting for input.....");
    while(true) {
     new EchoServer(serverSocket.accept());
     
    }
   }

    private EchoServer(Socket accept) {
        clientSocket = accept;
        client_id = clientSocket.getPort();
        all_ids[count++] = clientSocket.getPort();
        start();
    }
    
    public void run() {
      try {   
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
         BufferedReader in = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        boolean done = false;
        while ((inputLine = in.readLine()) != null)
            {
             System.out.println ("Client #" + this.client_id + " " + inputLine);
             out.println(inputLine);
             out.println(this.client_id);
             if(count == 2) {
                 out.println(count);
                 for(int i = 0; i < count; i++) {
                     out.println(all_ids[i]);
                }
             }
             if (inputLine.equalsIgnoreCase("Bye"))
                 break;
            }

        out.close();
        in.close();
        clientSocket.close();
        }catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
