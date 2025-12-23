package LoggingServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


import javax.jms.JMSException;

import javax.naming.NamingException;

public abstract class ServerSkeleton implements Interfaccia.ILog{



    public void runskeleton() throws IOException, NamingException, JMSException
    {
        //classico
        InetAddress host=InetAddress.getByName("localhost");
        int port=8075;

        ServerSocket server=new ServerSocket(port, 30, host);
        System.out.println("Server in ascolto su porto 8075..");

        try
        {

            while (true)
            {
                Socket s=server.accept();

                //thread da startare
                ThreadServer thread=new ThreadServer(s, this);
                thread.start();
            }
        }
        finally
        {
            server.close();
        }
        
    }

}
