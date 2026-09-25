package mk.smarthome.homeprocessor.service;

import mk.smarthome.homeprocessor.model.Alert;
import mk.smarthome.homeprocessor.model.SensorEvent;
import mk.smarthome.homeprocessor.model.Severity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class AlertRuleEvaluator {

    private static final double DANGEROUS_TEMPERATURE = 45.0;

    public Optional<Alert> evaluate(SensorEvent event) {
        return switch (event.getDeviceType()) {
            case SMOKE -> checkBinaryDanger(event, "Smoke detected");
            case WATER_LEAK -> checkBinaryDanger(event, "Water leak detected");
            case TEMPERATURE -> checkTemperature(event);
            case MOTION, DOOR_WINDOW -> Optional.empty();
        };
    }

    private Optional<Alert> checkBinaryDanger(SensorEvent event, String description) {
        if (Boolean.TRUE.equals(event.getValue())) {
            Severity severity = event.getDeviceType().name().equals("SMOKE")
                    ? Severity.CRITICAL
                    : Severity.WARNING;
            return Optional.of(buildAlert(event, severity, description));
        }
        return Optional.empty();
    }

    private Optional<Alert> checkTemperature(SensorEvent event) {
        if (event.getNumericValue() != null && event.getNumericValue() > DANGEROUS_TEMPERATURE) {
            String message = String.format("High temperature: %.1f °C", event.getNumericValue());
            return Optional.of(buildAlert(event, Severity.CRITICAL, message));
        }
        return Optional.empty();
    }

    private Alert buildAlert(SensorEvent event, Severity severity, String message) {
        return Alert.builder()
                .alertId(UUID.randomUUID().toString())
                .sourceEventId(event.getEventId())
                .deviceType(event.getDeviceType())
                .apartmentId(event.getApartmentId())
                .roomId(event.getRoomId())
                .severity(severity)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

}