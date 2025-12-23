package publisher_side;

import javax.jms.*;
import javax.naming.*;
import java.util.Hashtable;
import java.util.Random;

public class ControlStation {

    public static void main(String args[]) throws NamingException, JMSException
    {

        Hashtable <String, String> jndiproperties=new Hashtable<String, String>();
        jndiproperties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jndiproperties.put( "java.naming.provider.url", "tcp://127.0.0.1:61616");
        jndiproperties.put("topic.comandi", "topic_comandi_sensore");

        javax.naming.Context jndiContext=new InitialContext(jndiproperties);

        TopicConnectionFactory connfactory=(TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
        javax.jms.Topic topic=(javax.jms.Topic) jndiContext.lookup("comandi");
        
        TopicConnection conn=connfactory.createTopicConnection();
        TopicSession session=conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher pub=session.createPublisher(topic);

        for (int i=0; i<20; i++)
        {
            String comando;
            Random number=new Random();
            int scelta_casuale=number.nextInt(3);
            if (scelta_casuale==0) 
            {
                comando="startSensor";
            }
            else if (scelta_casuale==1) 
            {
                comando="stopSensor";    
            }
            else    //2
            {
                comando="read";
            }

            TextMessage message=session.createTextMessage();
            message.setText(comando);
            System.out.println("[PUBLISHER] messaggio numero "+i+". Comando : "+comando+". Procedo alla pubblicazione");
            pub.publish(message);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }

        }

        System.out.println("Effettuo cleanup risorse");
        pub.close();
        session.close();
        conn.close();
        System.out.println("Fine.");

    }
    
}
