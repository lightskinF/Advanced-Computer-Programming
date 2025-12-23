import stomp 
from threading import Thread
from random import choice, randint
from datetime import date
from time import sleep

class MyListener(stomp.ConnectionListener):
    def on_message(self, frame):
        pass

def create (identificativo, paziente, email, tipo, data_integer, periodo, stomp):

    print("Generata richiesta CREATE: ", identificativo, ", ", paziente, ", ", email, ", ", tipo, ", ", data_integer, ", ", periodo)
    print("La mando all'Exam Manager")
    richiesta="CREATE"  #discrimino cosi lato server!!!!!!!

    stomp.send("/topic/request", body=richiesta, headers={"identificativo":identificativo, "paziente":paziente, "email":email, "tipo":tipo, "data_integer":data_integer, "periodo":periodo})
    #la risposta, cosi per update e delete, la ricevi sulla STOMP /topic/response

def update(new_periodo, tipo_per_modifica, data_per_modifica, stompp):
    print("Generata richiesta UPDATE: ",new_periodo, ", ", tipo_per_modifica, ", ", data_per_modifica)
    print("La mando all'Exam Manager")
    richiesta="UPDATE"

    stompp.send("/topic/request", body=richiesta, headers={"new_periodo":new_periodo, "tipo_per_modifica":tipo_per_modifica, "data_per_modifica":data_per_modifica}) 

def delete(id_paziente_elimina_visita, data_elimina_visita, stomppp):

    print("Generata richiesta DELETE: ", id_paziente_elimina_visita, ", ", data_elimina_visita)
    print("La mando all'Exam Manager")
    richiesta="DELETE"
    
    stomppp.send("/topic/request", body=richiesta, headers={"id_paziente_elimina_visita":id_paziente_elimina_visita, "data_elimina_visita":data_elimina_visita})



if __name__=="__main__":

    threads_joinati=[]

    STOMP=stomp.Connection([('127.0.0.1', 61613)])
    STOMP.set_listener(" ", MyListener())
    STOMP.connect(wait=True)
    STOMP.subscribe("/topic/response", id=1, ack="auto")

    for i in range(7):

        if i<4: #CREATE

            identificativo=randint(2000, 9000)
            paziente=choice(["giovanni lindo ferretti", "carmelo bene", "linus torvalds", "max pezzali"])
            email=choice(["utente@libero.it", "user@gmail.com"])
            tipo=choice(["prima", "controllo", "bilanci"])
            
            anno=randint(2010, 2025)
            mese=randint(1,12)
            giorno=randint(1,28)        #serve per la data

            data=date(anno, mese, giorno)
            data_integer=int(data.strftime("%Y%m%d"))   #formattazione, chiede un intero la traccia

            periodo=choice(["mattina", "pomeriggio"])


            t=Thread(target=create, args=(identificativo, paziente, email, tipo, data_integer, periodo, STOMP))
            threads_joinati.append(t)
            t.start()
            sleep(2.0)

        elif i==4 or i==5:
            #PRATICAMENTE TESTO SULL' i=3, cioe l'ultimo create
            new_periodo=choice(["mattina", "pomeriggio"])   #posso anche qui, in base all'if dell'indice 3, mettere l'altro. ma lasciamo cosi per ora
            tipo_per_modifica=tipo                          #uguale al quarto if (partendo da 0, il numero 3). cosi poso testare la funzione.
            data_per_modifica=data_integer                  #stesso discorso, data della quarta creazione, i=3

            t=Thread(target=update, args=(new_periodo, tipo_per_modifica, data_per_modifica, STOMP))
            threads_joinati.append(t)
            t.start()
            sleep(2.0)
        
        else: #ultima
            id_paziente_elimina_visita=identificativo #del quarto, stesso discors di prima
            data_elimina_visita=data_integer            #quarto
            t=Thread(target=delete, args=(id_paziente_elimina_visita, data_elimina_visita, STOMP))
            threads_joinati.append(t)
            t.start()
            sleep(2.0)

    
    try:
        print("Termino programma\n")
        sleep(2.0)
    except KeyboardInterrupt:
        print("\ninterrotto volontariamente\n")
    finally:
        STOMP.disconnect()
        print("connessione stomp disconnessa")
            
            
