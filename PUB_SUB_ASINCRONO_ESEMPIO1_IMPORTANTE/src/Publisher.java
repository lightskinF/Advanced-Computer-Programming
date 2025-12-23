import javax.jms.*;
import javax.management.JMException;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.activemq.broker.region.Topic;

import javax.naming.*;

import java.util.Hashtable;

public class Publisher{


    public static void main(String[] args)
    {
    
    Hashtable <String, String> jndiproperties=new Hashtable<String, String>();

    jndiproperties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
    jndiproperties.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
    jndiproperties.put("topic.mionuovotopic", "topicfisico");

    try {

        Context jndiContext=new InitialContext(jndiproperties);

        TopicConnectionFactory topic_factory=(TopicConnectionFactory)jndiContext.lookup("TopicConnectionFactory");
        javax.jms.Topic topic=(javax.jms.Topic) jndiContext.lookup("mionuovotopic");    //ATTENTO AL TOPIC CHE METTI!

        TopicConnection topic_conn=(TopicConnection) topic_factory.createTopicConnection();
        TopicSession session=(TopicSession) topic_conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher pub=session.createPublisher( (javax.jms.Topic) topic);    //perche fa il casting senno m i da errore?

        System.out.println("Procedo a mandare messaggi");

        for (int i=0; i<10;i++)
        {
            TextMessage message=session.createTextMessage();
            message.setText("Ciao, dal publisher numero "+i);   //facciamo in modo che il ricevente possa ricevere solo messaggi dai subscriber "pari"
            if ((i%2)==0)
            {
                message.setStringProperty("ricezione_abilitata", "sì");
            }
            else
            {
                message.setStringProperty("ricezione_abilitata", "no");
            }

            pub.publish(message);
        }

        TextMessage endMessage = session.createTextMessage();
        endMessage.setText("fine");
        endMessage.setStringProperty("ricezione_abilitata", "sì");
        pub.publish(endMessage);

        System.out.println("Messaggi inviati. Chiudo le risorse.");
        pub.close();
        session.close();
        topic_conn.close();


    } catch (Exception e) {
        e.printStackTrace();
    }

}
    
}
