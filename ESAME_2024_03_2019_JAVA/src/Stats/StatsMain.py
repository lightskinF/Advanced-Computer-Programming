import stomp
from Listener import MyListener
from time import sleep
import queue
import multiprocessing



if __name__=="__main__":

    c = stomp.Connection([('127.0.0.1', 61613)])
    #settalistener
    coda=multiprocessing.Queue(10)        #da passare al listener
    
    c.set_listener("", listener=MyListener(c, coda))

    c.connect(wait=True)
    print("Server in ascolto...")
    #PRIMA LA CONNECT E POI LA WAIT!

    c.subscribe("/topic/statistiche", id=1, ack="auto")                      #qui metti il nome fisico da java e non quello logico
    c.subscribe("/topic/tickets", id=1, ack="auto")

    try:
        sleep(60.0)     #dai il tempo di accettare le richieste
    finally:
        c.disconnect()
    
