package mk.smarthome.homeprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.smarthome.homeprocessor.model.*;
import mk.smarthome.homeprocessor.repository.AlertRepository;
import mk.smarthome.homeprocessor.repository.ApartmentRepository;
import mk.smarthome.homeprocessor.repository.NotificationRepository;
import mk.smarthome.homeprocessor.repository.SensorEventRepository;
import mk.smarthome.homeprocessor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorEventListener {

    private final SensorEventRepository sensorEventRepository;
    private final AlertRepository alertRepository;
    private final AlertRuleEvaluator alertRuleEvaluator;
    private final KafkaTemplate<String, Alert> alertKafkaTemplate;
    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Value("${sensor.alert-topic}")
    private String alertTopic;

    @KafkaListener(topics = "${sensor.input-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleSensorEvent(SensorEvent event) {
        log.info("Received: {} | apartment={} | room={} | value={}",
                event.getDeviceType(), event.getApartmentId(), event.getRoomId(),
                event.getValue() != null ? event.getValue() : event.getNumericValue());

        SensorEventDocument document = SensorEventDocument.builder()
                .eventId(event.getEventId())
                .deviceId(event.getDeviceId())
                .deviceType(event.getDeviceType())
                .apartmentId(event.getApartmentId())
                .roomId(event.getRoomId())
                .value(event.getValue())
                .numericValue(event.getNumericValue())
                .timestamp(event.getTimestamp())
                .build();
        sensorEventRepository.save(document);

        Optional<Alert> alertOpt = alertRuleEvaluator.evaluate(event);

        if (alertOpt.isPresent()) {
            Alert alert = alertOpt.get();
            alertRepository.save(alert);
            alertKafkaTemplate.send(alertTopic, alert.getApartmentId(), alert);
            log.warn("ALERT generated: [{}] apartment={} room={} - {}",
                    alert.getSeverity(), alert.getApartmentId(), alert.getRoomId(), alert.getMessage());

            createNotifications(alert);
        }
    }

    private void createNotifications(Alert alert) {
        // 1. Notifikacija do stanarot na konkretniot stan
        apartmentRepository.findByApartmentId(alert.getApartmentId())
                .ifPresent(apartment -> saveNotification(alert, apartment.getOwnerUsername()));

        // 2. Notifikacija do site ADMIN korisnici
        List<User> admins = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .toList();

        admins.forEach(admin -> saveNotification(alert, admin.getUsername()));
    }

    private void saveNotification(Alert alert, String recipientUsername) {
        Notification notification = Notification.builder()
                .notificationId(UUID.randomUUID().toString())
                .recipientUsername(recipientUsername)
                .alertId(alert.getAlertId())
                .deviceType(alert.getDeviceType())
                .apartmentId(alert.getApartmentId())
                .roomId(alert.getRoomId())
                .severity(alert.getSeverity())
                .message(alert.getMessage())
                .timestamp(Instant.now())
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

}