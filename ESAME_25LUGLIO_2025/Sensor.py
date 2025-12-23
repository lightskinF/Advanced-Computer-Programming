from threading import Thread
from random import choice, randint
from time import sleep
from Proxy import Proxy
import sys



def thread_func(tipo_comando, valore_comando, porta_server):

    proxy=Proxy(porta_server)
    proxy.measure(tipo_comando, valore_comando)     #upcall



if __name__=="__main__":

    PORTA=int(sys.argv[1])          #invoca da terminale e passi la porta di dove sta ascoltando il server, che passi al thread, che passa al proxy per instaurare la connessione

    threads=[]

    for i in range(5):

        tipo=choice(["temperatura", "pressione", "umidita'"])
        value=float(randint(1, 100) )

        print("[CLIENT] thread numero "+str(i)+" inizia. TIPO: "+tipo+", VALORE: "+str(value))

        t=Thread(target=thread_func, args=(tipo, value, PORTA))
        threads.append(t)
        t.start()       #avevi mancato le parentesi e non te ne eri accorto, ma di che parliamo.. debugga subito ogni file, assurdo
        sleep(1.0)
        

    for thread in threads:
        t.join()
        print("Thread terminato..")
