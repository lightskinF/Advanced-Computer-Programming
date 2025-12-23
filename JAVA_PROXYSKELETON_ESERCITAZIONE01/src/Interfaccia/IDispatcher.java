package Interfaccia;

import java.io.IOException;
import java.net.UnknownHostException;

public interface IDispatcher {

    public void sendCmd(int command) throws UnknownHostException, IOException;
    public int getCmd() throws UnknownHostException, IOException;
    
}
