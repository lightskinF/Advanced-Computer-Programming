import grpc
from concurrent import futures
import servizi_pb2
import servizi_pb2_grpc
import pymongo

client=pymongo.MongoClient() 
db=client["database_prova"]
collection=db["sensors"]
collection_temp=db["temp_data"]
collection_press=db["press_data"]

class Myservicer(servizi_pb2_grpc.FunzioniServicer):

    def getSensors(self, request, context):
        print("Ricevuta richiesta dalla Dashboard: ", request.messaggio)

        sensori_database=[] #sara una lista di dictionaryMONGODB, 
        for sensori in collection.find():
            sensori_database.append(sensori)

        print("Recuperati sensori nel Database, li mando alla Dashboard")

        for sensore in sensori_database:
            #yield sensore                   #è del tipo {"_id": id_ricevuto, "tipo": tipo}), dove l'id è un intero, NO! STAI RESTITUENDO UN DIZIONARIO MONGODB
                                            #E NON UN OGGETTO SENSOR DEL FILE.PROTO. QUINDI COSTRUISCI PRIMA L'OGGETTO E POI LO MANDI!!
            yield servizi_pb2.Sensor(id=int(sensore["_id"]), data_type=sensore["tipo"])


    def getMean(self, request, context):
        media=0
        i=0

        data_type=request.data_type
        id_sensore=request.sensor_id    #di quelli proto! ti sta mandando un ogetto sensor!

        if data_type=="temp":
            for oggetti in collection_temp.find({ "id_sensore": id_sensore }):  #e non _id, non stai cercando nella collection sensors ma in quella di temp o press sotto
                media=media+float(oggetti["valore_misura"])
                i=i+1
            mediatotale=media/i
            print("media calcolata per sensore ID :",id_sensore, " e richiesta ", data_type, ": ", mediatotale)

        elif data_type == "press":
            for oggetti2 in collection_press.find({ "id_sensore": id_sensore }):
                media=float(media+oggetti2["valore_misura"])
                i=i+1
            mediatotale=media/i
            print("media calcolata per sensore ID :",id_sensore, " e richiesta ", data_type, ": ", mediatotale)

        else:
            print("errore data type")
            mediatotale=0
            
        return servizi_pb2.StringMessage(calcolo_media=mediatotale)



if __name__=="__main__":
    s=grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    servizi_pb2_grpc.add_FunzioniServicer_to_server(Myservicer(), s)
    porta=s.add_insecure_port("localhost:0")
    print("server in ascolta a port ", porta)
    s.start()
    try:
        s.wait_for_termination()
    except KeyboardInterrupt:
        print("\nServer terminato volontariamente\n")
