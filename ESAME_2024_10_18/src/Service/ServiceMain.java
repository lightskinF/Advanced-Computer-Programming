package Service;

import java.io.IOException;
import java.util.Random;

public class ServiceMain {

    public static void main(String[] args) throws IOException {

        int tipo=0;
        String messagelog="";

        ServiceProxy proxy=new ServiceProxy();
        

        for (int i=0; i<10; i++)
        {
            Random casuale=new Random();
            tipo=casuale.nextInt(3);

            if (tipo==0 || tipo==1) {messagelog="success";}
            else {messagelog="fatal";}

            System.out.println("[SERVICE] richiesta numero "+i+" con tipo= "+tipo+" e messageLog: "+messagelog);

            proxy.log(messagelog, tipo);    //upcall

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
