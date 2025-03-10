import datetime as dt
import json
import logging
import os
import random
import socket
import sqlite3
import sys
import time
import uuid
from datetime import timezone
import subprocess
import struct
import re
import traceback

import asyncio
import bleak as bl
import requests as rest
import yaml
from requests.auth import HTTPBasicAuth as httpaut

# classes for measurements

# value with timestamp


class Measurement(object):
    def __init__(self, val: float, measurement_timestamp: str):
        self.val = val
        self.measurementTimestamp = measurement_timestamp

    def __repr__(self):
        return str(self.__dict__)

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False)

# includes a Measurement and an additional sensor-id


class Sensor_measurement(object):
    def __init__(self, sensorId: int, stationId: int, val: float, measurement_timestamp: dt.datetime):
        self.sensorId = sensorId
        self.stationId = stationId
        self.measurement = Measurement(val, measurement_timestamp)

    def __repr__(self):
        return str(self.__dict__)

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False)


class Server(object):
    def __init__(self, url: str, sending_url: str, limits_url: str, user_name: str, user_pw: str, interval: int,
                 new_accesspoint_url: str, new_sensorstation_url: str, station_exits: str, send_bt_discover: str,
                 new_sensor_url: str, make_bt_connection: str, connection_success: str, should_connect: str) -> None:
        self.url = url
        self.sending_url = sending_url
        self.limits_url = limits_url
        self.new_accesspoint_url = new_accesspoint_url
        self.new_sensorstation_url = new_sensorstation_url
        self.new_sensor_url = new_sensor_url
        self.station_exits = station_exits
        self.send_bt_discover = send_bt_discover
        self.make_bt_connection = make_bt_connection
        self.should_connect = should_connect
        self.connection_success = connection_success
        self.user_name = user_name
        self.user_pw = user_pw
        self.interval = interval

    def __repr__(self):
        return str(self.__dict__)


class SensorStation(object):
    def __init__(self, interval: int, name: str = None, stationId: int = None) -> None:
        self.name = name
        self.stationId = stationId
        self.interval = interval

    def __repr__(self) -> str:
        return str(self.__dict__)

# definition of functions


def put_uuids_in_db(con: sqlite3.Connection):
    logging.info("putting bt-uuids in db")
    cur = con.cursor()

    types = ["TEMPERATURE", "HUMIDITY", "PRESSURE",
             "AIR_QUALITY", "ALTITUDE", "LIGHT", "HYGROMETER"]

    result = cur.execute("select count(*) from bt_uuids")
    counter = result.fetchone()[0]

    # when there are uuids in table -- just return
    if counter != 0:
        logging.info("bt-uuids are allready in db")
        return

    logging.info("reading bt-uuids from config.yaml")
    config_file = open("conf.yaml", "r")

    data = yaml.safe_load(config_file)

    for t in types:
        sensor = (str(data["sensorStation"][f"{t}"]["uuid"]), f"{t}")
        cur.execute("insert into bt_uuids values (?, ?)", sensor)
        con.commit()

    config_file.close()


# inits db with all necessary tables when they are not there


def init_db():
    logging.info("inti database")

    con = sqlite3.connect("accesspoint.db")

    cur = con.cursor()

    cur.execute(
        """
                       CREATE TABLE IF NOT EXISTS ownId(
                               id INTEGER PRIMARY KEY
                       )
               """
    )

    cur.execute(
        """
                       CREATE TABLE IF NOT EXISTS stations(
                               id INTEGER PRIMARY KEY,
                               name VARCHAR(50),
                               address VARCHAR(150),
                               interval REAL
                       )
               """
    )

    cur.execute(
        """
                       CREATE TABLE IF NOT EXISTS sensors(
                               id INTEGER PRIMARY KEY,
                               station_fk INTEGER,
                               name_fk VARCHAR(50),
                               upper REAL,
                               lower REAL,
                               FOREIGN KEY(station_fk) REFERENCES stations(id),
                               FOREIGN KEY(name_fk) REFERENCES bt_uuids(name)

                       )
               """
    )

    cur.execute(
        """
                        CREATE TABLE IF NOT EXISTS measurements(
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                station_fk INTEGER,
                                sensor_fk INTEGER,
                                value REAL,
                                dateTime DATETIME,
                                FOREIGN KEY(station_fk) REFERENCES stations(id),
                                FOREIGN KEY(sensor_fk) REFERENCES sensors(id)
                                )
                """
    )

    cur.execute(
        """
                        CREATE TABLE IF NOT EXISTS bt_uuids(
                                uuid varchar(50),
                                name varchar(50) PRIMARY KEY
                                )
                """
    )

    put_uuids_in_db(con)

    return con

