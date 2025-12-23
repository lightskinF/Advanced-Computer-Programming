package Manager;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ManagerMain {

    public static void main(String[] args) throws NamingException, JMSException 
    {
        

        Hashtable<String, String> prop=new Hashtable<>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");

        prop.put("topic.Request", "Topic_Richieste");
        //QUESTE SERVON AL LISTENER PER MANDARE I MESSAGGI SU QUESTE SPECIFICHE DESTINAZIONI, ALLE QUALI SI COLLEGHERA ANCHE IL SERVER!
        prop.put("topic.stats", "statistiche");
        prop.put("topic.tickets", "tickets");


        javax.naming.Context jndiContext=new InitialContext(prop);

        TopicConnectionFactory factory=(TopicConnectionFactory)jndiContext.lookup("TopicConnectionFactory");
        Topic topic=(Topic)jndiContext.lookup("Request");
        //QUESTI TOPIC, DESTINAZIONI, DEVI PASSARE AL LISTENER PER INVIARE MESSAGGI.
        Topic topic_stats=(Topic) jndiContext.lookup("stats");
        javax.jms.Topic topic_tickets=(Topic) jndiContext.lookup("tickets");

        TopicConnection conn=factory.createTopicConnection();
        conn.start();

        TopicSession session=conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicSubscriber sub=session.createSubscriber(topic);

         //primo topic per ricevere dal client e le atre due per mandare messaggi al server

        Listener listener=new Listener(conn, topic_stats, topic_tickets);     //conn serve come connessione dove riceve i messaggi. i topic li passi per instaurare connessione e mandare messaggi
        sub.setMessageListener(listener);

    }

}
