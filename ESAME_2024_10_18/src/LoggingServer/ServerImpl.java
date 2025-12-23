package LoggingServer;



import javax.jms.*;


public class ServerImpl extends ServerSkeleton
{

    private javax.jms.Queue coda_errori;        //ISTANZIATA NEL MAIN IL SETUP DELLE CODE, QUI PASSO IL NECESSARIO
    private javax.jms.Queue coda_info;
    private javax.jms.QueueConnection connection;

    public ServerImpl (javax.jms.Queue coda1, javax.jms.Queue coda2, javax.jms.QueueConnection conn)    //importante istanziarlo, nel main, creei li le code.
    {
        coda_errori=coda1;
        coda_info=coda2;
        connection=conn;
    }

    //mio modo di pensare per rendere log in mutua eslusione. lo richiamo in log.
    //SAREBBE METODO IMPLEMENTATO DA INTERFACCIA E INVOCATO DAL THREAD
    private void impl(String messaggiooo, int interooo) throws JMSException //messo synchronized ma non va, proviamo senza
    {

        QueueSession session=connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        MapMessage messaggio_da_inviare=session.createMapMessage();

        if (interooo==2)
        {
            QueueSender sender=session.createSender(coda_errori);       //se tipo è 2 devo inviarlo su coda errpri!
            messaggio_da_inviare.setString("messagelog", messaggiooo);
            messaggio_da_inviare.setInt("type", interooo);
            System.out.println("invio a coda errori!");
            sender.send(messaggio_da_inviare);
        }

        else 
        {

            QueueSender sender=session.createSender(coda_info); //altrimenti sull'altra coda!
            messaggio_da_inviare.setString("messagelog", messaggiooo);
            messaggio_da_inviare.setInt("type", interooo);
            System.out.println("invio a coda info!");
            sender.send(messaggio_da_inviare);

        }
    }


     public void log (String stringa, int intero) //invocata dal thread, richiama la funzione di sopra synchronized
     {
        try {


                impl(stringa, intero);



        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     }

}
