package LoggingServer;

import java.io.BufferedInputStream;

import java.io.DataInputStream;

import java.io.IOException;
import java.net.Socket;

import Interfaccia.ILog;

public class ThreadServer extends Thread{


    private Socket socket;
    private ILog serviceupcall;

    public ThreadServer(Socket s, ILog daskeleton)
    {
        socket=s;
        serviceupcall=daskeleton;       //OGGETTO INTERFACCIA, THIS PASSATO DA SKELETON!
        
    }

    public void run()
    {
        try {

            DataInputStream ricevo=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //DataOutputStream invio=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));  
            
            String messagelog=ricevo.readUTF();
            int tipo=ricevo.readInt();
            System.out.println("Ricevuto da client: "+messagelog+" "+tipo);

            serviceupcall.log(messagelog, tipo);
            //continua? sì devo mandare risposta al client.     NON DIMEMNTICARTI           //dubbio, come facevo a farlo synchronized forse ne creo uno interno a serverimpl synchronized!

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
