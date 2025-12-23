import stomp
from ServerImpl import ServerImpl



if __name__=="__main__":

    c = stomp.Connection([('127.0.0.1', 61613)])
    c.connect(wait=True)

    #non devi ricevere in questo caso ma solo inviare
    #LA CONNESSIONE LA PASSI AL SERVER IMPL, TRAMITE LA MQUALE INVIA I MESSAGGI AL GIUSTO TOPIC!

    serverimpl=ServerImpl(c)
    serverimpl.runskeleton()

     # no try finally:
     #no   c.disconnect()