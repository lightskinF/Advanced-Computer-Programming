import stomp


class Listener(stomp.ConnectionListener):

    def on_message(self, frame):
        print("Ricevuto messaggio: "+frame.body)