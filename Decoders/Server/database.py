import sqlite3
import os

print(os.getcwd())

con = sqlite3.connect("data.db", check_same_thread = False)
cur = con.cursor()

tables = [
    "users"
    ]


def execute(query, mode="r"):
    if mode == "w":
        try:
            cur.execute(query)
            con.commit()
        except Exception as e:
            print(e)

    else:
        try:
            cur.execute(query)
            return cur.fetchall()
        except Exception as e:
            print(e)


def clear_all_data():
    for table in tables:
        execute(f"DELETE FROM {table}")


def onCreate():
    execute("create table users(userId INTEGER PRIMARY KEY AUTOINCREMENT, fcmToken text, aadharNumber INTEGER)", "w")
    execute("create table alerts(alertId INTEGER PRIMARY KEY AUTOINCREMENT, aadharNumber INTEGER, latLang text)", "w")
    execute("create table location(fcmToken INTEGER, aadharNumber INTEGER, lat TEXT, lang TEXT)", "w")
    


try:
    cur.execute("SELECT * FROM users")[0][0]
    clear_all_data()
except Exception as e:
    onCreate()
    print("Database created")
    con.commit()