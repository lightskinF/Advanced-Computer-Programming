import stomp
import requests
from time import sleep


class MyListener(stomp.ConnectionListener):

    def __init__(self, stomp_connection):
        self.connessione_stomp=stomp_connection

    def on_message(self, frame):
        print("Ricevuta richiesta di tipo: ", frame.body)

        if frame.body=="CREATE":

            id=frame.headers["identificativo"]
            paziente=frame.headers["paziente"]
            email=frame.headers["email"]
            tipo=frame.headers["tipo"]
            data_integer=frame.headers["data_integer"]
            periodo=frame.headers["periodo"]

            risposta=requests.put("http://127.0.0.1:5000", json={"identificativo":id, "paziente":paziente, "email":email, "tipo":tipo, "data_integer":data_integer, "periodo":periodo})
            answer=risposta.json()["esito"]
            print("ricevuta risposta ", answer)
            self.connessione_stomp.send("/topic/response", body=answer)

        elif frame.body=="UPDATE":

            new_periodo=frame.headers["new_periodo"]
            tipo_per_modifica=frame.headers["tipo_per_modifica"]
            data_per_modifica=frame.headers["data_per_modifica"]

            risposta=requests.post("http://127.0.0.1:5000", json={"periodo":new_periodo, "tipo":tipo_per_modifica, "data":data_per_modifica})
            answer=risposta.json()["esito"]
            print("ricevuta risposta ", answer)
            self.connessione_stomp.send("/topic/response", body=answer)


        elif frame.body=="DELETE":

            #headers={"id_pazienete_elimina_visita":id_pazienete_elimina_visita, "data_elimina_visita":data_elimina_visita})
            id_paziente_elimina_visita=frame.headers["id_paziente_elimina_visita"]
            data_elimina_visita=frame.headers["data_elimina_visita"]

            risposta=requests.delete("http://127.0.0.1:5000", json={"id_paziente":id_paziente_elimina_visita, "data":data_elimina_visita})
            answer=risposta.json()["esito"]
            print("ricevuta risposta ", answer)
            self.connessione_stomp.send("/topic/response", body=answer)

        else:
            print("\nerrore tipo richiesta \n")
            return "errore"


if __name__=="__main__":

    STOMP=stomp.Connection([('127.0.0.1', 61613)])
    STOMP.set_listener(" ", MyListener(STOMP))
    STOMP.connect(wait=True)
    STOMP.subscribe("/topic/request", id=1, ack="auto")
    try:
        print("aspetto richieste")
        sleep(20)
    except KeyboardInterrupt:
        print("\ninterrotto volontariamente\n")
    finally:
        STOMP.disconnect()