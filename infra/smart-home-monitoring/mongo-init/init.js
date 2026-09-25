db = db.getSiblingDB('smarthome');

db.createCollection('sensor_events');
db.createCollection('alerts');

db.alerts.createIndex({ timestamp: -1 });
db.sensor_events.createIndex({ timestamp: -1 });
db.alerts.createIndex({ severity: 1 });
db.alerts.createIndex({ roomId: 1 });

print('smarthome baza inicijalizirana: sensor_events i alerts kolekcii kreirani.');