# creates default logger in DEBUG mode


def init_logger():
    logging.basicConfig(
        filename="log.log",
        encoding="utf-8",
        level=logging.INFO,
        format="%(asctime)s %(levelname)s\t\[%(filename)s:%(funcName)s():%(lineno)s]: %(message)s",
    )

# searches for sensorstation and connects with it


async def sending_bt_devices_to_server(server: Server, accesspointId: int):
    # connects with sensorStation
    logging.info("init bt connection to sensorstation")

    devices = await bl.BleakScanner.discover()
    (front, back) = server.send_bt_discover.split("?")

    url = f"{server.url}{front}{accesspointId}{back}"

    sending = []

    for d in devices:
        string = f"{d.name}"
        # pattern = r'^[a-zA-Z]{2}-[a-zA-Z]{2}-[a-zA-Z]{2}-[a-zA-Z]{2}-[a-zA-Z]{2}-[a-zA-Z]{2}$'
        if string.find("-") < 2:
            sending.append(string)

    headers = {'Content-Type': 'application/json'}

    code = rest.post(url, headers=headers, json=sending, auth=httpaut(
        server.user_name, server.user_pw))

    return devices

# reading from sensorstation a specific value
# returns by default the read value as float -- every stream sends converted floats


async def bt_read(client: bl.BleakClient, reading_uuid: str, return_float=True):
    # functioning reading
    reading = await client.read_gatt_char(reading_uuid)

    if return_float:
        value = struct.unpack('f', reading)[0]
    else:
        value = int.from_bytes(reading, "little")

    return value

# reading each sensor-bt-stream from station and storing values in db


async def read_station_values_store_db(stationId: int, client: bl.BleakClient, con: sqlite3.Connection):
    logging.info("reading bt values and putting into db")
    cur = con.cursor()

    uuids = cur.execute("select * from bt_uuids").fetchall()
    sensor_names_from_station = cur.execute(
        f"select id, name_fk from sensors where station_fk = {stationId}").fetchall()

    uuids_to_read = []
    sensor_ids = []

    for u in uuids:
        for n in sensor_names_from_station:
            if u[1] == n[1]:
                uuids_to_read.append(u[0])
                sensor_ids.append(n[0])
                break

    to_read = zip(sensor_ids, uuids_to_read)

    for r in to_read:
        value = await bt_read(client=client, reading_uuid=r[1])

        # just for testing
        # value = random.random()*100

        insert = (None, stationId, r[0], value, dt.datetime.utcnow())

        cur.execute(
            "insert into measurements values (?, ?, ?, ?, ?)", insert)

        con.commit()


# writes to a specific bt-stream -- used for setting error flags and setting blinking led
async def bt_write(client: bl.BleakClient, writing_uuid: str, value):
    # value = b'\x01'  # 0 is putting flag down
    # await sensorStation.client.write_gatt_char("0000fff3-0000-1000-8000-00805f9b34fb", value, response=True)
    # value = struct.pack('<i', 2000)
    # await sensorStation.client.write_gatt_char("0000fff4-0000-1000-8000-00805f9b34fb", value, response=True)
    await client.write_gatt_char(writing_uuid, value, response=False)

