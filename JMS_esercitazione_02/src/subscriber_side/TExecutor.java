package subscriber_side;

import java.io.IOException;

public class TExecutor extends Thread{

    private CodaImpl coda_comandi_sensori;

    public TExecutor(CodaImpl coda_da_svuotare)
    {
        coda_comandi_sensori=coda_da_svuotare;
    }

    public void run()
    {
        System.out.println("Svuoto la coda e ne salvo i comandi su un file..");
        
        try {
            coda_comandi_sensori.svuota();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
    
}
