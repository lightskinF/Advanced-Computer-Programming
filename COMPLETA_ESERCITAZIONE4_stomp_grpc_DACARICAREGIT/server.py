import servizi_pb2
import servizi_pb2_grpc
import grpc
import queue
from concurrent import futures

coda=queue.Queue(5)


class Servicer(servizi_pb2_grpc.FunzioniServicer):
    def Deposita(self, request, context):
        print("ricevuta richiesta da dispatcher di tipo deposita")
        if coda.full():
            print("coda piena, ritento dopo\n")
            return servizi_pb2.Esito(messaggio="not deposited yet")
        else:
            id_articolo=request.id
            product=request.prodotto    #mandi e ricevi
            prodotto_to_add=servizi_pb2.Articolo(id=id_articolo, prodotto=product)
            coda.put(prodotto_to_add)
            print("prodotto ", prodotto_to_add," aggiunto correttamente.")
            return servizi_pb2.Esito(messaggio="deposited")


    def Preleva(self, request, context):
        print("ricevuta richiesta da dispatcher di tipo ", request.messaggio)
        if coda.empty():
            print("Coda vuota, nessun articolo da prelevare")
            # Restituisci un articolo vuoto o con valori sentinel
            return servizi_pb2.Articolo(id=0, prodotto="Nessun prodotto")
        else:
            risposta_da_dare=coda.get() #sarebbe un servizi_pb2.Articolo
            return risposta_da_dare

    def Svuota(self, request, context):
        print("ricevuta richiesta da dispatcher: ", request.messaggio)
        while not coda.empty():
            try:
                coda.get_nowait()  # prende senza bloccare, altrimenti get() potrebbe bloccare se vuota
            except queue.Empty:
                break
        return servizi_pb2.Esito(messaggio="coda svuotata!")





def serve():
    server=grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    servizi_pb2_grpc.add_FunzioniServicer_to_server(Servicer(), server)
    porta=server.add_insecure_port("127.0.0.1:0")  #imposto io porta, non 0
    print("Server in ascolto al porto ", porta)
    server.start()
    try:
        server.wait_for_termination()       #basta macello. premi Ctrl+C e te ne esci pulito
    except KeyboardInterrupt:
        print("\nServer interrotto manualmente.")
        server.stop(grace=1)

if __name__=="__main__":
    serve()