# reads config yaml file
# returns (Server, Sensorstation) with the read stats


def read_config():
    logging.info("reading config.yaml")
    config_file = open("conf.yaml", "r")

    data = yaml.safe_load(config_file)

    server = Server(url=str(data["server"]["url"]), sending_url=str(data["server"]["sending_url"]), limits_url=str(data["server"]["limits_url"]), user_name=str(
        data["server"]["user_name"]), user_pw=str(data["server"]["user_pw"]), interval=int(data["server"]["interval"]),
        new_accesspoint_url=str(data["server"]["new_accesspoint_url"]), new_sensorstation_url=str(data["server"]["new_sensorstation_url"]), station_exits=str(
        data["server"]["station_exits"]), send_bt_discover=str(data["server"]["send_bt_discover"]), new_sensor_url=str(data["server"]["new_sensor_url"]),
        make_bt_connection=str(data["server"]["make_bt_connection"]), connection_success=str(data["server"]["connection_success"]),
        should_connect=str(data["server"]["should_connect"]))

    sensor_station = SensorStation(
        interval=int(data["sensorStation"]["interval"]))

    config_file.close()

    return (server, sensor_station)


# Gets all SensorMeasurements from DB and returns a stream of tuples


def get_all_measurements_from_db(con: sqlite3.Connection):
    cur = con.cursor()
    result = cur.execute("select * from measurements order by dateTime")
    return result

# looking if measurement is in db -- returns bool


def measurement_in_db(
    sensor_measurement: Sensor_measurement, con: sqlite3.Connection
) -> bool:
    cur = con.cursor()
    result = cur.execute(
        f'select count(*) from measurements where dateTime = "{sensor_measurement.measurement.measurementTimestamp}"'
    )
    counter = result.fetchone()[0]

    if counter == 0:
        return False
    return True

# deleting single measurement in db


def delete_measurement_in_db(
    sensor_measurement: Sensor_measurement, con: sqlite3.Connection
):
    cur = con.cursor()
    sensor_measurement = change_timestamp_for_db(sensor_measurement)

    if not measurement_in_db(sensor_measurement, con):
        return False

    cur.execute(
        f'delete from measurements where dateTime = "{sensor_measurement.measurement.measurementTimestamp}"'
    )
    con.commit()

    if measurement_in_db(sensor_measurement, con):
        return False
    return True

# sending a specific measurement to the given server
# returns the status code from http


def send_measurement(
    sensor_measurement: Sensor_measurement, url: str, user: str, user_pw: str
):
    sensor_measurement = change_timestamp_for_server(sensor_measurement)
    logging.info("sending -- " + str(sensor_measurement) + " -- ")
    r = rest.post(
        url=url, json=eval(sensor_measurement.toJSON()), auth=httpaut(user, user_pw)
    )
    return r.status_code

# for inconsistency from python to java


def change_timestamp_for_server(sensor_measurement: Sensor_measurement):
    timestamp = sensor_measurement.measurement.measurementTimestamp
    timestamp = timestamp.replace(" ", "T")
    sensor_measurement.measurement.measurementTimestamp = timestamp

    return sensor_measurement

# for inconsistency from python to java


def change_timestamp_for_db(sensor_measurement: Sensor_measurement):
    timestamp = sensor_measurement.measurement.measurementTimestamp
    sensor_measurement.measurement.measurementTimestamp = timestamp.replace(
        "T", " ")
    return sensor_measurement

# extract and return a SensorMeasurement from a db-tuple


def extract_measurement(tuple):
    (id, station_id, sensor_id, val, timestamp) = tuple
    return Sensor_measurement(sensorId=sensor_id, stationId=station_id, val=val, measurement_timestamp=timestamp)

# is getting all measurements from db and sending to server


