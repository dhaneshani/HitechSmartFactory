import datetime
from influxdb import InfluxDBClient
import MySQLdb
import random
import time

#Setup some constants with InfluxDB Host and Database name
INFLUXDB_HOST = 'localhost'
INFLUXDB_NAME = 'Hitech'

#Setup some constants with mysql Host and Database name
conn =MySQLdb.connect(host='localhost',user='root',passwd='',db='Hitech_Smart_Factory')

cursor = conn.cursor()
cursor.execute("SELECT * FROM Tags")
rows = cursor.fetchall()

while 1:
	for row in rows:
		#Initialize the InfluxDB Client
		client = InfluxDBClient(INFLUXDB_HOST,'8086','','',INFLUXDB_NAME)
		station_name = "S4"
		timestamp = datetime.datetime.utcnow().isoformat()
		sen_data = random.uniform(30, 32)
		tag=row[1]
		json_data = [
		   {
		       "measurement":tag,
		"time":timestamp,
		"tags": {
		   "Station":station_name
		},
		"fields": {
		   "value":sen_data
		}
		   }
		]
		bResult = client.write_points(json_data)
		print("Result of Write Data : ",bResult)
		time.sleep(5)
