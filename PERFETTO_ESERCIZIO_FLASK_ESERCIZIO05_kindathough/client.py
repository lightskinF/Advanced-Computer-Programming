import requests, flask
from random import choice



local_versione_notes={}


def genera_post(text, tag):

    nota={"text":text, "tag":tag}

    
    print("mando richiesta post con text: ",text ," e tag: ", tag )


    risposta=requests.post("http://127.0.0.1:5000/note", json=nota)     #guarda get cosa restituisce; un oggetto json che in realta è un dictionary alla fine

    answer_id_generated=risposta.json()["id"]
    esito=risposta.json()["result"]

    print("Esito richiesta post: ", esito)
    local_versione_notes[answer_id_generated]=nota

    return "procedure, che devo returnare, il nulla più assoluto. cosi o mi da none"



def get_nota(idpassato):
    risposta = requests.get(f"http://127.0.0.1:5000/note/{idpassato}")
    try:
        respjson = risposta.json()
    except Exception as e:
        print("Errore nel decodificare la risposta JSON:", e)
        print("Testo risposta server:", risposta.text)
        return None

    local_versione_notes[idpassato] = respjson
    return respjson


def get_note():

    response=requests.get(url="http://127.0.0.1:5000/notes")        # server returna lista di dictionary: ({"id":keys, "values":values})
    answer=response.json()

    try:
        for variabili in response.json():   #STO ITERANDO SULLE CHIAVI
            local_versione_notes[variabili["id"]]=variabili["values"]   #le aggiungi local
        return answer
    except TypeError:  # Se answer non è iterabile, è un dictTypeError:
        if "valori" in answer:            #gestire il caso in cui non c'è nessun valore, quando restituisci "valori":"inesistenti"
            return answer   #non devi modificare niente localmente, returni solo la stringa del server, serve per l'ultimo caso di delete_all nel main()


def put(id_nota):

    new_text="hello thereeeee, i've changed!"
    new_tag="cambiento_happened"
    response=requests.put(f"http://127.0.0.1:5000/note/{id_nota}", json={"text":new_text, "tag":new_tag})

    #aggiorno anche versione locale
    local_versione_notes[id_nota]={"text":new_text, "tag":new_tag}

    return response.json()

def delete_note(note_id):

    response=requests.delete(f"http://127.0.0.1:5000/note/{note_id}")
    return response.json()

def delete_all():

    response=requests.delete(url="http://127.0.0.1:5000/notes")
    risposta_jsonata=response.json()
    local_versione_notes.clear()            #elimina tutto
    return risposta_jsonata



if __name__=="__main__":

    for i in range (2):
        testo=choice(("ciao", "hello", "bonjour", "forza milan"))
        tag=choice(("ok", "not_ok"))

        genera_post(testo, tag)

    print("proviamo funzione get\n")
    id_nota=input("inserisci ID nota da cercare: ")
    risposta=get_nota(id_nota)
    print("aggiornato valore locale con ", risposta)

    print("\n")

    print("proviamo funzione get_notes\n")
    risp=get_note()
    print("valori ottentuti dalla chiamata: ", risp)

    print("\n")
    print("provo la funzione put")
    nota_id=input("inserisci ID della nota che vuoi cambiare: ")
    answer=put(nota_id)
    print("RICEVUTO DAL SERVER :", answer)
    print("vediamo con getnotes() se effettivamente è stata modificata la nota: \n")
    print(get_note())
    print("Versione locale: ")
    for keys, values in local_versione_notes.items():
        print("key", keys)
        print("values", values)


    print("\n")
    print("provo la funzione delete")
    NOTAID=input("inserisci ID della nota che vuoi ELIMINARE: ")
    answer2=delete_note(NOTAID)
    print("RICEVUTO DAL SERVER :", answer2)
    print("vediamo con getnotes() se effettivamente è stata eliminata la nota: \n")
    print(get_note())

    print("\n")
    print("provo la funzione delete_all()")
    answer3=delete_all()
    print("RICEVUTO DAL SERVER :", answer3)
    print("vediamo con getnotes() se effettivamente è stata eliminato tutto: \n")
    print(get_note())
    print("Versione locale: ")
    for keys, values in local_versione_notes.items():
        print("key", keys)
        print("values", values)








