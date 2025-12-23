import stomp
from multiprocessing import Process

def inserimento(queue, message):

    print("Inserisco messaggio in coda")        #inserisci sempre un'artista
    queue.put(message) #metodo built iin



def svuota(coda):

    print("SVUOTAMENTO partito..")

    while (not coda.empty()):       #usi il metodod di Queue built in, sarebbe l'equivalente di quando fai in java nell'implementazione di coda il metodo isemprty() se Nelem==0

        artista=coda.get()      #metodo built in
        print("Prelevato artista: "+artista)

    if (coda.empty()):
        print("Coda vuota..")





class MyListener(stomp.ConnectionListener):

    def __init__(self, conn, queue):
        self.connessione=conn
        self.coda=queue

    def on_message(self, frame):
        print('[SERVER] received a message "%s"' % frame.body)

        if frame.body=="Sold":
            p=Process(target=svuota, args=(self.coda, ))         #sold sarà lo riceve sempre il topic stats. SINGLETON
            p.start()
        else:   #per gli artisti..
            p=Process(target=inserimento, args=(self.coda, frame.body ))   #singleton
            p.start()





