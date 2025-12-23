package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ServerSkeleton implements Interfaccia.IMagazzino{

    public void runskeleton() throws IOException
    {
        InetAddress host=InetAddress.getByName("localhost");
        int port=8075;
        ServerSocket server=new ServerSocket(port, 50, host);

        System.out.println("Server in ascolto su porta 8075");

        while (true)
        {
            Socket connessione_client=server.accept();
            ServerThread thread=new ServerThread(connessione_client, this);
            thread.start();
        }
    }
    
}
