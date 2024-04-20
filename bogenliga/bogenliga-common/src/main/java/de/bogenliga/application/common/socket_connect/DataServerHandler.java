package de.bogenliga.application.common.socket_connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *  I handle socket connection for the current thread.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

public class DataServerHandler implements Runnable
{
    private Socket clientSocket;

    public DataServerHandler(Socket _clientSocket)
    {
        this.clientSocket = _clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("Thread " + Thread.currentThread().getId() + " starts for client call");

            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String strInput = null;
            System.out.println ("Thread " + Thread.currentThread().getId() + " waiting for client call");
            while ((strInput = in.readLine()) != null)
            {
                System.out.println("Thread " + Thread.currentThread().getId() + ": input: " + strInput);

                out.println(strInput.toUpperCase());

                System.out.println("Thread " + Thread.currentThread().getId() + " wait for next call");
            }

            System.out.println("Thread " + Thread.currentThread().getId() + " is finished");
        }
        catch (IOException ioEx)
        {
            System.out.println("Input Error :" + ioEx.getMessage());
        }
    }
}
