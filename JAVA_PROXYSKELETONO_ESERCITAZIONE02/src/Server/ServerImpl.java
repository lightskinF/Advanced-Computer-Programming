package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

import Coda.CodaImpl;

public class ServerImpl extends ServerSkeleton{


    private CodaImpl coda_laptop;
    private CodaImpl coda_smartphone;

    public ServerImpl(int dimensione_massima_coda)
    {
        coda_laptop=new CodaImpl(dimensione_massima_coda);
        coda_smartphone=new CodaImpl(dimensione_massima_coda);
    }


    public void deposita(String articolo, int id) throws IOException
    {
        if (articolo.equals("laptop"))                          //è qua  che capisci se depositare su coda laptop o smartphone, in base all'articolo
        {
            //UPCALL
            coda_laptop.deposita(id);
        }
        else if (articolo.equals("smartphone"))
        {
            //UPCALL
            coda_smartphone.deposita(id);
        }
        else
        {
            System.out.println("ERRORE RICEZIONE TIPO DI ARTICOLO");
        }

    }


    public int preleva(String articolo) throws UnknownHostException, IOException
    {


        int id_prelevato=0;

        FileWriter file_prelievi_laptop=new FileWriter("prelievi_laptop.txt", true);   //false non salva i prelievi di run del main precedenti.
        FileWriter file_prelievi_smartphone=new FileWriter("prelievi_smartphone.txt", true);

        if (articolo.equals("laptop"))
        {
            //UPCALL
            id_prelevato=coda_laptop.preleva();

            file_prelievi_laptop.write(id_prelevato+"\n");
            
        }
        else if (articolo.equals("smartphone"))
        {
            //UPCALL
            id_prelevato=coda_smartphone.preleva();

            file_prelievi_smartphone.write(id_prelevato+"\n");

        }
        else 
        {
            System.out.println("ERRORE PRELIEVO DI SERVER IMPL");
            return -1;
        }
        
        file_prelievi_laptop.close();
        file_prelievi_smartphone.close();
        
        return id_prelevato;

    }

}
