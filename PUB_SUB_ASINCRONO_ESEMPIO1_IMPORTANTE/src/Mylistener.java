import javax.jms.*;
import javax.naming.*;

import org.apache.activemq.protobuf.Message;

public class Mylistener implements MessageListener{

    private TopicSubscriber subscriber;
    private TopicSession sessione;
    private TopicConnection connessione;

    public Mylistener(TopicSubscriber sub, TopicSession sessione_dachiudere, TopicConnection connection_dachiudere)
    {
        this.subscriber=sub;
        this.sessione=sessione_dachiudere;
        this.connessione=connection_dachiudere;
    }


    @Override
    public void onMessage(javax.jms.Message message) {      //FIRMA SEMPRE COSI, NON DIMENTICARLA!
        
        //quello passato come parametro
        try {
            TextMessage messaggio=(TextMessage)message;
            String miomessaggio=messaggio.getText();
            System.out.println("Ricevuto messaggio "+miomessaggio);

            if (miomessaggio.equals("fine"))
            {
                System.out.println("Procedo alla cleanup delle risorse");
                this.subscriber.close();
                this.sessione.close();
                this.connessione.close();
            }

        } catch (JMSException e) {
        
            e.printStackTrace();
        }
        
    }
    
}
