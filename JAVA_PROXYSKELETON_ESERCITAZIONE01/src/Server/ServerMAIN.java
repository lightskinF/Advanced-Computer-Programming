package Server;

import java.io.IOException;

public class ServerMAIN {

    public static void main(String[] args) 
    {

        ServerImpl server=new ServerImpl(5);
        try {
            server.runskeleton();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}


