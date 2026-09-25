#!/bin/bash

echo "Cekam Kafka da bide spremna..."
sleep 5

echo "Kreiram topic: sensor-events"
docker exec kafka kafka-topics --create \
  --topic sensor-events \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1 \
  --if-not-exists

echo "Kreiram topic: alerts"
docker exec kafka kafka-topics --create \
  --topic alerts \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1 \
  --if-not-exists

echo ""
echo "Lista na topics:"
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092
