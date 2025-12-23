from abc import ABC, abstractmethod
from ICollector import ICollector
import socket
from MultiProcessSkeleton import Process_Skeleton


class Skeleton(ICollector, ABC):    #importante anche abc, perchè lo skeleton deve essere una classe astratta, nel caso di proxy skeleton x ereditarieta

    @abstractmethod
    def measure(self, stringa_comando, float_comando):
        pass                                                #lo implementaera appunto serverImpl


    def runskeleton(self):

        server=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind(("localhost", 0))   #con 0 lo imposta il SO
        porta_server=server.getsockname()[1]
        print("Server in ascolto su porta "+str(porta_server))
        server.listen(20)

        while True:
            conn, addr=server.accept()          #conn sarà socket per mandare e ricevere i messaggi dalla ricjiesta singola del client

            p=Process_Skeleton(conn, self)
            p.start()

        server.close()      #LE PARENTESI, ATTENTO...




            

