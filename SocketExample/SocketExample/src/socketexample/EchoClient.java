/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketexample;
import java.io.*;
import java.net.*;
/**
 *
 * @author yzhu
 */
public class EchoClient {
    public static void main(String[] args) throws IOException {

        
        String serverHostname = new String ("127.0.0.1");
        int id = -1;
        int timetable[] = {0, 0, 0, 0, 0};
        int other_clients[] = new int[5];
        int num_clients = 0;
        
        if (args.length > 0)
           serverHostname = args[0];
        System.out.println ("Attemping to connect to host " +
		serverHostname + " on port 10007.");

        Socket echoSocket = null;
        
        PrintWriter controller_out = null;
        BufferedReader controller_in = null;
        
 

        try {
            echoSocket = new Socket(serverHostname, 10007);
            controller_out = new PrintWriter(echoSocket.getOutputStream(), true);
            controller_in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String userInput;
        System.out.print ("input: ");

	while ((userInput = stdIn.readLine()) !=  null) {
	    controller_out.println(userInput);
	    try{
                System.out.println("echo: " + controller_in.readLine());
                id = Integer.parseInt(controller_in.readLine().trim());
                if(controller_in.ready()) {
                    num_clients = Integer.parseInt(controller_in.readLine().trim());
                    for(int i = 0; i < num_clients; i++) {
                        other_clients[i] = Integer.parseInt(controller_in.readLine());
                    }
                    startSim(other_clients, num_clients, id);
                }
               
            }
            catch (Exception e)
            {
                System.out.println("Socket Closed! ");
                break;
            }
            System.out.print ("input: ");

	}
       
        
        System.out.println(controller_in.read());

	controller_out.close();
	controller_in.close();
	stdIn.close();
	echoSocket.close();
    }
    public static void startSim(int[] other_clients, int num_clients, int id) {
        Socket otherClientSocket = null;
        ServerSocket clientServer = null;
        try { 
            clientServer = new ServerSocket(id);
        } catch(Exception e) {
             System.out.println("Error: " + e.getMessage());
        }
        while(num_clients >= 0) {
                
            if(other_clients[num_clients] != id) {
                try {
                otherClientSocket = new Socket("127.0.0.1", other_clients[num_clients]);
                otherClientSocket = clientServer.accept();
                } catch(Exception e) {
                    System.out.println("Error " + e.getLocalizedMessage());
                }
            }
            num_clients--;
        }
        
    }
}