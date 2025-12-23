import flask
from threading import Thread
import requests
from random import choice, randint
from time import sleep



def runfunction(id, data_type):
    print(f"Invio al controller: ID sensore: {id}, data_type: {data_type}. Registrami nel DB NOSQL, grazie")
    rispostacontroller=requests.post("http://127.0.0.1:5000/sensor", json={"id": id, "data_type":data_type})
    print("Esito richiesta: ", rispostacontroller.json()["esito"])


    data=randint(1,50)
    print(f"Misurazione effettuata, valore:{data}; inoltro al controller")
    risposta2controller=requests.post(f"http://127.0.0.1:5000/data/{data_type}", json={"sensor_id":id, "data":data})
    print("Esito ricevuto per inserimento misurazione: ", risposta2controller.json()["esito"])


joinati=[]

if __name__=="__main__":

    for i in range (5):
        id=i
        data_type=choice(["temp", "press"])
        t=Thread(target=runfunction, args=(id, data_type))
        joinati.append(t)
        t.start()
        sleep(2.0)
        

    for i in joinati:
        i.join()
        print("thread seppur deamon termianto ora correctly")
