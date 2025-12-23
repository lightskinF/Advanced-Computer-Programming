package subscriber_side;

public class TManager extends Thread{

    private String comando;
    private CodaImpl codabuffer;

    public TManager(String comando_ricevuto, CodaImpl coda_buffer_comandi)
    {
        this.comando=comando_ricevuto;
        this.codabuffer=coda_buffer_comandi;
    }


    public void run()
    {
        System.out.println("[WORKERTHREAD] Procedo ad inserire comando "+comando+" in coda");
        codabuffer.put(comando);
    }
    
}
