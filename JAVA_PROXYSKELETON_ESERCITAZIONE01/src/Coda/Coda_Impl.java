package Coda;

public class Coda_Impl implements Coda_interfaccia
{

    private int head, tail, Nelem, maxdim;
    private int[] coda;

    public Coda_Impl(int dimensione_coda)
    {
        head=tail=Nelem=0;
        maxdim=dimensione_coda;
        coda=new int[maxdim];
    }

    private Boolean isfull()
    {
        if (Nelem==maxdim)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private Boolean isempty()
    {
        if (Nelem==0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void inserisci(int comando)
    {
        synchronized(coda)
        {
            while(isfull())
            {
                try {
                    System.out.println("Coda piena, non posso inserire..");
                    coda.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            coda[tail]=comando;
            tail=(tail+1)%maxdim;
            Nelem++;
            System.out.println("Elemento inseriro in coda correttamente ");

            coda.notifyAll();
        }
    }

    public int preleva()
    {
        synchronized(coda)
        {
            while (isempty()) 
            {

                System.out.println("Coda vuota, non posso prelevare..");
                try {
                    coda.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }   
            }

            int prelievo=coda[head];
            System.out.println("prelevato dalla coda comando numero : "+prelievo);
            head=(head+1)%maxdim;
            Nelem--;

            coda.notifyAll();

            return prelievo;
        }
    }
}
    

