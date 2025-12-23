import java.util.Hashtable;

import javax.jms.*;
import javax.naming.*;

import org.apache.activemq.broker.region.Topic;

//facciamolo asincrono e che filtra i messaggi


public class Subscriber {

    public static void main (String args[]) throws NamingException, JMSException
    {
        Hashtable<String, String> jndiproperties =new Hashtable<String, String>();
        jndiproperties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jndiproperties.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        jndiproperties.put("topic.mionuovotopic", "topicfisico");

        Context jndiContext=new InitialContext(jndiproperties);

        TopicConnectionFactory connfactory=(TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
        javax.jms.Topic topic_destination= (javax.jms.Topic) jndiContext.lookup("mionuovotopic");   //attento a topic

        TopicConnection connection=(TopicConnection) connfactory.createTopicConnection();

        TopicSession session=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        TopicSubscriber sub=session.createSubscriber((javax.jms.Topic) topic_destination, "ricezione_abilitata='sì'", false);
        //qui sopra fai il filtraggio!!!!!!!!!!!!!!!


        Mylistener listener= new Mylistener(sub, session, connection);

        sub.setMessageListener(listener);   //prima setti il listener e poi puoi far partire la connessione in caso di asincrona
        //SUL SUBSCRIBER IMPOSTI IL LISTENER NON SULLA SESSION!!!
        connection.start();

        System.out.println("Attendo messaggi dal publisher");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("Fine");

        

    }
    
}
