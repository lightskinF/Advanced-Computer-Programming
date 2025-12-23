package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import Interfaccia.IDispatcher;

public class ServerThread extends Thread{

    private Socket singolaconnessione;
    private IDispatcher servizio;

    public ServerThread(Socket socket, IDispatcher daskeleton)
    {
        singolaconnessione=socket;
        servizio=daskeleton;        //fai upcall a serverimpl
    }

    public void run()
    {
        try
        {
            DataOutputStream invio=new DataOutputStream(new BufferedOutputStream(singolaconnessione.getOutputStream()));
            DataInputStream ricevo=new DataInputStream(new BufferedInputStream(singolaconnessione.getInputStream()));

            String ricezione_tipo_comando=ricevo.readUTF();
            System.out.println("[THREAD_SERVER]Ricevuto comando di tipo "+ricezione_tipo_comando);

            if (ricezione_tipo_comando.equals("send"))
            {
                int seconda_lettura_numero_comando=ricevo.readInt();        //seconda lettura dal flusso che nel caso di send manda 2 messaggi
                //UPCALL:
                servizio.sendCmd(seconda_lettura_numero_comando);
                invio.writeUTF("[THREAD_SERVER] Operazione SEND andata a buon fine");
                invio.flush();
            }
            else if (ricezione_tipo_comando.compareTo("get")==0)
            {
                //UPCALL
                int prelievo_coda=servizio.getCmd();
                invio.writeInt(prelievo_coda);
                invio.flush();
            }
            else
            {
                System.out.println("ERRORE");
            }

            invio.close();
            ricevo.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }
    
}
