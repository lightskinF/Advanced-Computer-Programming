package Client;

import java.util.Random;

public class ClientB {

    public static int NUM_THREADS=5;

    public static void main(String[] args) throws InterruptedException 
    {

        ClientThread [] threads=new ClientThread[5];
        
        Random number=new Random();
        int numerocasuale=number.nextInt(3)+2;

        for (int i=0; i<NUM_THREADS; i++)
        {
            Thread.sleep(numerocasuale*1000);
            System.out.println("[CLIENT_B] numero "+i+" startato. Richiesta preleva");

            threads[i]=new ClientThread("preleva");
            threads[i].start();
        }

        for (int i=0;i<NUM_THREADS; i++)
        {
            threads[i].join();
            System.out.println("Thread terminato correttamente");
        }
        

    }
    
}
