package Interfaccia;

import java.io.IOException;
import java.net.UnknownHostException;



public interface ILog {

    public void log (String stringa, int intero) throws UnknownHostException, IOException;
}