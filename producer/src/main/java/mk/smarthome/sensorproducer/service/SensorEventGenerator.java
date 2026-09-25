package mk.smarthome.sensorproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.smarthome.sensorproducer.model.ApartmentInfo;
import mk.smarthome.sensorproducer.model.SensorEvent;
import mk.smarthome.sensorproducer.model.SensorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventGenerator {

    private final KafkaTemplate<String, SensorEvent> kafkaTemplate;
    private final Random random = new Random();

    @Value("${sensor.topic}")
    private String topic;

    @Value("${sensor.danger-probability}")
    private double dangerProbability;

    // Sobi vnatre vo eden stan
    private static final List<String> ROOMS = List.of(
            "kitchen", "living-room", "bedroom", "bathroom"
    );

    // 8 stanovi vo zgradata (apartmentId, broj na stan, kat)
    private static final List<ApartmentInfo> APARTMENTS = List.of(
            new ApartmentInfo("apt-1", "1", 1),
            new ApartmentInfo("apt-2", "2", 1),
            new ApartmentInfo("apt-3", "3", 2),
            new ApartmentInfo("apt-4", "4", 2),
            new ApartmentInfo("apt-5", "5", 3),
            new ApartmentInfo("apt-6", "6", 3),
            new ApartmentInfo("apt-7", "7", 4),
            new ApartmentInfo("apt-8", "8", 4)
    );

    private static final List<SensorType> SENSOR_TYPES = List.of(SensorType.values());

    @Scheduled(fixedDelayString = "${sensor.interval-ms}")
    public void generateAndSendEvent() {
        SensorEvent event = buildRandomEvent();

        kafkaTemplate.send(topic, event.getDeviceId(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent: {} | apartment={} | room={} | value={}",
                                event.getDeviceType(), event.getApartmentId(), event.getRoomId(),
                                event.getValue() != null ? event.getValue() : event.getNumericValue());
                    } else {
                        log.error("Error sending event: {}", ex.getMessage());
                    }
                });
    }

    private SensorEvent buildRandomEvent() {
        SensorType type = SENSOR_TYPES.get(random.nextInt(SENSOR_TYPES.size()));
        ApartmentInfo apartment = APARTMENTS.get(random.nextInt(APARTMENTS.size()));
        String room = ROOMS.get(random.nextInt(ROOMS.size()));
        String deviceId = apartment.getApartmentId() + "-" + type.name().toLowerCase() + "-" + room;

        SensorEvent.SensorEventBuilder builder = SensorEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .deviceId(deviceId)
                .deviceType(type)
                .apartmentId(apartment.getApartmentId())
                .roomId(room)
                .timestamp(Instant.now());

        boolean isDangerous = random.nextDouble() < dangerProbability;

        switch (type) {
            case SMOKE, WATER_LEAK, MOTION, DOOR_WINDOW -> {
                boolean value = isDangerous || random.nextDouble() < 0.05;
                builder.value(value);
            }
            case TEMPERATURE -> {
                double temp = isDangerous
                        ? 45 + random.nextDouble() * 15
                        : 18 + random.nextDouble() * 8;
                builder.numericValue(Math.round(temp * 10.0) / 10.0);
            }
        }

        return builder.build();
    }

}