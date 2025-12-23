package Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ServiceProxy implements Interfaccia.ILog{



    public void log (String stringa, int intero) throws IOException
    {
        InetAddress host=InetAddress.getByName("localhost");
        int port=8075;

        Socket socket=new Socket(host, port);
        
        DataInputStream ricevo=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream invio=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        invio.writeUTF(stringa);
        invio.writeInt(intero);
        invio.flush();

        //String risposta=ricevo.readUTF();
        //System.out.println("Ricevuto messaggio "+risposta);

        ricevo.close();
        invio.close();
        socket.close();
        
    }

}
