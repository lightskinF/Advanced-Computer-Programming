package Interfaccia;

import java.io.IOException;
import java.net.UnknownHostException;

public interface IMagazzino {

    public void deposita(String articolo, int id) throws IOException;
    public int preleva(String articolo) throws UnknownHostException, IOException;
    
}