def send_measurements_rest(
        db_connection: sqlite3.Connection, server: Server):
    for tuple in get_all_measurements_from_db(db_connection):
        sensor_measurement = extract_measurement(tuple)
        url = f"{server.url}{server.sending_url}"
        status_code = send_measurement(
            sensor_measurement, url, server.user_name, server.user_pw)
        if status_code == 200:
            pass
            if not delete_measurement_in_db(sensor_measurement, db_connection):
                logging.error(
                    "measurement should have been deleted but did not work")
            logging.info("measurement deleted")
        else:
            logging.error(str(status_code) + " -- measurement not deleted")

    return True

# writting a sensor warning - error to station


async def write_sensor_warning(client: bl.BleakClient, sensor, lower=True):
    logging.info(f"writing sensor={sensor[2]} warning *lower={lower}* limit")
    config_file = open("conf.yaml", "r")
    data = yaml.safe_load(config_file)

    uuid_code = data["sensorStation"]["error_code"]
    uuid_flag = data["sensorStation"]["error_flag"]

    if lower:
        error_code = data["sensorStation"][sensor[2]]["lower"]
    else:
        error_code = data["sensorStation"][sensor[2]]["upper"]

    config_file.close()

    value = struct.pack('<i', error_code)
    # sending error_code to sensorStation
    await bt_write(client=client, writing_uuid=uuid_code, value=value)
    flag = b'\x01'
    # sending to set error-flag
    await bt_write(client=client, writing_uuid=uuid_flag, value=flag)

# unsetting error-flag on station


def unset_station_error(client: bl.BleakClient):
    logging.info("unset error-flag")
    flag = b'\x00'

    config_file = open("conf.yaml", "r")
    data = yaml.safe_load(config_file)
    uuid_flag = data["sensorStation"]["error_flag"]
    config_file.close()

    # sending to unset error-flag
    bt_write(client=client, writing_uuid=uuid_flag, value=flag)

# returns accesspoint id -- creates a new one when necessary


def ownId(con: sqlite3.Connection, server: Server):
    logging.info("checking for accesspoint id")

    cur = con.cursor()
    result = cur.execute("select count(*) from ownId")
    counter = result.fetchone()[0]

    # when there is an Id -- just return
    if counter != 0:
        logging.info("accesspoint has own id")
        result = cur.execute("select * from ownId")
        id = result.fetchone()[0]
        return id

    logging.info("accesspoint has no id")
    # otherwise get id from server

    # TODO this is just for testing

    status = 0
    while status != 200:
        try:
            res = rest.post(f"{server.url}{server.new_accesspoint_url}",
                            auth=httpaut(server.user_name, server.user_pw))

            status = res.status_code
        except:
            logging.error("server not reachable")
            time.sleep(2)

    id = int(res.text)

    logging.info(f"accesspoint got id: {id}")

    # storing id in db
    cur.execute(f'insert into ownId values ({id})')
    con.commit()

    return id

# init sensorstation -- if allready valid id just storing in object -- otherwise asking for id


def init_sensorStation(con: sqlite3.Connection, accesspointId: int, sensorStation: SensorStation, server: Server):
    logging.info("init sensorstation")

    cur = con.cursor()
    res = cur.execute("SELECT id FROM stations").fetchone()

    if res != None:
        stationid = res[0]
        (front, back) = server.station_exits.split("?")
        url = f"{server.url}{front}{stationid}{back}"
        res = rest.get(url, auth=httpaut(server.user_name, server.user_pw))

        if res.text:
            sensorStation.stationId = stationid
            return
        else:
            cur.execute(f"DELETE FROM stations WHERE id = {stationid}")
            cur.execute(f"DELETE FROM sensors WHERE station_fk = {stationid}")
            con.commit()

    (front, back) = server.new_sensorstation_url.split("?")

    url = f"{server.url}{front}{accesspointId}{back}"

    stationid = rest.post(url=url, auth=httpaut(
        server.user_name, server.user_pw)).text

    sensorStation.stationId = stationid
    cur.execute(f'insert into stations values (?, ?, ?, ?)',
                (stationid, sensorStation.name, None, sensorStation.interval))
    logging.info(f"sensorstation id: {stationid}")
    con.commit()

    types = ["TEMPERATURE", "HUMIDITY", "PRESSURE",
             "AIR_QUALITY", "ALTITUDE", "LIGHT", "HYGROMETER"]

    (front, back) = server.new_sensor_url.split("?")
    url = f"{server.url}{front}{stationid}{back}"

    for t in types:
        sensor_id = rest.post(
            url, json={"typ": f"{t}"}, auth=httpaut(server.user_name, server.user_pw)).text
        logging.info(f"sensor id from {t}: {sensor_id}")
        cur.execute(f'insert into sensors values (?, ?, ?, ?, ?)',
                    (sensor_id, stationid, f"{t}", 0.0, 0.0))
        con.commit()

