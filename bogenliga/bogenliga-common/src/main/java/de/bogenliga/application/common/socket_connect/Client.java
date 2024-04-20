package de.bogenliga.application.common.socket_connect;

/**
 * define the Client side for Socket Connect
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Client
{
    public static void main(String[] args)
    {
        try
        {

            Socket socket = new Socket ("localhost", 5432);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverResponse = in.readLine();
            System.out.println("Server-Antwort: " + serverResponse);

            socket.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
