package Server;

import java.io.IOException;

public class ServerMain {


    public static void main(String[] args) 
    {
        
        ServerImpl server=new ServerImpl(5);        //gli passi solo dimensione max coda, le crea lui poi nel costruttore (vedi serverimpl)
        try {
            server.runskeleton();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
