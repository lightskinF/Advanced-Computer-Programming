package Infofilter;

import java.io.IOException;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class InfofilterMain {

public static void main(String[] args) throws IOException, NamingException, JMSException 
     {


        //setup code 
        Hashtable<String, String> prop=new Hashtable<>();
        prop.put("java.naming.factory.initial" , "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url" , "tcp://127.0.0.1:61616");

        prop.put("queue.info", "info");       //diversa da error e solo una rispetto a servermain siccome devo ricevere solo su questa
        

        javax.naming.Context jndContext=new InitialContext(prop);

        QueueConnectionFactory factory=(QueueConnectionFactory)jndContext.lookup("QueueConnectionFactory");

        javax.jms.Queue coda_info=(javax.jms.Queue)jndContext.lookup("info");

        QueueConnection conn=factory.createQueueConnection();
        conn.start();

        QueueSession session=conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueReceiver receiver=session.createReceiver(coda_info);
        //set listeer
        ListenerInfo listener=new ListenerInfo();
        receiver.setMessageListener(listener);


     }
    

}
