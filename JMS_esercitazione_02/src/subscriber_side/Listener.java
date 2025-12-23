package subscriber_side;

import javax.jms.*;

public class Listener implements MessageListener{

    private CodaImpl coda_comandi_sensori;

    public Listener(CodaImpl coda_comandi)
    {
        coda_comandi_sensori=coda_comandi;
    }

    @Override
    public void onMessage(javax.jms.Message message)
    {
    
        try {

            TextMessage ricevuto=(TextMessage) message;
            String comando=ricevuto.getText();
            System.out.println("Ricevuto comando : "+comando);

            TManager nuovo_thread=new TManager(comando, coda_comandi_sensori);
            nuovo_thread.start();

        } catch (JMSException e) {
           
            e.printStackTrace();
        }
    }
    
}