# reading new limits from server and updating in db


def update_limits(con: sqlite3.Connection, server: Server, sensorStation: SensorStation):
    cur = con.cursor()

    res = cur.execute(
        f"SELECT id FROM sensors WHERE station_fk = {sensorStation.stationId}").fetchall()

    (front, back) = server.limits_url.split("?")

    for r in res:
        url = f"{server.url}{front}{r[0]}{back}"
        sensor = rest.get(url=url, auth=httpaut(
            server.user_name, server.user_pw))

        upper = sensor.json()["upperLimit"]
        lower = sensor.json()["lowerLimit"]

        cur.execute(
            f"UPDATE sensors SET lower = {lower}, upper = {upper} WHERE id = {r[0]}")
        con.commit()

# checking if limits are respected


async def check_limits(con: sqlite3.Connection, stationId: int, client: bl.BleakClient, accesspointId: int):
    logging.info("checking limits")
    cur = con.cursor()

    sensor_ids = cur.execute(
        f"SELECT * FROM sensors WHERE station_fk = {stationId}").fetchall()

    for sensor_id in sensor_ids:
        value = cur.execute(
            f"SELECT value FROM measurements WHERE sensor_fk = {sensor_id[0]} ORDER BY dateTime DESC").fetchone()[0]
        limits = cur.execute(
            f"SELECT upper, lower FROM sensors WHERE id = {sensor_id[0]}").fetchone()

        if value > limits[0]:
            logging.warning(
                f"value {value} of sensor {sensor_id[0]} is to over {limits[0]}")
            await write_sensor_warning(client=client, sensor=sensor_id, lower=False)
        elif value < limits[1]:
            logging.warning(
                f"value {value} of sensor {sensor_id[0]} is to under {limits[1]}")
            await write_sensor_warning(client=client, sensor=sensor_id, lower=True)


def making_bt_con(server: Server, accesspointId: int):

    (front, back) = server.make_bt_connection.split("?")

    url = f"{server.url}{front}{accesspointId}{back}"

    res = rest.get(url=url, auth=httpaut(
        server.user_name, server.user_pw)).text
    return res


async def bt_meth(server: Server, accesspointId: int, sensorStation: SensorStation):
    devices = []
    name = ""
    while (name == ""):
        devices = await sending_bt_devices_to_server(server, accesspointId)
        time.sleep(2)
        (front, back) = server.should_connect.split("?")
        url = f"{server.url}{front}{accesspointId}{back}"

        name = rest.get(url=url, auth=httpaut(
            server.user_name, server.user_pw)).text

    address = None

    for d in devices:
        if d.name == name:
            sensorStation.name = name
            address = d.address
            logging.info("found sensor_station with address :%s", address)
            break

    if address is None:
        logging.error("no address found for name")
    else:
        logging.info(f"address: {address} found for name: {name}")

    client = bl.BleakClient(address)

    await client.connect()

    if (not client.is_connected):
        raise "client not connected"
    logging.info("connected to sensor_station")

    return client


