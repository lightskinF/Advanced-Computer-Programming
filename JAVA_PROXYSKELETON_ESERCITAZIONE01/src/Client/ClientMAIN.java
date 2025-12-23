package Client;

import java.util.Random;

public class ClientMAIN {

    public static int NUMTHREADS=5;

    public static void main(String[] args) {
        

        ClientThread[] threads=new ClientThread[NUMTHREADS];

        for (int i=0; i<NUMTHREADS; i++)
        {
            Random number=new Random();
            int numerocasuale=number.nextInt(3)+2;
            try {
                Thread.sleep(numerocasuale*1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int comando_send=number.nextInt(4);
            System.out.println("THREAD "+i+" COMANDO SEND: "+comando_send);

            threads[i]=new ClientThread(comando_send);
            threads[i].start();
        }

        for (int i=0; i<NUMTHREADS; i++)
        {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Thread terminato");
        }
    }
    
}
