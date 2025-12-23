package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientProxy implements Interfaccia.IMagazzino{

    public void deposita(String articolo, int id) throws IOException
    {
        System.out.println("[PROXY] Richiesta DEPOSITA per articolo: "+articolo+" e id: "+id);

        InetAddress host=InetAddress.getByName("localhost");
        int port=8075;

        Socket socket=new Socket(host, port);
        DataOutputStream invio=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream ricevo=new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        invio.writeUTF("deposita");     //per discriminare il tipo messaggio
        invio.writeUTF(articolo);
        invio.writeInt(id);
        invio.flush();

        String esito=ricevo.readUTF();
        System.out.println("Messaggio ricevuto dal server: "+esito);

        invio.close();
        ricevo.close();
        socket.close();
    }



    public int preleva(String articolo) throws IOException
    {
        System.out.println("[PROXY] Richiesta di tipo PRELEVA");

        InetAddress host=InetAddress.getByName("localhost");
        int port=8075;

        Socket socket=new Socket(host, port);
        DataOutputStream invio=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream ricevo=new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        invio.writeUTF("preleva");     //per discriminare il tipo messaggio
        invio.writeUTF(articolo);
        invio.flush();

        int id_articolo_prelevato=ricevo.readInt();
        System.out.println("Prelevato articolo di tipo "+articolo+" con ID: "+id_articolo_prelevato);

        invio.close();
        ricevo.close();
        socket.close();
        
        return id_articolo_prelevato;
    }
    
}
