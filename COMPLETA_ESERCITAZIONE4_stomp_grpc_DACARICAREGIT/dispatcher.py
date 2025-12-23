from concurrent import futures
from time import sleep
import stomp
import grpc
import multiprocessing
import servizi_pb2
import servizi_pb2_grpc
import sys
import signal




class MyListener(stomp.ConnectionListener):

    def __init__(self, connessioneSTOMP, serverport):
        self.connection=connessioneSTOMP
        self.portaconnserver=serverport

    def on_message(self, frame):

        if frame.body=="deposita":
            id_valore = int(frame.headers["id"])
            prodotto=str(frame.headers["prodotto"])     #uso cosi con i dictionary anche con flask
            print("ricevuto messaggio da client ", frame.body, ", ", id_valore, ", ", prodotto)

            with grpc.insecure_channel(f"127.0.0.1:{self.portaconnserver}") as canale:
                stub=servizi_pb2_grpc.FunzioniStub(canale)
                response=stub.Deposita(servizi_pb2.Articolo(id=id_valore, prodotto=prodotto))
                print("ricevuto da server:", response.messaggio)
                self.connection.send("/queue/Risposta", body=response.messaggio)

        elif frame.body=="preleva":
            print("ricevuto messaggio da client ", frame.body)
            with grpc.insecure_channel(f"127.0.0.1:{self.portaconnserver}") as canale2:
                stub2=servizi_pb2_grpc.FunzioniStub(canale2)
                response=stub2.Preleva(servizi_pb2.Richiesta(messaggio="preleva"))
                print(f"ricevuto da server: {response.id} e ", response.prodotto)
                self.connection.send("/queue/Risposta", body=str(response.id)+" "+response.prodotto)

        else:   #svuota
            with grpc.insecure_channel(f"127.0.0.1:{self.portaconnserver}") as canale3:
                stub3=servizi_pb2_grpc.FunzioniStub(canale3)
                response=stub3.Svuota(servizi_pb2.Richiesta(messaggio="svuota"))
                print("ricevuto messaggio da server: ", response.messaggio)
                self.connection.send("/queue/Risposta", body=response.messaggio)

        



def runfunction(portaserver):
    conn=stomp.Connection([("localhost", 61613)])
    conn.set_listener(" ", MyListener(conn, portaserver))
    conn.connect(wait=True)
    conn.subscribe("/queue/Richiesta", id=1, ack="auto")
    print("Dispatcher in ascolto...")

    try:
        while True:         #va sempre ma se premi ctrlc si interrompe e chiudi la connessione. lo fai tu manualmente!
            sleep(1)
    except KeyboardInterrupt:
        print("Interrotto processo dispatcher.")
    finally:
        conn.disconnect()

def termina_tutti_processi(processi):
    print("Terminazione ricevuta, chiudo i processi...")        #mi serve per il fatto del while sopra
    for p in processi:
        p.terminate()
    for p in processi:
        p.join()


if __name__=="__main__":

    joinali=[]
    try:
        porta=int(sys.argv[1])
    except KeyError:
        print("Quando runni il dispatcher da terminale ASSICURATI DI INSERIRE PORT NUMBER DEL SERVER!")

    try:
        for i in range(16):
            p=multiprocessing.Process(target=runfunction, args=(porta, ))
            p.start()
            joinali.append(p)

        for pr in joinali:
            pr.join()

    except KeyboardInterrupt:               #I PROCESSI NON TERMINANO FINVH NON PREMO MANUALMENTE CTRL+C!!!!!!!!!!!
        termina_tutti_processi(joinali)
        print("Dispatcher terminato.")

    

    



