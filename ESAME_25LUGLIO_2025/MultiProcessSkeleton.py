from multiprocessing import Process


class Process_Skeleton(Process):

    def __init__(self, singola_connessione, istanza_interfaccia_upcall_serverimpl):

        super().__init__()                          #fondamentale
        self.singola_socket=singola_connessione
        self.istanza_upcal=istanza_interfaccia_upcall_serverimpl


    def run(self):
        
        messaggio_ricevuto_dal_client=self.singola_socket.recv(1024)
        stringa_intera=messaggio_ricevuto_dal_client.decode("utf-8")

        print("Ricevuto messaggio dal client: "+stringa_intera)
        #ora devo invocare measure di serverimpl, che si occupera di mandare il messaggio sulla coda stomp
        #siccome accetta i 2 parametri divisi dellA STRINGA_UNITA, faccio lo split e posso passarli per la chiamata measure di serverimpl

        tipo_comando=stringa_intera.split('-')[0]
        valore_comando=float( stringa_intera.split('-')[1] )

        #UPCALL
        self.istanza_upcal.measure(tipo_comando, valore_comando)

        self.singola_socket.close()     #vedi in Proxy.py; forse la singola socket conviene chiuderla qui solo, tanto non devo manader un altro messaggio dietro!

