from ICollector import ICollector
import socket

class Proxy (ICollector):               #classe concreta che implementa i metodi

    def __init__(self, portaserver_in_ascolto):

        self.PORTA_SERVER=portaserver_in_ascolto


    #override dell'interfaccia
    def measure(self, stringa_comando, float_comando):

        s=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect(("localhost", self.PORTA_SERVER))                          #meto il server in acsolto a 8020

        messaggio=stringa_comando+"-"+str(float_comando)     #faro lo split su '-'      #LA CONCATENAZIONE DEV ESSERE SEMPRE FRA STRINGHE!!!!! TYPECAST STR
        print("Invio comando al server "+messaggio)
        s.send(messaggio.encode("utf-8"))   #marshalling

        #NON NECESSARIO, NON CI VUOLE RISPOSTA
        #risposta=s.recv()
        #print("Ricevuta risposta dal server: "+risposta.decode("utf-8"))           #forse meglio lasciarla se la socket di connessione si chiude subito

        #s.close     #dubbio, vedi in multiprocessskeleton alla fine di run()

        

