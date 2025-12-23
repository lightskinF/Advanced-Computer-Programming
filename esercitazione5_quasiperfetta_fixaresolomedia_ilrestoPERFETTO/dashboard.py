import grpc 
import servizi_pb2
import servizi_pb2_grpc
import sys


if __name__=="__main__":

    try:
        porta=sys.argv[1]
    except KeyError:
        print("\nPassa la porta del server\n")

    with grpc.insecure_channel(f"localhost:{porta}") as channel:
        stub=servizi_pb2_grpc.FunzioniStub(channel)

        iteratori=stub.getSensors(servizi_pb2.Empty(messaggio="Mandami tutti i sensori presenti nel database"))
        
        for risposte_yeld in iteratori:
            print(f"Trovato sensore: ID={risposte_yeld.id}, tipo={risposte_yeld.data_type}")

            risposta_server=stub.getMean( servizi_pb2.MeanRequest(sensor_id=risposte_yeld.id, data_type=risposte_yeld.data_type) )

            print("Media ricevuta per sensore ", risposte_yeld.id, " e tipo ", risposte_yeld.data_type, ": ", float(risposta_server.calcolo_media))  #float() anche se in file proto è float ma tu forza sempre la cosa, non si sa mai