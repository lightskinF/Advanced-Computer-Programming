package Actuator;

import java.io.FileWriter;
import java.io.IOException;

import Client.ProxyClient;

public class Actuator {

    public static void main(String[] args) throws IOException {

        while (true) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println("[ACTUATOR] invoco funzione get per prelevare elemento dalla coda..");

            ProxyClient proxy=new ProxyClient(0);   //qui comando non necessario ma va be'
            int prelievo=proxy.getCmd();
            System.out.println("[ACTUATOR] prelevato comando numero: "+prelievo);


            try (FileWriter file = new FileWriter("cmdlog.txt", true)) {    //secondo campo per fare l'append, booleano
                file.write(prelievo+"\n");      //scrivi una stringa, senno ti scrive il carattere asci
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            
        }
        
    }
    
}
