package Client;

import java.io.IOException;

public class ClientThread extends Thread{

    private int command;

    public ClientThread (int comando)
    {
        this.command=comando;
    }

    public void run()
    {

        ProxyClient client=new ProxyClient(command);

        for (int i=0; i<3; i++)
        {
            System.out.println("[THREAD] Richiesta "+i+" per il comando numero "+command);
            try {
                client.sendCmd(command);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}
