package Manager;

import java.io.FileWriter;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

public class Listener implements MessageListener
{

    private TopicConnection connessione;
    private javax.jms.Topic stats;
    private javax.jms.Topic tickets;

    public Listener (TopicConnection c, javax.jms.Topic topic_statistiche, javax.jms.Topic topic_tickets)
    {
        connessione=c;
        stats=topic_statistiche;
        tickets=topic_tickets;

    }


    @Override
    public void onMessage(Message message) 
    {
        MapMessage messaggio_asincrono_ricevuto=(MapMessage) message;

        try {


            String richiesta=messaggio_asincrono_ricevuto.getString("richiesta");

            if (richiesta.equals("stats"))
            {
                TopicSession session=connessione.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                TopicPublisher pub=session.createPublisher(stats);  //stats è il topic su cui mandi i messaggi!!!!!!FONDAMENTALE!!!!!!!!
                TextMessage messaggio=session.createTextMessage();
                String valore_campo_value=messaggio_asincrono_ricevuto.getString("value");      //sara sempre sold

                System.out.println("[MANAGER] Ricevuta richiesta stats con valore "+valore_campo_value);

                messaggio.setText(valore_campo_value);
                pub.publish(messaggio);                     //attento a non chiamrlo message come quel parametro di defaulr che da onMessage

            }

            else if (richiesta.equals("buy"))
            {
                String artista=messaggio_asincrono_ricevuto.getString("value");     //jovanotti o gli altri
                //salvo su file
                FileWriter file=new FileWriter("tickets.txt", true);
                file.write("artist: "+artista+"\n");
                file.close();

                System.out.println("[MANAGER] Ricevuta richiesta buy con artista "+artista);
                //invio messaggio
                TopicSession session=connessione.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                TopicPublisher pub=session.createPublisher(tickets);         //FONDAMENTALE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! TOPIC DIVERSI
                TextMessage messaggio=session.createTextMessage();
                
                messaggio.setText(artista);     //vedi, prelevato al primo riga, dal mapmessage
                pub.publish(messaggio);             //NON MESSAGE..
                
            }
            else 
            {
                System.out.println("ERRORE");
            }


        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
