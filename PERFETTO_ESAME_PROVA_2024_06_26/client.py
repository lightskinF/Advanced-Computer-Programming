import grpc
import sys
from threading import Thread
from random import choice, randint
import servizi_pb2
import servizi_pb2_grpc
from time import sleep

serial_numbers=[]

def genera_numero_univoco():

    NON_gia_generato=True
    while(NON_gia_generato):
        numero=randint(1,100)
        if numero not in serial_numbers:
            serial_numbers.append(numero)
            NON_gia_generato=False
    return numero

    

def runfunction(richiesta, porta):

    if richiesta=="acquisto":
        print("Richiesta :", richiesta)
        with grpc.insecure_channel((f"localhost:{porta}")) as channel:
            stub=servizi_pb2_grpc.FunzioniStub(channel)
            risposta_productmanager=stub.Buy(servizi_pb2.Vuoto(messaggio="acquisto"))
            print("risposta ricevuta: SERIAL NUMBER PRODOOTO ACQUISTATO", risposta_productmanager.serial_number, "\n")

    else:   #deposita
        print("Richiesta :", richiesta)

        serial_number_pc=genera_numero_univoco()
        print("ID generato: ", serial_number_pc)

        with grpc.insecure_channel((f"localhost:{porta}")) as channel:
            stub=servizi_pb2_grpc.FunzioniStub(channel)
            risposta=stub.Sell(servizi_pb2.Prodotto(serial_number=serial_number_pc))      #FORSE QUA DEVO FARE TYPECASTING STRINGA PRIMA, no perchè PRODOTTO nel file protoc ha un campo che è un int!
            print("Inserimento in coda, ACK= ", risposta)





if __name__=="__main__":

    try:
        porta_server=int(sys.argv[1])
    except IndexError:
        print("inserisci porta server prima!\n")

    thread_joinati=[]
                                # oppure scegli=["acquisto", "vendita"] e poi request=choice(scegli)

    for i in range(10):
        
        if (i%2==0):
            request="vendi"         #in questo modo una volta vendi e una consumi, 5 produttori e 5 consumatori
        else:
            request="acquisto"

        t=Thread(target=runfunction, args=(request, porta_server))
        t.start()
        thread_joinati.append(t)
        sleep(1.0)

    try:
        for x in thread_joinati:
            print("thread terminato")
            x.join()
    except KeyboardInterrupt:
        print("termino i thread")
    finally:
        for j in thread_joinati:
            j.join()
