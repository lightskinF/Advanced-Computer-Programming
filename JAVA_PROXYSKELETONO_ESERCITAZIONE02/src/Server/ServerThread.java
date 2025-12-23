package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Interfaccia.IMagazzino;

public class ServerThread extends Thread{

    private Socket connessione_da_skeleton;
    private IMagazzino servizio_upcall;


    public ServerThread(Socket socket, IMagazzino lo_implementa_ServerImpl)
    {
        connessione_da_skeleton=socket;
        servizio_upcall=lo_implementa_ServerImpl;
    }

    public void run()
    {
        try {
            DataOutputStream invio=new DataOutputStream(new BufferedOutputStream(connessione_da_skeleton.getOutputStream()));
            DataInputStream ricevo=new DataInputStream(new BufferedInputStream(connessione_da_skeleton.getInputStream()));

            String primo_comando_ricevuto=ricevo.readUTF();         //per capire quale messaggio è, vedi in ClientProxy. sia in prelievo che deposita il primo è una strimga per far capirev al server il tipo di messaggio

            if (primo_comando_ricevuto.equals("deposita"))
            {
                //UPCALL->RICORDA, tu dal main() del server istanzi un oggetto ServerImpl, sottoclasse di ServerSkeleton, che invoca runskeleton
                //quando parte runskeleton, viene startato il thread, quindi questa funzione run. Quindi quel this in ServerSkeleton che passi come parametro
                //sarebbe l'oggetto ServerImpl che istanzi nel main. qui invochi proprio oggetto interfaccia. metodo, che È IMPLEMENTATO APPUNTO DA SERVERIMPL->
                //ServerSkeleton, CLASSE ASTRATTA, implementa l'Interfaccia (implements ma senza implementare)
                //ServerImpl, CLASSE CONCRETA, estende ServerSkeleton (extends, e quindi è lui a implemenatre concreatmente i metodi dell'interfaccia).
                String tipo_articolo=ricevo.readUTF();
                int id_articolo_dadepositare=ricevo.readInt();      //vedi in ClientThread. lui sceglie casualmente l'articolo

                servizio_upcall.deposita(tipo_articolo, id_articolo_dadepositare);
                invio.writeUTF("Inserimento riuscito dell'articolo "+tipo_articolo+" con id "+id_articolo_dadepositare);
                invio.flush();
                
            }

            else if ( ( primo_comando_ricevuto.compareTo("preleva") ) ==0 )
            {
                //UPCALL
                String tipoarticolo=ricevo.readUTF();
                int id_prelevato=servizio_upcall.preleva(tipoarticolo);
                invio.writeInt(id_prelevato);
                invio.flush();
            }

            else 
            {
                System.out.println("ERRORE IN SERVER_THREAD PER RICEZIONE TIPO MESSAGGIO ");
            }

            ricevo.close();
            invio.close();
            connessione_da_skeleton.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }
    
}
