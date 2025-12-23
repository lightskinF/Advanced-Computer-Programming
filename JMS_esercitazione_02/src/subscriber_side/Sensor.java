package subscriber_side;

import javax.jms.*;
import javax.naming.*;
import java.util.Hashtable;


public class Sensor {

    public static void main (String args[]) throws NamingException, JMSException, InterruptedException
    {

        CodaImpl Coda_COMANDI=new CodaImpl(5);

        Hashtable <String, String> jndiproperties=new Hashtable<String, String>();
        jndiproperties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jndiproperties.put( "java.naming.provider.url", "tcp://127.0.0.1:61616");
        jndiproperties.put("topic.comandi", "topic_comandi_sensore");

        javax.naming.Context jndiContext=new InitialContext(jndiproperties);

        TopicConnectionFactory connfactory=(TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
        javax.jms.Topic topic=(javax.jms.Topic) jndiContext.lookup("comandi");
        
        TopicConnection conn=connfactory.createTopicConnection();
        TopicSession session=conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        TopicSubscriber sub=session.createSubscriber(topic);

        Listener MyListener=new Listener(Coda_COMANDI);
        sub.setMessageListener(MyListener);
        conn.start();

        System.out.println("In attesa di messaggi...");

        Thread.sleep(10000);

        TExecutor thread_svuota=new TExecutor(Coda_COMANDI);
        thread_svuota.start();

        thread_svuota.join();
        System.out.println("Effettuo cleanup risorse...");
        sub.close();
        session.close();
        conn.close();
        System.out.println("Terminato Sensor.");
        
    }
  
    
}
