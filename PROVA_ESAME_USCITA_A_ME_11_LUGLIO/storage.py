from flask import Flask, request
import pymongo

app=Flask(__name__)



client = pymongo.MongoClient("localhost", 27017)
db = client.ge["database_prova_11_luglio"]
collection = db["cartella_esami_medici"]


@app.put("/")
def operazione_create():
    
    print("Ricevuta richiesta di inserimento nel DB. Procedo")

    oggetto_da_inserire={                                       #questo dictionary è un documento
        "identificativo":request.get_json()["identificativo"],
        "paziente":request.get_json()["paziente"],
        "email":request.get_json()["email"],
        "tipo":request.get_json()["tipo"],
        "data":request.get_json()["data_integer"],
        "periodo":request.get_json()["periodo"]
    }

    collection.insert_one(oggetto_da_inserire)

    print("\nSTAMPA DOPO INSERIMENTO ")
    for documents in collection.find():
        print(documents)

    return {"esito": "ALL SMOOTH"}

@app.post("/")
def operazione_update():
    print("Ricevuta richiesta di UPDATE nel DB. Procedo")


    new_periodo=request.get_json()["periodo"]
    tipo_per_modifica=request.get_json()["tipo"]
    data_per_modifica=request.get_json()["data"]

    """for dati in collection.find({"data":data_per_modifica, "tipo":tipo_per_modifica}):
        collection.update_one"""
    #meglio con update many

    collection.update_many(

    {"data": data_per_modifica, "tipo": tipo_per_modifica},     #primo campo filtro
    {"$set": {"periodo": new_periodo}}                          #setti i nuovi valori
                          
    )

    print("\nSTAMPA DOPO MODIFICA ")
    for documents in collection.find():
        print(documents)

    return {"esito":"Aggiornamento effettuato"}


@app.delete("/")
def elimina():

    print("Ricevuta richiesta di DELETE nel DB. Procedo")

    id_paziente=request.get_json()["id_paziente"]
    data_elimina_visita=request.get_json()["data"]

    collection.delete_many({"identificativo":id_paziente, "data":data_elimina_visita})

    print("Visita eliminata dal DB")

    print("\nSTAMPA DOPO ELIMINAZIONE ")
    for documents in collection.find():
        print(documents)

    return {"esito":"Visita eliminata"}






if __name__=="__main__":

    print("\n--- Avvio server Flask ---")
    print("Pulizia collection esistente...")        #in modo di avere il db da capo ogni volta che eseguo il programma e vedere il test fatto
    collection.delete_many({})  # reset

    app.run()   #ip e port di default

    print("\nTesto consistenza database in seguito agli inserimenti\n")

    for documents in collection.find():
        print(documents)
    