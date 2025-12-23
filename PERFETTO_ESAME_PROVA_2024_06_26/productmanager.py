import grpc
import flask
from concurrent import futures
import servizi_pb2
import servizi_pb2_grpc
from threading import Semaphore
import flask, requests

laptop_queue=[]
MAXdimensionecoda=5

puoi_produrre=Semaphore(MAXdimensionecoda)
puoi_consumare=Semaphore(0) #all'inizio non possono consumare!

mutexP=Semaphore()
mutexC=Semaphore()  #default 1

class Myservicer(servizi_pb2_grpc.FunzioniServicer):


    def Buy(self, request, context):

        if len(laptop_queue)==0:
            print("coda attualmete vuota")
        print("RICHIESTA DI ACQUISTO DAL CLIENT.")

        puoi_consumare.acquire()
        mutexC.acquire()

        prelevato=laptop_queue.pop(0)   #sara di tipo serviza_pb2.Prodotto. ATTEENTO AI TIPI!!!!!!!!!   
        id_prodotto=prelevato.serial_number   #puoi mandare solo stringhe con grpc!
        print("prelevato dalla coda, prodotto con serial number: ", id_prodotto, ". Mando richiesta flask e mando al client l'ID prodottom prelevato")
        risposta_flask_server=requests.post("http://127.0.0.1:5000/update_history", json={"operation": "compra", "serial_number": str(id_prodotto)})
        print("risposta ricevuta da flask server : ", risposta_flask_server.json())
        
        mutexC.release()
        puoi_produrre.release()

        return servizi_pb2.Prodotto(serial_number=id_prodotto)
        

    def Sell(self, request, context):

        idproduct=request.serial_number
        if (len(laptop_queue)==MAXdimensionecoda):  #lo metto giusto per farlo mostrare per averne contezza
            print("coda piena ")
        print("RICHIESTA DI VENDITA DAL CLIENT.")

        puoi_produrre.acquire()
        mutexP.acquire()

        print("inserisco ", str(idproduct), " nella coda; mando richiesta al server flask e conferma ACK al client")
        laptop_queue.append(request)  ##sara di tipo serviza_pb2.Prodotto, ATTENTO AI TIPI!!!!!!!
        risposta_flask_server=requests.post("http://127.0.0.1:5000/update_history", json={"operation": "vendi", "serial_number": str(idproduct)})
        print("risposta ricevuta da flask server : ", risposta_flask_server.json())

        mutexP.release()
        puoi_consumare.release()

        return servizi_pb2.Ack(esito=True)




if __name__=="__main__":



    s=grpc.server(thread_pool=futures.ThreadPoolExecutor(max_workers=10))
    servizi_pb2_grpc.add_FunzioniServicer_to_server(Myservicer(), s)
    porta_scelta=s.add_insecure_port("localhost:0")
    print("Server in ascolto su porta ", porta_scelta)
    s.start()
    try:
        s.wait_for_termination()
    except KeyboardInterrupt:
        print("chiusa connessione in maniera volontaria")
        s.stop(grace=1)