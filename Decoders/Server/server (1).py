#Simple flask app

from flask import Flask, request, jsonify
from database import execute

import requests
import subprocess
import json

app = Flask(__name__)

AYUSH_FCM = "eFC4y0ZAT2-Yn6oJ8KaJny:APA91bEWBcQq9rL7Fj3J-VjjtU7oyUbkif8Wov59Vq64HwPsF6Dl-QVjnyHYK-_gJ3c19_Q3VjmsgyHwIQ9ob0hq11LscIMzpIaZpGl7Mp9oofuZnJSK4Y4AyfyvbFJil1WzhKyFteC6"


ASHWIN_FCM = "eabcy93rQeyqh0_pp9ebiZ:APA91bHvdXTL4rN19atzIGWqKxJ34JcsrRdzB14U1TO8CnpquiRB-4EYyKwdBuuhtLRi_l_bOuBahwE2BTIiESV_HHo758YWzzVijTlc7JBfSsiifw9Odp-cRvJzsPLfgiVGxHaIzi2s"
ANKIT_FCM = "f7d9QSZsQQKjJJ34M2jREX:APA91bFqXMklhpvPkyf7k0BRfA4dM95Drfk-FQ61YwwZvWR5qvlRy8n0e2YN6LIsPQkWrjI5C2BkZSz4qNt7HiUK1lsqpJPWVP2gJ84dbPKYq5E9eEfZ8W7U79MFyRMhGIxi4Z1vDSZe"

AYUSH_BODY = {
#        "notification": {
#            "title": "ALERT!",
#            "body": "Kya haal hai.",
#            },
        "data": {
            "title": "show"},
        "to": str(AYUSH_FCM),
        "priority": "high",
        }

ASHWIN_BODY = {
#        "notification": {
#            "title": "ALERT!",
#            "body": "Kya haal hai.",
#            },
        "data": {
            "title": "show"},
        "to": str(ASHWIN_FCM),
        "priority": "high",
        }

ANKIT_BODY = {
#        "notification": {
#            "title": "ALERT!",
#            "body": "Kya haal hai.",
#            },
        "data": {
            "title": "show"},
        "to": str(ANKIT_FCM),
        "priority": "high",
        }


REST_FCM = [
    ASHWIN_FCM, ANKIT_FCM
]

headers = {
    "Content-Type": "application/json",
    "Authorization": "key=" + "AAAAkWBJj7o:APA91bF1n4BZm1uH7O_vt8vPJKSImelCCZ2Ut-7yquSX8XWBpEgk8bmhqqgE8RpNK8hHyS_6NREIWB3im5PfhpLUNhdCbuDTLpB1W-9HlQUSUfZeumv2Hu7t0c68RY-HFP7jh0KFjVNN",
    }
triggered = False

def play():
    if triggered:
        return
    subprocess.Popen(["ffplay", "-nodisp", "-autoexit", "siren.mpeg"])

def sendCustomAlert(body):
    print(body)
    response = requests.post("https://fcm.googleapis.com/fcm/send", headers = headers, data=json.dumps(body))
    return response

def sendAlert(fcmToken):
    print(fcmToken)
    body = {
       "notification": {
           "title": "ALERT!",
           "body": "Kya haal hai.",
           },

        "to": str(fcmToken),
        "priority": "high",
        }
    response = requests.post("https://fcm.googleapis.com/fcm/send", headers = headers, data=json.dumps(body))
    return response
    

@app.route('/earthquake/<int:lat>/<int:long>', methods=['GET'])
def api(lat, long):
    global triggered
    print("Earthquake detected at", lat, long)
    sendCustomAlert(AYUSH_BODY)
    sendCustomAlert(ASHWIN_BODY)
    sendCustomAlert(ANKIT_BODY)
    # if request.method == 'GET':
    #     sendCustomAlert(AYUSH_BODY)
    #     for i in REST_FCM:
    #         sendAlert(i)
        # play()
        # triggered = True
        # fcmToken = execute(f"SELECT fcmToken FROM users")
        # for fcm in fcmToken:
        #     sendAlert(fcm[0])
    return jsonify({'status': 'OK'})
    

@app.route('/register')
def register():
    fcm = request.args.get('fcm')
    phoneNumer = request.args.get('phoneNo')
    print(fcm, phoneNumer)

    # Check if user already exists
    userExists = execute(f"SELECT * FROM users WHERE fcmToken = '{fcm}'")
    if len(userExists) == 0:
        execute(f"INSERT INTO users (fcmToken, phoneNumber) VALUES ('{fcm}', '{phoneNumer}')")
        return jsonify({'status': 'OK'})
    
    else:
        return jsonify({'status': 'User already exists'})


@app.route('/')
def hello_world():
    # play()
    sendAlert(AYUSH_FCM)
    return 'Hello, World!'

# print(sendAlert(AYUSH_FCM).text)
    

if __name__ == '__ main__':
    app.run("0.0.0.0", 5000)
# sendCustomAlert(AYUSH_BODY)
# sendCustomAlert(ASHWIN_BODY)
# sendCustomAlert(ANKIT_BODY)
# sendAlert(ANKIT_FCM)