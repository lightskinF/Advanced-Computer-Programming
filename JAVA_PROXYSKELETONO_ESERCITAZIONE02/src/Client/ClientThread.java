package Client;

import java.io.IOException;
import java.util.Random;

public class ClientThread extends Thread
{

    private String tipo_richiesta_per_upcall;

    public ClientThread(String tipo_richiesta)      //se preleva o deposita
    {
        tipo_richiesta_per_upcall=tipo_richiesta;
    }
    
    public void run()
    {

            if (tipo_richiesta_per_upcall.equals("deposita"))
            {

                for (int j=0; j<3; j++)
              {

                System.out.println("[THREAD] Richiesta numero "+j+" di tipo: "+tipo_richiesta_per_upcall);
                
                Random casuale=new Random();
                int scelta_articolo=casuale.nextInt(2);
                int scelta_id=casuale.nextInt(100)+1;
                String articolo="";
                if ((scelta_articolo)==0)
                {
                    articolo="smartphone";
                }
                else
                {
                    articolo="laptop";
                }

                //UPCALL!!!!!!!!!
                ClientProxy proxy=new ClientProxy();
                try {
                    proxy.deposita(articolo, scelta_id);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

              }

            }

            else if(tipo_richiesta_per_upcall.equals("preleva"))
            {

                for (int j=0; j<3; j++)
             {
                System.out.println("[THREAD] Richiesta numero "+j+" di tipo: "+tipo_richiesta_per_upcall);

                Random casuale=new Random();
                int scelta_articolo=casuale.nextInt(2);
                String articolo="";
                if ((scelta_articolo)==0)       //per decidere su quale coda prelevare, se un laptop o un smartphone
                {
                    articolo="smartphone";
                }
                else
                {
                    articolo="laptop";
                }

                //UPCALL
                ClientProxy proxy=new ClientProxy();
                int id_prelevato=0;
                try {
                    id_prelevato=proxy.preleva(articolo);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println("Prelevato item con id "+id_prelevato+" dalla coda per "+articolo);
             }


            }

            else
            {
                System.out.println("ERRORE LATO CLIENT TIPO RICHIESTA");
            }
        
    }
}
