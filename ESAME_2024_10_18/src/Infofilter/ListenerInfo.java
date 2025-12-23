package Infofilter;

import java.io.FileWriter;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ListenerInfo implements MessageListener
{

    @Override
    public void onMessage(Message message) 
    {

        try {

            
            MapMessage ricevuto=(MapMessage) message;
            String messagelog = ricevuto.getString("messagelog");
            int tipo=ricevuto.getInt("type");
            System.out.println("ricevuto messaggio :"+messagelog+" "+tipo);
            FileWriter file=new FileWriter("info.txt", true);
            file.write(messagelog+"\n");
            file.close();



        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
        
}


