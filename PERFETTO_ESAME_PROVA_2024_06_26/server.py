from flask import Flask, request, app


app=Flask(__name__)

@app.post("/update_history")
def service():
    operazione=request.get_json()["operation"]
    id_prodotto=request.get_json()["serial_number"]
    with open("history.txt", "a") as file1:
        file1.write(operazione+" + "+ id_prodotto)
    
    print("mostro il file intero dopo la richiesta: ")
    with open("history.txt", "r") as fileuno:
        righe = fileuno.readlines()
        print("".join(righe))

    return {"esito": "file_properly_changed"}



if __name__=="__main__":
    print("Aspetto richieste all'URL di default")
    app.run()            #di default è a 127.0.0.1:5000



