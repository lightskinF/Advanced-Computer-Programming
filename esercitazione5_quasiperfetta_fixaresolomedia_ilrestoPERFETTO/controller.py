from flask import Flask, request
import pymongo
from pymongo.errors import DuplicateKeyError

client=pymongo.MongoClient()    #default /127.0.0.1:27017

db=client["database_prova"]

collection=db["sensors"]
collection_temp=db["temp_data"]
collection_press=db["press_data"]

#collection.delete_many({})
#collection_temp.delete_many({})
#collection_press.delete_many({})    #cancello collection ad ogni ciclo ripeto che a volte non riesco a tener bene traccia dei vari document

                                    #basta gestire con un return e except DuplicateKeyError per elementi gia inseriti!
app=Flask(__name__)

@app.post("/sensor")
def post():
    id_ricevuto=request.get_json()["id"]
    tipo=request.get_json()["data_type"]
    print(f"Ricevuta richiesta registrazione, sensore: {id_ricevuto}, tipo: {tipo}")

    try:
        print("inserisco nel database...")
        collection.insert_one({"_id": id_ricevuto, "tipo": tipo})       
    except DuplicateKeyError:
        print("attento a _id in mongo. Sensore con questo _id già presente nel Database!")
        return {"esito": "errore ID. Nel database già presente sensore con questo ID! "}, 400

    return {"esito": "success"}, 200

        

@app.post("/data/<data_type>")
def post_due(data_type):
    id=request.get_json()["sensor_id"]
    misura=request.get_json()["data"]
    tipo_misurazione=data_type
    print(f"Ricevuta misurazione: id {id}, misura {misura}, tipo {tipo_misurazione}. Procedo ad inserirla nel database opportuno")

    misurazione={
        "id_sensore":id,
        "valore_misura":misura,
        "tipo_misura":tipo_misurazione                  #però la misurazione te le fa aggiungere dalla traccia, è solo nella registrazione, che giustamente, hai un id univco per quel sensore
    }
                                                        #però l'id univoco, quel sensore, puo fare piu misurazioni! COERENTE. sarebbe la fk in questa collection, e pk nell'altra, percio deve essere univoca in collection sensors!!!
    if data_type=="temp":
        collection_temp.insert_one(misurazione)
    elif data_type=="press":
        collection_press.insert_one(misurazione)
    else:
        print("errore data_type")
        return {"esito": "ERROR: invalid data_type"}, 400

    return {"esito":"succes"}, 200
    





if __name__=="__main__":
    app.run()

    print("prova database: \n")
    print("Stampa collection sensors: ")

    for sensori in collection.find():
        print(sensori)

    print("Stampa collection temp: ")

    for temp in collection_temp.find():
        print(temp)

    print("Stampa collection press: ")

    for press in collection_press.find():
        print(press)
