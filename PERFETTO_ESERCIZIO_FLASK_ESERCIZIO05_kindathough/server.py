from flask import Flask, request
import uuid



dizionario={}

app=Flask(__name__)


@app.post("/note")
def post():
    text=request.get_json()["text"]
    tag=request.get_json()["tag"]

    print("ricevuta richiesta con nota: ", text, "e tag: ", tag)

    id_univoco=uuid.uuid4().hex
    print("id generato per tale richiesta: ", id_univoco)

    dizionario[id_univoco]=request.get_json()           #il dizionario conterrà come valori dei dizionari

    return {"result":"added", "id":id_univoco}          #returno un dizionario che flask converte automaticamente in un oggetto json e lato client quindi lo tratti come tale



@app.get("/note/<id>")  #placeholder che passi alla funzione view sotto
def get_request(id):

    id_ricevuto=id      #passo un id del dizionario, devo vedere se c'è, sarebbe la chiave

    
    if id_ricevuto in dizionario:
        return dizionario[id_ricevuto]
    else:
        print("Nota con quest ID non esiste, errore!")
        messaggio_errore={"result":"note not found"}
        return messaggio_errore, 404

@app.get("/notes")
def get_all_notes():

    notes=[]
    if (len(dizionario)>0):
        for keys, values in dizionario.items():
            notes.append({"id":keys, "values":values})        #lista di dizionari
        return notes
    else:
        return {"valori":"inesistenti"}

@app.put("/note/<id>")
def put(id):

    try:                                                                        #puo essere che l'id inserito dal client non c'è nel dizionario e quindi return un errore, sempre COME DICTIONARY E POI FAI .JSON()
        body_richiesta=request.get_json()       #nota nel body e id nella url
        if body_richiesta in dizionario.items():    #questo per fare check sui values, allora items(). per farlo sulle chiavi allora: "in dizionario" solo
            dizionario[id]=body_richiesta           #INFATTI VEDI funzione ELIMINA SOTTO! controlli nelle chiavi non nei values del dictionary!!!!!!!!!!
            print("valori nota già presenti")
            message="updated"
        else:
            dizionario[id]=body_richiesta
            print("aggiornamento nota")
            message="created"

        return {"result":message, "id": id}

    except KeyError:
        print("errore id nota passata al server\n")
        return {"risultato": "chiave non esistente!"}

@app.delete("/note/<id>")
def elimina(id):

    if id in dizionario:                        #QUI NON dizionario.items()!!!!!! fai check sulle CHIAVI, NON SUI VALUES!!!!!!!!!!!!
        valore_dictionary=dizionario.pop(id)
        print("eliminato correttamente ", valore_dictionary)
        return {"result": "deleted", "id":id}
    else:
        return {"result":"not found"}, 404                          #RETURNI SEMPRE UN FOTTUTISSIMO DICTIONARY, IN OGNI CASO. DAL CLINT FAI .JSON()


@app.delete("/notes")
def elimina_tutto():

    dizionario.clear()
    return {"result":"no more notes"}



if __name__=="__main__":

    app.run()           #indirizzo di default: http://127.0.0.1:5000/





