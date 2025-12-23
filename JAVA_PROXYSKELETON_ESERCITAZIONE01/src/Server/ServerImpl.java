package Server;

import java.io.IOException;
import java.net.UnknownHostException;

import Coda.Coda_Impl;

public class ServerImpl extends ServerSkeleton{

    private Coda_Impl coda;

    public ServerImpl(int dimensionecoda)
    {
        coda=new Coda_Impl(dimensionecoda);
    }

    //override
    public void sendCmd(int command) throws UnknownHostException, IOException
    {
        //upcall metodo di coda
        coda.inserisci(command);
    }

    //override
    public int getCmd() throws UnknownHostException, IOException
    {
        int prelievo=coda.preleva();
        return prelievo;
    }


    
}
