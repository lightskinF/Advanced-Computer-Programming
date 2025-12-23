package subscriber_side;

import java.io.IOException;

public class CodaImpl implements Coda{

    private String[] coda;
    private int head, tail, Nelementi, maxdimensione;

    private boolean isfull()
    {
        if(Nelementi==maxdimensione)
        {
            return true;
        }
        else {return false ;}
    }

    private boolean isempty()
    {
        if (Nelementi==0)
        {
            return true;
        }
        else {return false;}
    }

    public CodaImpl(int DIMENSIONE_CODA)    //costruttrìore, inizializzo la coda!
    {
        head=tail=0;
        Nelementi=0;
        maxdimensione=DIMENSIONE_CODA;
        coda=new String[maxdimensione];
    }

    public void put(String comando) 
    {
        synchronized(coda)
        {
            while (isfull())
            {
                try 
                {
                    System.out.println("Coda piena..");
                    coda.wait();
                }

                catch (InterruptedException e) 
                {
                   
                    e.printStackTrace();
                };
            }

            coda[head]=comando;
            head=(head+1)%maxdimensione;
            Nelementi++;
            System.out.println("Inserito comando "+comando+" in coda");
            coda.notifyAll();
        }
    }

    public void svuota() throws IOException
    {
        synchronized(coda)
        {
            while (isempty())
            {
                try 
                {
                    System.out.println("coda vuota..");
                    coda.wait();
                } 
                catch (InterruptedException e) 
                {
                    
                    e.printStackTrace();
                }
            }

            while (Nelementi!=0)        //devo svuotarla tutta completamente
            {
                try (java.io.FileWriter writer = new java.io.FileWriter("CmdLog.txt", true)) 
                {
                    String comando_prelevato=coda[tail];    //sopra creo il file in cui salvare i comandi
                    tail=(tail+1)%maxdimensione;
                    Nelementi--;
                    System.out.println("Prelevato comando "+comando_prelevato);

                    writer.write(comando_prelevato + "\n");     //aggiungo il comando al file
                }

            }


            coda.notifyAll();    //solo dopo aver svuotato la coda risveglio i produttori. qui con coda. davanti perchè sei fuoti il blocco syncronyzed
        }
    }

    

    
}
