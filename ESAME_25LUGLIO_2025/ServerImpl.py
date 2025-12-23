from CollectorSkeleton import Skeleton
from multiprocessing import Queue           #NEL SERVERIMPL ISTANZI LA CODA PROCESS-SAFE, nel costruttore


class ServerImpl(Skeleton):    #per ereditarieta

    def __init__(self, CONNESSIONE_STOMP):
        
        super().__init__()  # Chiamare il costruttore della classe padre, meglio metterlo
        self.coda=Queue(5)  #inizializzi coda, non la passi al costruttore, la crei direttamente tu qui. metto 5 e non 6 cosi puo' partire lo svuotamento, senno non parte mai..
        self.STOMP_CONNECTION=CONNESSIONE_STOMP


    def measure(self, stringa_comando, float_comando):

        stringa_inserisci_coda=stringa_comando+"-"+str(float_comando)
        print("Inserisco nella coda il messaggio unito: "+stringa_inserisci_coda)

        self.coda.put(stringa_inserisci_coda)   #produzione implicita con metodo built in put
        print("Stringa inserita correttamente")


        if (self.coda.full()):                          #dovrei creare un nuovo processo ma provo stesso da qui
            print("CODA PIENA, PARTE LO SVUOTAMENTO\n")

            while ( not self.coda.empty()):
                prelievo=self.coda.get()
                print("Prelevato dalla coda comando: "+prelievo)
                print("Lo mando al MeasureChecker!")
                
                #QUI DEVI INVIARE AL TOPIC STOMP!
                tipo_comando=prelievo.split("-")[0]

                if tipo_comando=="temperatura":
                    self.STOMP_CONNECTION.send("/topic/temp", prelievo)

                elif tipo_comando=="pressione":
                    self.STOMP_CONNECTION.send("/topic/press", prelievo)

                elif tipo_comando=="umidita'":
                    self.STOMP_CONNECTION.send("/topic/humid", prelievo)
                
                else:
                    print("ERRORE")
                



        