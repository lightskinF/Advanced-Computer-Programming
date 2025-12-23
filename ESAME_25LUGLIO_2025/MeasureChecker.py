import stomp
from ListenerMeasureChecker import Listener
from time import sleep





if __name__=="__main__":


    c = stomp.Connection([('127.0.0.1', 61613)])
    c.set_listener("", Listener())
    c.connect(wait=True)

    c.subscribe("/topic/temp", id=1, ack="auto")
    c.subscribe("/topic/press", id=2, ack="auto")
    c.subscribe("/topic/humid", id=3, ack="auto")           #id sottoscrizione diversi

    print("[MEASURECHECKER] ATTIVO...")

    try:
        sleep(90.0)
    finally:
        c.disconnect()
        

