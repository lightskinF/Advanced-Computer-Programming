package subscriber_side;

import java.io.IOException;

public interface Coda {
    
    public void put(String comando);
    public void svuota() throws IOException;
}