async def connect_to_old_station(con: sqlite3.Connection, sensorStation: SensorStation):
    logging.info("is connecting to old station")
    cur = con.cursor()

    address = cur.execute(
        f"SELECT address FROM stations WHERE id = {sensorStation.stationId}").fetchone()[0]

    logging.info(f"address of station {sensorStation.stationId} is {address}")

    # devices = await bl.BleakScanner.discover()

    # for d in devices:
    #    logging.info(f"bt-device: {d}")

    if address == None:
        logging.error("gewaltiger Schaden")
        raise "gewaltiger Schaden"

    client = bl.BleakClient(address)

    await client.connect()

    if (not client.is_connected):
        raise "client not connected"
    logging.info("connected to sensor_station")

    return client


def is_there_old_station(con: sqlite3.Connection, accesspointId: int, server: Server, sensorStation: SensorStation):
    logging.debug("looking for old station")
    cur = con.cursor()

    res = cur.execute("SELECT id FROM stations").fetchone()

    if res != None:
        stationid = res[0]
        (front, back) = server.station_exits.split("?")
        url = f"{server.url}{front}{stationid}{back}"
        res = rest.get(url, auth=httpaut(server.user_name, server.user_pw))

        if res.text:
            logging.info("old station still exists on server")
            sensorStation.stationId = stationid
            return True
        else:
            logging.info("old station does not exist on server")
            cur.execute(f"DELETE FROM stations WHERE id = {stationid}")
            cur.execute(f"DELETE FROM sensors WHERE station_fk = {stationid}")
            con.commit()
            return False

    logging.debug("there is no old station")
    return False


def store_bt_address(con: sqlite3.Connection, client: bl.BleakClient, sensorStation: SensorStation):
    cur = con.cursor()

    cur.execute(
        f"UPDATE stations SET address = \'{client.address}\' WHERE id = {sensorStation.stationId}")
    con.commit()


async def main():
    init_logger()
    logging.info("starting")

    (server, sensorStation) = read_config()
    db_connection = init_db()

    accesspointId = ownId(con=db_connection, server=server)
    bt_client: bl.BleakClient = None
    cur = db_connection.cursor()

    logging.info("config finished")

    while True:
        try:
            if bt_client is not None:
                logging.debug("there is a bt-client connected")
            elif is_there_old_station(db_connection, accesspointId, server, sensorStation):
                bt_client = await connect_to_old_station(db_connection, sensorStation)
            elif making_bt_con(server, accesspointId) == "true":
                bt_client = await bt_meth(server, accesspointId, sensorStation)

                (front, back) = server.connection_success.split("?")
                url = f"{server.url}{front}{accesspointId}{back}"

                rest.post(url=url, auth=httpaut(
                    server.user_name, server.user_pw))
                
                init_sensorStation(con=db_connection, accesspointId=accesspointId,
                                   sensorStation=sensorStation, server=server)

                store_bt_address(con=db_connection, client=bt_client,
                                 sensorStation=sensorStation)

            res = cur.execute("SELECT * FROM stations").fetchall()

            if len(res) == 0:
                time.sleep(5)
                continue

            stationId = res[0][0]

            inner: int = int(server.interval / sensorStation.interval)

            # rest get from server -- all new set borders etc
            update_limits(con=db_connection, server=server,
                          sensorStation=sensorStation)

            for i in range(inner):
                # get new measurements from arduino via BT and store in db
                await read_station_values_store_db(
                    stationId=stationId, client=bt_client, con=db_connection)

                # testing for limits
                await check_limits(con=db_connection, stationId=stationId,
                                   client=bt_client, accesspointId=accesspointId)

                # time.sleep(sensorStation.interval)

            # send all measurements that are in db to server and delete if status 200

            send_measurements_rest(db_connection=db_connection, server=server)

        except Exception as error:
            logging.error(error)
            time.sleep(2)


if __name__ == "__main__":
    asyncio.run(main())
