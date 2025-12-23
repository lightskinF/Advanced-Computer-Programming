package LoggingServer;

import java.io.IOException;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServerMain 
{


     public static void main(String[] args) throws IOException, NamingException, JMSException 
     {


        //setup code per poi mandare messaggi all'altro server, importante qui e lo passo a thread la connessione
        Hashtable<String, String> prop=new Hashtable<>();
        prop.put("java.naming.factory.initial" , "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url" , "tcp://127.0.0.1:61616");

        prop.put("queue.error", "error");
        prop.put("queue.info", "info");

        javax.naming.Context jndContext=new InitialContext(prop);

        QueueConnectionFactory factory=(QueueConnectionFactory)jndContext.lookup("QueueConnectionFactory");

        javax.jms.Queue coda_errori=(javax.jms.Queue)jndContext.lookup("error");
        javax.jms.Queue coda_info=(javax.jms.Queue) jndContext.lookup("info");

        QueueConnection conn=factory.createQueueConnection();
        //va bene qui, la sessione la deve creare ogni volta il thread, cosi come messaggio e sender e poi inviare. il thread la fa fare a serverimpl


        ServerImpl server=new ServerImpl(coda_errori, coda_info, conn);
        server.runskeleton();


     }
}
