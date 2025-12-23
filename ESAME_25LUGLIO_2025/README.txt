AVVIARE PRIMA MEASURECHECKER.PY, POI COLLECTORSERVER.PY, E POI DA TERMINALE "python Sensor.py NUMERO_PORTA DEL SERVER COME ARGV[1]". Ovviamente splitta su 3 terminali diversi.



NON L'HO FATTA ALLA LETTERA L'ULTIMA PARTE DEL MEASURE CHECKER CON THREAD DIVERSI E DURABLE SUBSCRIPTION, ma più "normale".
Il funzionamento c'è.



MODIFICA: LA CODA L'HO impostato a 5 e non a 6, senno non la svuota mai (potevo fare oppure 6 thread); guarda in ServerImpl, lì la istanzi e inizializzi. In questo modo, riesco a 
vedere i messaggi stomp che vengono mandati, appunto, solo in caso di svuotamento totale-> cio' è possibile quindi quando queue.isfull()!



Piccoli errorucci, tipo non mettevo le () dopo oggetto.funzione(); quindi fai un file alla volta e debugga subito, interessante lo stacktrace, di cui, l'ultimo messaggio
è proprio da dove risale l'errore che devi anadre a modificare,

Classico problema: tempo;


si potrebbe renderlo piu interessante introducendo messaggi di risposta all'indietro ma non è stato richiesto.


riprovalo magari con durable subscription.

BENE sul miglioramento di stronzatine: tipo->funzione classe (self), altrimenti non ci vuole. sulla sintassi piccolo miglioramento direi quindi

nella classe processo, dove fai la run, importante mettere nel costruttore super().__init__, cioe richiamera costruttore classe padre.


attento a differenza concatenazione tra python e java->
PYTHON: La CONCATENAZIONE è sempre tra stringhe e ottieni la stringa concatenata. quindi nell'evenienza fai il type casting con str(intero o float)
JAVA: puoi fare stringa+intero o float. Funziona perché, quando uno degli operandi di + è una String, Java converte automaticamente l’altro in stringa (chiamando in pratica String.valueOf(x)).