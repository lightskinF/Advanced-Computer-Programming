package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Interfaccia.IDispatcher;

public abstract class ServerSkeleton implements IDispatcher{

    public void runskeleton() throws IOException
    {
        InetAddress host=InetAddress.getByName("localhost");
        int port=8070;

        ServerSocket server=new ServerSocket(port, 50, host);
        System.out.println("Server in ascolto su porta 8070...");

        while (true) 
        {

            Socket connessione_messaggi=server.accept();
            
            ServerThread thread=new ServerThread(connessione_messaggi, this);
            thread.start();
            
        }
        
    }
    
}
