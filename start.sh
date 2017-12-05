sudo docker run -d -p 8083:8083 -p 8086:8086   -e PRE_CREATE_DB="wadus"   --expose 8090 --expose 8099   --name influxdb   tutum/influxdb
sudo docker run -d -p 3000:3000 --link influxdb:influxdb --name=grafana -e "GF_INSTALL_PLUGINS=briangann-gauge-panel" grafana/grafana
docker run -d -p 2181:2181 -p 9092:9092     --env ADVERTISED_HOST=localhost     --env ADVERTISED_PORT=9092 spotify/kafka
sudo docker run -d -p 3000:3000 --link influxdb:influxdb --name grafana grafana/grafana
