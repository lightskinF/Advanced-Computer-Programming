package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ProxyClient implements Interfaccia.IDispatcher{

    private int comando;

    public ProxyClient(int command)
    {
        comando=command;
    }

    public void sendCmd(int command) throws IOException
    {
        InetAddress host=InetAddress.getByName("localhost");
        int port=8070;

        Socket socket=new Socket(host, port);

        DataOutputStream mando=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream ricevo=new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        System.out.println("[PROXY] Procedo a mandare il comando SEND, numero "+comando);

        mando.writeUTF("send");     //serve per discriminare il comando
        mando.writeInt(comando);
        mando.flush();

        String risposta=ricevo.readUTF();
        System.out.println("[PROXY] Risposta ricevuta dal dispatcher: "+risposta);

        mando.close();
        ricevo.close();
        socket.close();

    }

    public int getCmd() throws IOException
    {
        InetAddress host=InetAddress.getByName("localhost");
        int port=8070;

        Socket socket=new Socket(host, port);

        DataOutputStream mando=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream ricevo=new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        System.out.println("[PROXY] Procedo a inviare comando tipo GET");

        mando.writeUTF("get");      //serve per discriminare tipo comando
        mando.flush();

        int risposta=ricevo.readInt();
        System.out.println("[PROXY] Ricevuto dal Dispatcher comando numero "+risposta);

        return risposta;
        
    }
    
}
