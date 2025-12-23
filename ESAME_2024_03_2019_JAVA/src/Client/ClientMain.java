package Client;

import java.util.Hashtable;
import java.util.Random;

import javax.jms.*;
import javax.naming.*;


public class ClientMain {

    public static void main(String[] args) throws NamingException, JMSException {
        

        Hashtable<String, String> prop=new Hashtable<>();
        prop.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        prop.put("java.naming.provider.url", "tcp://127.0.0.1:61616");
        prop.put("topic.Request", "Topic_Richieste");

        javax.naming.Context jndiContext=new InitialContext(prop);

        TopicConnectionFactory factory=(TopicConnectionFactory)jndiContext.lookup("TopicConnectionFactory");
        Topic topic=(Topic)jndiContext.lookup("Request");

        TopicConnection conn=factory.createTopicConnection();
        //conn.start();

        TopicSession session=conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher pub=session.createPublisher(topic);

        MapMessage message=session.createMapMessage();


        for (int i=0; i<20; i++)
        {
            Random casuale=new Random();
            int numero_per_type=casuale.nextInt(2);

            System.out.println("Richiesta numero "+i+" dal client");

            if (numero_per_type==0)     //oppure facevi i%2==0
            {
                //buy
                message.setString("richiesta", "buy");  //mapmessage, filtro su "richiesta"
                int numero_per_value=casuale.nextInt(3);
                String artista="";
                
                if (numero_per_value==0) {artista="Jovanotti";}
                else if (numero_per_value==1) {artista="Ligabue";}
                else {artista="Negramaro";}

                message.setString("value", artista);  //mapmessage, filtro su "value"

                System.out.println("INVIO richiesta buy per "+artista+".");
                pub.publish(message);

            }

            else
            {
                message.setString("richiesta", "stats");    //mapmessage sempre
                message.setString("value", "Sold");

                System.out.println("INVIO richiesta di tipo stats.");
                pub.publish(message);

            }


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        

        
    }

}
