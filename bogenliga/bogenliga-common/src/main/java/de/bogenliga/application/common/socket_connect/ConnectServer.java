package de.bogenliga.application.common.socket_connect;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

/**
 * this is the socket connect server class
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

public class ConnectServer
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(5432);
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Serverantwort");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}