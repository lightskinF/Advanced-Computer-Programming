import stomp
from random import choice, randint
import uuid
from time import sleep


def genera_id_univoco():
    id_gia_generati=[]
    gia_generato=False          #mi assicuro che id generato sia univoco
    while(not gia_generato):
        id=randint(1, 10000)
        if id in id_gia_generati:
            gia_generato==False     #continua a provare
        else:                                                               #else non è in quelli gia generati
            id_gia_generati.append(id)
            gia_generato==True
        return id



class MyListener(stomp.ConnectionListener):
    def on_message(self, frame):
        print("ricevuto messaggio dal dispatcher: ", frame.body)



def deposita(connessione, request_type):    #ma questo solo per deposita!

    id_univoco=genera_id_univoco()
    scelta=["laptop", "smartphone"]
    prodotto=choice(scelta)
    print("Richiesta generata: tipo: ", request_type, "id: ", id_univoco, "prodotto: ", prodotto, ". Procedo all'invio")

    connessione.send("/queue/Richiesta", body=request_type, headers={"id": id_univoco, "prodotto":prodotto})


def preleva(connection):
    print("Richiesta: preleva. Procedo all'invio messaggio.")
    connection.send("/queue/Richiesta", body="preleva")



def svuota(connection):
    print("Richiesta: svuota. Procedo all'invio messaggio.")
    connection.send("/queue/Richiesta", body="svuota")





if __name__=="__main__":

    conn=stomp.Connection([("localhost", 61613)])       #default 61613
    conn.set_listener(" ", MyListener())
    conn.connect(wait=True)
    conn.subscribe("/queue/Risposta", id=1, ack="auto")

    for i in range(10):
        deposita(connessione=conn, request_type="deposita")
        sleep(1.0)
    for i in range(5):
        preleva(connection=conn)
        sleep(1.0)
    
    svuota(connection=conn)

    sleep(5)   #5 secondi e chiude la connessione

    conn.disconnect()