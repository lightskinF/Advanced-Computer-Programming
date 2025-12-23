package Coda;

public class CodaImpl implements CodaInterfaccia{

    private int head, tail, Nelem, maxdim;
    private int[] coda;

    public CodaImpl(int dimensione_massima_coda)
    {
        head=tail=Nelem=0;
        maxdim=dimensione_massima_coda;
        coda=new int[maxdim];
    }

    private boolean isfull()
    {
        if (Nelem==maxdim) {return true;}
        else {return false;}
    }

    private boolean isempty()
    {
        if (Nelem==0) {return true;}
        else {return false;}
    }

    public void deposita(int id)
    {
        synchronized(coda)
        {
            while(isfull())
            {
                System.out.println("Coda piena, deposito non ancora possibile...");
                try {
                    coda.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            coda[tail]=id;
            tail=(tail+1)%maxdim;
            Nelem++;

            System.out.println("Inserimento di articolo ID "+id+" nella rispettiva coda.");

            coda.notifyAll();
        }

    }



    public int preleva()
    {

        int prelievo=0;

        synchronized(coda)
        {
            while(isempty())
            {
                System.out.println("Coda vuota, prelievo non ancora possibile...");
                try {
                    coda.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            prelievo=coda[head];
            head=(head+1)%maxdim;
            Nelem--;

            coda.notifyAll();

            return prelievo;
        }

    }
    
}
